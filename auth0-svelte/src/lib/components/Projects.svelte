<script context="module" lang="ts">
	export type Project = {
		id: string;
		name: string;
		badge?: string;
		description: string;
		tags?: string[];
		githubUrl?: string;
		active: boolean;
	};
</script>

<script lang="ts">
	import { onMount } from 'svelte';
	import { isAdmin, getToken } from '$lib/stores/auth';

	let projects: Project[] = [];
	let loading = true;
	let showModal = false;
	let editingItem: Project | null = null;

	let formData = {
		projectName: '',
		tag: '',
		description: '',
		skills: [] as string[],
		githubUrl: ''
	};
	let skillsInput = '';

	let scrollContainer: HTMLDivElement;
	let canScrollLeft = false;
	let canScrollRight = true;

	onMount(async () => {
		await fetchProjects();
	});

	async function fetchProjects() {
		loading = true;
		try {
			const response = await fetch('/api/projects');
			if (response.ok) {
				projects = await response.json();
			}
		} catch (error) {
			console.error('Failed to fetch projects:', error);
		} finally {
			loading = false;
		}
	}

	function openCreateModal() {
		editingItem = null;
		formData = { projectName: '', tag: '', description: '', skills: [], githubUrl: '' };
		skillsInput = '';
		showModal = true;
		document.body.style.overflow = 'hidden';
	}

	function openEditModal(item: Project) {
		editingItem = item;
		formData = {
			projectName: item.name,
			tag: item.badge || '',
			description: item.description,
			skills: [...(item.tags || [])],
			githubUrl: item.githubUrl || ''
		};
		skillsInput = (item.tags || []).join(', ');
		showModal = true;
		document.body.style.overflow = 'hidden';
	}

	function closeModal() {
		showModal = false;
		editingItem = null;
		document.body.style.overflow = '';
	}

	function handleKeydown(event: KeyboardEvent) {
		if (event.key === 'Escape' && showModal) closeModal();
	}

	async function handleSubmit() {
		const token = await getToken();
		if (!token) return;

		const method = editingItem ? 'PUT' : 'POST';
		const url = editingItem ? `/api/projects/${editingItem.id}` : '/api/projects';

		const body = {
			projectName: formData.projectName,
			tag: formData.tag,
			description: formData.description,
			skills: skillsInput.split(',').map(s => s.trim()).filter(Boolean),
			githubUrl: formData.githubUrl || null
		};

		try {
			const response = await fetch(url, {
				method,
				headers: {
					'Content-Type': 'application/json',
					'Authorization': `Bearer ${token}`
				},
				body: JSON.stringify(body)
			});

			if (response.ok) {
				await fetchProjects();
				closeModal();
			}
		} catch (error) {
			console.error('Failed to save project:', error);
		}
	}

	async function toggleActive(item: Project) {
		const token = await getToken();
		if (!token) return;

		try {
			const response = await fetch(
				`/api/projects/${item.id}/active?active=${!item.active}`,
				{
					method: 'PATCH',
					headers: { 'Authorization': `Bearer ${token}` }
				}
			);
			if (response.ok) await fetchProjects();
		} catch (error) {
			console.error('Failed to toggle active:', error);
		}
	}

	async function deleteProject(id: string) {
		if (!confirm('Are you sure you want to delete this project?')) return;

		const token = await getToken();
		if (!token) return;

		try {
			const response = await fetch(`/api/projects/${id}`, {
				method: 'DELETE',
				headers: { 'Authorization': `Bearer ${token}` }
			});
			if (response.ok) await fetchProjects();
		} catch (error) {
			console.error('Failed to delete project:', error);
		}
	}

	function checkScroll() {
		if (!scrollContainer) return;
		canScrollLeft = scrollContainer.scrollLeft > 0;
		canScrollRight =
			scrollContainer.scrollLeft < scrollContainer.scrollWidth - scrollContainer.clientWidth - 10;
	}

	function scroll(direction: 'left' | 'right') {
		if (!scrollContainer) return;
		const children = Array.from(scrollContainer.children) as HTMLElement[];
		if (children.length === 0) return;

		const containerRect = scrollContainer.getBoundingClientRect();
		const centerX = containerRect.left + containerRect.width / 2;

		let closestIdx = 0;
		let minDist = Infinity;
		children.forEach((child, i) => {
			const r = child.getBoundingClientRect();
			const dist = Math.abs(r.left + r.width / 2 - centerX);
			if (dist < minDist) { minDist = dist; closestIdx = i; }
		});

		const nextIdx = direction === 'left'
			? (closestIdx === 0 ? children.length - 1 : closestIdx - 1)
			: (closestIdx === children.length - 1 ? 0 : closestIdx + 1);

		const targetRect = children[nextIdx].getBoundingClientRect();
		scrollContainer.scrollBy({
			left: targetRect.left + targetRect.width / 2 - centerX,
			behavior: 'smooth'
		});
	}

	$: displayItems = $isAdmin ? projects : projects.filter(p => p.active);
</script>

<svelte:window on:keydown={handleKeydown} />

<section id="projects" class="section shell snap-section">
	<div class="section-head">
		<div class="section-pill">
			<h2 class="h2">Projects</h2>
		</div>
		<p class="lead">A few builds that show how I think: clean architecture, shippable UX, and pragmatic engineering.</p>

		{#if $isAdmin}
			<button class="btn-new" type="button" on:click={openCreateModal}>+ New Project</button>
		{/if}
	</div>

	{#if loading}
		<div class="loading">Loading projects...</div>
	{:else if displayItems.length === 0}
		<div class="empty-state">
			{#if $isAdmin}
				<p>No projects yet. Click "New Project" to create one.</p>
			{:else}
				<p>No projects available.</p>
			{/if}
		</div>
	{:else if $isAdmin}
		<div class="admin-grid">
			{#each displayItems as project (project.id)}
				<article class="card project-card grid-card" class:inactive-card={!project.active}>
					<div class="project-top">
						<h3 class="h3">{project.name}</h3>
						<div class="project-actions-top">
							{#if project.badge}
								<span class="badge">{project.badge}</span>
							{/if}
							<button class="pen-button" type="button" aria-label="Edit {project.name}" on:click={() => openEditModal(project)}>
								<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16">
									<path d="M12 20h9" />
									<path d="M16.5 3.5a2.1 2.1 0 0 1 3 3L8 18l-4 1 1-4 11.5-11.5z" />
								</svg>
							</button>
						</div>
					</div>

					<p class="p muted">{project.description}</p>

					{#if project.tags?.length}
						<div class="tag-row">
							{#each project.tags as tag}
								<span class="tag">{tag}</span>
							{/each}
						</div>
					{/if}

					{#if project.githubUrl}
						<a class="github-link" href={project.githubUrl} target="_blank" rel="noreferrer">
							<svg viewBox="0 0 24 24" fill="currentColor" width="18" height="18">
								<path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z"/>
							</svg>
							View on GitHub
						</a>
					{/if}

					<div class="admin-section">
						<div class="approval-badge {project.active ? 'approved' : 'pending'}">
							{project.active ? 'Active' : 'Inactive'}
						</div>
						<div class="admin-actions">
							<button
								class="btn-approve {project.active ? 'hide' : 'approve'}"
								on:click={() => toggleActive(project)}
							>
								{project.active ? 'Hide' : 'Show'}
							</button>
							<button class="btn-delete" on:click={() => deleteProject(String(project.id))}>
								Delete
							</button>
						</div>
					</div>
				</article>
			{/each}
		</div>
	{:else}
		<div class="carousel-wrapper">
			<button
				class="carousel-btn carousel-btn--left"
				on:click={() => scroll('left')}
				aria-label="Scroll projects left"
			>
				<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
					<polyline points="15 18 9 12 15 6"></polyline>
				</svg>
			</button>

			<div class="projects-carousel" bind:this={scrollContainer} on:scroll={checkScroll}>
				{#each displayItems as project (project.id)}
					<article class="card project-card" class:inactive-card={!project.active}>
						<div class="project-top">
							<h3 class="h3">{project.name}</h3>
							<div class="project-actions-top">
								{#if project.badge}
									<span class="badge">{project.badge}</span>
								{/if}
							</div>
						</div>

						<p class="p muted">{project.description}</p>

						{#if project.tags?.length}
							<div class="tag-row">
								{#each project.tags as tag}
									<span class="tag">{tag}</span>
								{/each}
							</div>
						{/if}

						{#if project.githubUrl}
							<a class="github-link" href={project.githubUrl} target="_blank" rel="noreferrer">
								<svg viewBox="0 0 24 24" fill="currentColor" width="18" height="18">
									<path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z"/>
								</svg>
								View on GitHub
							</a>
						{/if}
					</article>
				{/each}
			</div>

			<button
				class="carousel-btn carousel-btn--right"
				on:click={() => scroll('right')}
				aria-label="Scroll projects right"
			>
				<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
					<polyline points="9 18 15 12 9 6"></polyline>
				</svg>
			</button>
		</div>
	{/if}
</section>

<!-- svelte-ignore a11y_click_events_have_key_events a11y_no_static_element_interactions -->
{#if showModal}
	<div class="modal-layer" on:click={closeModal}>
		<div class="modal" role="dialog" aria-modal="true" on:click|stopPropagation>
			<div class="modal-head">
				<h3 class="h3" style="margin:0;">
					{editingItem ? 'Edit Project' : 'New Project'}
				</h3>
				<button class="icon-x" type="button" aria-label="Close" on:click={closeModal}>✕</button>
			</div>

			<div class="modal-body">
				<label class="label">
					Name
					<input class="input modal-input" bind:value={formData.projectName} placeholder="Project name" required />
				</label>

				<label class="label">
					Badge
					<input class="input modal-input" bind:value={formData.tag} placeholder="e.g. Full-stack" />
				</label>

				<label class="label">
					Description
					<textarea class="input modal-input textarea-input" rows="3" bind:value={formData.description} placeholder="Project description"></textarea>
				</label>

				<label class="label">
					GitHub URL
					<input class="input modal-input" bind:value={formData.githubUrl} placeholder="https://github.com/user/repo" type="url" />
				</label>

				<label class="label">
					Tags (comma separated)
					<input class="input modal-input" bind:value={skillsInput} placeholder="e.g. Spring Boot, React, Docker" />
				</label>
			</div>

			<div class="modal-actions">
				<button class="btn ghost" type="button" on:click={closeModal}>Cancel</button>
				<button class="btn primary" type="button" on:click={handleSubmit}>
					{editingItem ? 'Update' : 'Create'}
				</button>
			</div>
		</div>
	</div>
{/if}

<style>
	.section-head { position: relative; }

	.btn-new {
		position: absolute; top: 0; right: 0;
		padding: 10px 16px; border-radius: 10px; border: none;
		background: rgba(56, 197, 94, 0.9); color: white;
		font-weight: 700; font-size: 13px; cursor: pointer;
		transition: all 0.2s ease;
	}
	.btn-new:hover { background: rgba(56, 197, 94, 1); transform: translateY(-1px); }

	.loading, .empty-state { text-align: center; padding: 3rem; color: #666; font-size: 1.1rem; }

	.admin-grid {
		display: grid; grid-template-columns: repeat(3, 1fr);
		gap: 18px; margin-top: 32px;
	}
	@media (max-width: 980px) { .admin-grid { grid-template-columns: repeat(2, 1fr); } }
	@media (max-width: 640px) { .admin-grid { grid-template-columns: 1fr; } }
	.grid-card { flex: unset; max-width: unset; scroll-snap-align: unset; }

	.carousel-wrapper {
		display: flex; align-items: center; gap: 16px;
		position: relative; justify-content: center;
		max-width: 800px; margin: 0 auto;
	}

	.projects-carousel {
		display: flex; gap: 200px; overflow: hidden;
		scroll-behavior: smooth; scroll-snap-type: x mandatory;
		flex: 1; scrollbar-width: none;
		padding: 0 calc(50% - 300px);
	}
	.projects-carousel::-webkit-scrollbar { display: none; }

	.project-card {
		flex: 0 0 600px; max-width: 600px;
		scroll-snap-align: center; box-shadow: none;
	}

	.inactive-card { opacity: 0.6; border: 2px dashed rgba(255, 152, 0, 0.4); }

	.project-top {
		display: flex; align-items: flex-start;
		justify-content: space-between; margin-bottom: 16px; gap: 12px;
	}
	.project-actions-top { display: flex; align-items: center; gap: 12px; }

	.pen-button {
		width: 34px; height: 34px; border-radius: 10px;
		border: 1px solid rgba(0,0,0,0.12); background: rgba(0,0,0,0.05);
		color: rgba(0,0,0,0.4); cursor: pointer;
		display: grid; place-items: center;
		transition: transform 0.15s ease, background 0.15s ease; padding: 0;
	}
	.pen-button:hover { transform: translateY(-1px); background: rgba(0,0,0,0.08); }

	.badge {
		font-size: 12px; padding: 6px 10px; border-radius: 999px;
		border: 1px solid rgba(207,196,173,0.9);
		background: rgba(244,241,234,0.75); color: rgba(23,58,37,0.75);
		white-space: nowrap;
	}

	.admin-section {
		margin-top: 1.5rem; padding-top: 1.5rem;
		border-top: 1px solid rgba(0,0,0,0.1); width: 100%;
	}
	.approval-badge {
		display: inline-block; padding: 0.4rem 1rem; border-radius: 20px;
		font-size: 0.9rem; font-weight: 600; margin-bottom: 1rem;
	}
	.approval-badge.approved { background: rgba(56,197,94,0.2); color: rgba(56,197,94,1); }
	.approval-badge.pending { background: rgba(255,152,0,0.2); color: rgb(255,152,0); }
	.admin-actions { display: flex; gap: 0.5rem; justify-content: center; flex-wrap: wrap; }

	.btn-approve, .btn-delete {
		padding: 0.5rem 1rem; border: none; border-radius: 6px;
		font-size: 0.9rem; font-weight: 600; cursor: pointer;
		transition: all 0.3s ease;
	}
	.btn-approve.approve { background: rgba(56,197,94,1); color: white; }
	.btn-approve.approve:hover { background: rgba(56,197,94,0.9); }
	.btn-approve.hide { background: #f44336; color: white; }
	.btn-approve.hide:hover { background: #d32f2f; }
	.btn-delete { background: #f44336; color: white; }
	.btn-delete:hover { background: #d32f2f; }

	.carousel-btn {
		display: flex; align-items: center; justify-content: center;
		width: 44px; height: 44px; border-radius: 50%; border: none;
		background: var(--green); color: white; cursor: pointer;
		transition: all 0.25s ease; flex-shrink: 0; z-index: 10;
	}
	.carousel-btn:hover:not(.disabled) { transform: scale(1.1); box-shadow: 0 8px 24px rgba(56,197,94,0.3); }
	.carousel-btn.disabled { opacity: 0.4; cursor: not-allowed; }
	.carousel-btn svg { width: 20px; height: 20px; }

	@media (max-width: 980px) {
		.projects-carousel { padding: 0 calc(50% - 250px); }
		.project-card { flex: 0 0 500px; max-width: 500px; }
	}
	@media (max-width: 640px) {
		.projects-carousel { padding: 0 20px; }
		.project-card { flex: 0 0 calc(100% - 40px); max-width: 100%; }
	}

	/* MODAL */
	.modal-layer {
		position: fixed; top: 0; left: 0; width: 100%; height: 100%;
		background: rgba(0,0,0,0.55); z-index: 9999;
		display: flex; align-items: center; justify-content: center;
		padding: 20px; overflow-y: auto;
	}
	.modal {
		position: relative; width: min(640px, 100%); max-height: 85vh;
		background: white; border-radius: 14px;
		box-shadow: 0 30px 120px rgba(0,0,0,0.35);
		overflow: hidden; display: flex; flex-direction: column;
		z-index: 10000; margin: auto;
	}
	.modal-head {
		display: flex; justify-content: space-between; align-items: center;
		padding: 16px 18px; border-bottom: 1px solid #eee; flex-shrink: 0;
	}
	.icon-x {
		border: none; background: transparent; font-size: 18px;
		cursor: pointer; opacity: 0.7; padding: 0;
		width: 32px; height: 32px; display: grid; place-items: center;
	}
	.icon-x:hover { opacity: 1; }
	.modal-body { padding: 18px; display: grid; gap: 18px; overflow-y: auto; flex: 1; }
	.label { display: grid; gap: 8px; font-weight: 700; font-size: 13px; color: #222; }
	.modal-input {
		background: #fff; border: 1px solid #e0e0e0;
		padding: 10px 12px; border-radius: 6px;
		font-family: inherit; font-size: 14px;
	}
	.modal-input:focus { outline: none; border-color: rgba(56,197,94,0.5); box-shadow: 0 0 0 3px rgba(56,197,94,0.1); }
	.textarea-input { resize: vertical; min-height: 80px; }
	.modal-actions {
		display: flex; justify-content: flex-end; gap: 10px;
		padding: 14px 18px; border-top: 1px solid #eee;
		background: #fafafa; flex-shrink: 0;
	}
	.btn {
		padding: 10px 14px; border-radius: 10px;
		border: 1px solid rgba(0,0,0,0.12); background: rgba(0,0,0,0.04);
		cursor: pointer; font-weight: 800; transition: all 0.2s ease; font-size: 13px;
	}
	.btn:disabled { opacity: 0.6; cursor: not-allowed; }
	.btn.ghost { background: transparent; }
	.btn.primary { border-color: rgba(56,197,94,0.35); background: rgba(56,197,94,0.16); color: var(--green); }
	.btn.primary:hover:not(:disabled) { background: rgba(56,197,94,0.25); }

	.github-link {
		display: inline-flex; align-items: center; gap: 8px;
		margin-top: 12px; padding: 8px 14px; border-radius: 999px;
		border: 1px solid rgba(56,197,94,0.3); background: rgba(56,197,94,0.08);
		color: var(--green-d); font-weight: 700; font-size: 0.85rem;
		text-decoration: none; transition: all 0.2s ease; width: fit-content;
	}
	.github-link:hover { background: rgba(56,197,94,0.18); transform: translateY(-1px); }
	.github-link svg { width: 18px; height: 18px; flex-shrink: 0; }
</style>
