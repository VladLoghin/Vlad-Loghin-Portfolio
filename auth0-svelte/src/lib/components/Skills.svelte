<script context="module" lang="ts">
	export type Skill = {
		id: string;
		category: string;
		items: string[];
		active: boolean;
	};
</script>

<script lang="ts">
	import { onMount } from 'svelte';
	import { isAdmin, getToken } from '$lib/stores/auth';

	let skills: Skill[] = [];
	let loading = true;
	let showModal = false;
	let editingItem: Skill | null = null;

	let draftCategory = '';
	let draftItems: string[] = [];

	onMount(async () => {
		await fetchSkills();
	});

	async function fetchSkills() {
		loading = true;
		try {
			const response = await fetch('/api/skills');
			if (response.ok) {
				skills = await response.json();
			}
		} catch (error) {
			console.error('Failed to fetch skills:', error);
		} finally {
			loading = false;
		}
	}

	function openCreateModal() {
		editingItem = null;
		draftCategory = '';
		draftItems = [];
		showModal = true;
		document.body.style.overflow = 'hidden';
	}

	function openEditModal(skill: Skill) {
		editingItem = skill;
		draftCategory = skill.category;
		draftItems = [...skill.items];
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

	function addItem() {
		draftItems = [...draftItems, ''];
	}

	function updateItem(i: number, value: string) {
		const next = draftItems.slice();
		next[i] = value;
		draftItems = next;
	}

	function removeItem(i: number) {
		draftItems = draftItems.filter((_, idx) => idx !== i);
	}

	async function handleSubmit() {
		const token = await getToken();
		if (!token) return;

		const method = editingItem ? 'PUT' : 'POST';
		const url = editingItem ? `/api/skills/${editingItem.id}` : '/api/skills';

		const body = {
			skillName: draftCategory.trim(),
			subskills: draftItems.map(s => s.trim()).filter(Boolean)
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
				await fetchSkills();
				closeModal();
			}
		} catch (error) {
			console.error('Failed to save skill:', error);
		}
	}

	async function toggleActive(skill: Skill) {
		const token = await getToken();
		if (!token) return;

		try {
			const response = await fetch(
				`/api/skills/${skill.id}/active?active=${!skill.active}`,
				{
					method: 'PATCH',
					headers: { 'Authorization': `Bearer ${token}` }
				}
			);
			if (response.ok) await fetchSkills();
		} catch (error) {
			console.error('Failed to toggle active:', error);
		}
	}

	async function deleteSkill(id: string) {
		if (!confirm('Are you sure you want to delete this skill?')) return;

		const token = await getToken();
		if (!token) return;

		try {
			const response = await fetch(`/api/skills/${id}`, {
				method: 'DELETE',
				headers: { 'Authorization': `Bearer ${token}` }
			});
			if (response.ok) await fetchSkills();
		} catch (error) {
			console.error('Failed to delete skill:', error);
		}
	}

	$: displayItems = $isAdmin ? skills : skills.filter(s => s.active);
</script>

<svelte:window on:keydown={handleKeydown} />

<section id="skills" class="section shell snap-section">
	<div class="section-head">
		<div class="section-pill">
			<h2 class="h2">Skills</h2>
		</div>
		<p class="lead">Technologies and practices I use to build solid, maintainable software.</p>

		{#if $isAdmin}
			<button class="btn-new" type="button" on:click={openCreateModal}>+ New Skill</button>
		{/if}
	</div>

	{#if loading}
		<div class="loading">Loading skills...</div>
	{:else if displayItems.length === 0}
		<div class="empty-state">
			{#if $isAdmin}
				<p>No skills yet. Click "New Skill" to create one.</p>
			{:else}
				<p>No skills available.</p>
			{/if}
		</div>
	{:else}
		<div class="skills-grid">
			{#each displayItems as skill (skill.id)}
				<article class="card skill-card" class:inactive-card={!skill.active}>
					{#if $isAdmin}
						<button class="pen-btn" type="button" aria-label="Edit {skill.category}" on:click={() => openEditModal(skill)}>
							<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
								<path d="M12 20h9" />
								<path d="M16.5 3.5a2.1 2.1 0 0 1 3 3L8 18l-4 1 1-4 11.5-11.5z" />
							</svg>
						</button>
					{/if}

					<h3 class="h3">{skill.category}</h3>

					<div class="skill-items">
						{#each skill.items as item}
							<span class="skill-badge">{item}</span>
						{/each}
					</div>

					{#if $isAdmin}
						<div class="admin-section">
							<div class="approval-badge {skill.active ? 'approved' : 'pending'}">
								{skill.active ? 'Active' : 'Inactive'}
							</div>
							<div class="admin-actions">
								<button class="btn-approve {skill.active ? 'hide' : 'approve'}" on:click={() => toggleActive(skill)}>
									{skill.active ? 'Hide' : 'Show'}
								</button>
								<button class="btn-delete" on:click={() => deleteSkill(skill.id)}>Delete</button>
							</div>
						</div>
					{/if}
				</article>
			{/each}
		</div>
	{/if}
</section>

<!-- svelte-ignore a11y_click_events_have_key_events a11y_no_static_element_interactions -->
{#if showModal}
	<div class="modal-layer" on:click={closeModal}>
		<div class="modal" role="dialog" aria-modal="true" on:click|stopPropagation>
			<div class="modal-head">
				<h3 class="h3" style="margin:0;">{editingItem ? 'Edit Skill' : 'New Skill'}</h3>
				<button class="icon-x" type="button" aria-label="Close" on:click={closeModal}>✕</button>
			</div>

			<div class="modal-body">
				<label class="label">
					Category
					<input class="input modal-input" bind:value={draftCategory} placeholder="e.g. Backend" />
				</label>

				<div class="items-block">
					<div class="items-head">
						<p class="p" style="margin:0;font-weight:700;">Items</p>
						<button class="btn small" type="button" on:click={addItem}>+ Item</button>
					</div>

					{#each draftItems as it, i (i)}
						<div class="item-row">
							<input
								class="input modal-input item-input"
								value={it}
								on:input={(e) => updateItem(i, (e.currentTarget).value)}
								placeholder="e.g. Spring Boot"
							/>
							<button class="icon-btn remove-btn" type="button" aria-label="Remove item" on:click={() => removeItem(i)}>
								-
							</button>
						</div>
					{/each}
				</div>
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

	.skills-grid {
		display: grid; grid-template-columns: repeat(3, 1fr);
		gap: 18px; margin-top: 32px;
	}
	@media (max-width: 980px) { .skills-grid { grid-template-columns: repeat(2, 1fr); } }
	@media (max-width: 640px) { .skills-grid { grid-template-columns: 1fr; } }

	.skill-card {
		position: relative;
		transition: transform 0.25s ease, box-shadow 0.25s ease;
	}
	.skill-card:hover { transform: translateY(-2px); box-shadow: 0 24px 70px rgba(0,0,0,0.2); }

	.inactive-card { opacity: 0.6; border: 2px dashed rgba(255, 152, 0, 0.4); }

	.skill-items { display: flex; flex-wrap: wrap; gap: 8px; }
	.skill-badge {
		display: inline-block; padding: 6px 12px;
		background: rgba(56, 197, 94, 0.1); color: var(--green-d);
		border-radius: 6px; font-size: 13px; font-weight: 600;
		border: 1px solid rgba(56, 197, 94, 0.2);
	}

	.pen-btn {
		position: absolute; top: 12px; right: 12px;
		width: 34px; height: 34px; border-radius: 10px;
		border: 1px solid rgba(0,0,0,0.12); background: rgba(0,0,0,0.05);
		color: rgba(0,0,0,0.55); cursor: pointer;
		display: grid; place-items: center;
		transition: transform 0.15s ease, background 0.15s ease;
	}
	.pen-btn:hover { transform: translateY(-1px); background: rgba(0,0,0,0.08); }
	.pen-btn svg { width: 16px; height: 16px; }

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
	.items-block { display: grid; gap: 14px; padding-top: 4px; }
	.items-head { display: flex; justify-content: space-between; align-items: center; gap: 10px; }
	.item-row { position: relative; }
	.item-input { padding-right: 52px; box-sizing: border-box; }
	.icon-btn {
		width: 32px; height: 32px; border-radius: 6px;
		border: 1px solid rgba(0,0,0,0.12); background: rgba(0,0,0,0.04);
		cursor: pointer; font-weight: 900; font-size: 12px;
		display: grid; place-items: center; padding: 0;
	}
	.remove-btn { position: absolute; right: 10px; top: 50%; transform: translateY(-50%); }
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
	.btn.small { padding: 8px 10px; font-size: 13px; }
	.btn.ghost { background: transparent; }
	.btn.primary { border-color: rgba(56,197,94,0.35); background: rgba(56,197,94,0.16); color: var(--green); }
	.btn.primary:hover:not(:disabled) { background: rgba(56,197,94,0.25); }
</style>
