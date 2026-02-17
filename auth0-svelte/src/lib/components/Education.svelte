<script context="module" lang="ts">
	export type EducationItem = {
		id: string;
		institutionName: string;
		degree: string;
		active: boolean;
		displayOrder?: number;
	};
</script>

<script lang="ts">
	import { onMount } from 'svelte';
	import { isAdmin, getToken } from '$lib/stores/auth';

	const API_BASE = '/api';

	let educationItems: EducationItem[] = [];
	let loading = true;
	let showModal = false;
	let editingItem: EducationItem | null = null;

	let formData = {
		institutionName: '',
		degree: ''
	};

	let currentIndex = 0;
	let scrollContainer: HTMLDivElement;
	let canScrollLeft = false;
	let canScrollRight = true;

	// Fetch education items on mount
	onMount(async () => {
		await fetchEducation();
	});

	async function fetchEducation() {
		loading = true;
		try {
			const response = await fetch(`${API_BASE}/education`);
			if (response.ok) {
				const data = await response.json();
				educationItems = data;
			}
		} catch (error) {
			console.error('Failed to fetch education:', error);
		} finally {
			loading = false;
		}
	}

	function openCreateModal() {
		editingItem = null;
		formData = {
			institutionName: '',
			degree: ''
		};
		showModal = true;
		lockBodyScroll();
	}

	function openEditModal(item: EducationItem) {
		editingItem = item;
		formData = {
			institutionName: item.institutionName,
			degree: item.degree
		};
		showModal = true;
		lockBodyScroll();
	}

	function closeModal() {
		showModal = false;
		editingItem = null;
		formData = {
			institutionName: '',
			degree: ''
		};
		unlockBodyScroll();
	}

	function lockBodyScroll() {
		document.body.style.overflow = 'hidden';
	}

	function unlockBodyScroll() {
		document.body.style.overflow = '';
	}

	function handleKeydown(event: KeyboardEvent) {
		if (event.key === 'Escape' && showModal) {
			closeModal();
		}
	}

	async function handleSubmit() {
		const token = await getToken();
		if (!token) return;

		const method = editingItem ? 'PUT' : 'POST';
		const url = editingItem
			? `${API_BASE}/education/${editingItem.id}`
			: `${API_BASE}/education`;

		try {
			const response = await fetch(url, {
				method,
				headers: {
					'Content-Type': 'application/json',
					'Authorization': `Bearer ${token}`
				},
				body: JSON.stringify(formData)
			});

			if (response.ok) {
				await fetchEducation();
				closeModal();
			}
		} catch (error) {
			console.error('Failed to save education:', error);
		}
	}

	async function toggleActive(item: EducationItem) {
		const token = await getToken();
		if (!token) return;

		try {
			const response = await fetch(
				`${API_BASE}/education/${item.id}/active?active=${!item.active}`,
				{
					method: 'PATCH',
					headers: {
						'Authorization': `Bearer ${token}`
					}
				}
			);

			if (response.ok) {
				await fetchEducation();
			}
		} catch (error) {
			console.error('Failed to toggle active status:', error);
		}
	}

	async function deleteEducation(id: string) {
		if (!confirm('Are you sure you want to delete this education entry?')) {
			return;
		}

		const token = await getToken();
		if (!token) return;

		try {
			const response = await fetch(`${API_BASE}/education/${id}`, {
				method: 'DELETE',
				headers: {
					'Authorization': `Bearer ${token}`
				}
			});

			if (response.ok) {
				await fetchEducation();
				checkScroll();
			}
		} catch (error) {
			console.error('Failed to delete education:', error);
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

	let draggedId: string | null = null;
	let dragOverId: string | null = null;

	function handleDragStart(e: DragEvent, id: string) {
		draggedId = id;
		if (e.dataTransfer) e.dataTransfer.effectAllowed = 'move';
	}

	function handleDragOver(e: DragEvent, id: string) {
		e.preventDefault();
		if (e.dataTransfer) e.dataTransfer.dropEffect = 'move';
		if (id !== draggedId) dragOverId = id;
	}

	function handleDragLeave() {
		dragOverId = null;
	}

	function handleDrop(e: DragEvent, targetId: string) {
		e.preventDefault();
		dragOverId = null;
		if (!draggedId || draggedId === targetId) return;

		const fromIdx = educationItems.findIndex(i => i.id === draggedId);
		const toIdx = educationItems.findIndex(i => i.id === targetId);
		if (fromIdx < 0 || toIdx < 0) return;

		const item = educationItems[fromIdx];
		educationItems.splice(fromIdx, 1);
		educationItems.splice(toIdx, 0, item);
		educationItems = educationItems;

		saveOrder();
	}

	function handleDragEnd() {
		draggedId = null;
		dragOverId = null;
	}

	async function saveOrder() {
		const token = await getToken();
		if (!token) return;

		try {
			await fetch(`${API_BASE}/education/reorder`, {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/json',
					'Authorization': `Bearer ${token}`
				},
				body: JSON.stringify(educationItems.map(i => i.id))
			});
		} catch (error) {
			console.error('Failed to save order:', error);
			await fetchEducation();
		}
	}

	$: visibleItems = $isAdmin
		? educationItems
		: educationItems.filter(item => item.active);

	$: displayItems = $isAdmin ? educationItems : visibleItems;
</script>

<svelte:window on:keydown={handleKeydown} />

<section id="education" class="section shell snap-section">
	<div class="section-head">
		<div class="section-pill">
			<h2 class="h2">Education</h2>
		</div>
		<p class="lead">Academic Background</p>

		{#if $isAdmin}
			<button class="btn-new-education" type="button" on:click={openCreateModal}>
				+ Add Education
			</button>
		{/if}
	</div>

	{#if loading}
		<div class="loading">Loading education...</div>
	{:else if displayItems.length === 0}
		<div class="empty-state">
			{#if $isAdmin}
				<p>No education entries yet. Click "Add Education" to create one.</p>
			{:else}
				<p>No education information available.</p>
			{/if}
		</div>
	{:else if $isAdmin}
		<div class="admin-grid">
			{#each displayItems as item (item.id)}
				<article
				class="card education-card grid-card"
				class:inactive-card={!item.active}
				class:dragging={draggedId === item.id}
				class:drag-over={dragOverId === item.id}
				draggable="true"
				on:dragstart={(e) => handleDragStart(e, item.id)}
				on:dragover={(e) => handleDragOver(e, item.id)}
				on:dragleave={handleDragLeave}
				on:drop={(e) => handleDrop(e, item.id)}
				on:dragend={handleDragEnd}
			>
					<div class="edu-icon">
						<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="32" height="32">
							<path d="M22 10v6M2 10l10-5 10 5-10 5z" />
							<path d="M6 12v5c0 1.66 2.69 3 6 3s6-1.34 6-3v-5" />
						</svg>
					</div>
					<h3 class="h3 edu-institution">{item.institutionName}</h3>
					<p class="p muted edu-degree">{item.degree}</p>

					<div class="admin-section">
						<div class="approval-badge {item.active ? 'approved' : 'pending'}">
							{item.active ? 'Active' : 'Inactive'}
						</div>
						<div class="admin-actions">
							<button
								class="btn-approve {item.active ? 'hide' : 'approve'}"
								on:click={() => toggleActive(item)}
							>
								{item.active ? 'Hide' : 'Show'}
							</button>
							<button class="btn-edit" on:click={() => openEditModal(item)}>
								Edit
							</button>
							<button class="btn-delete" on:click={() => deleteEducation(item.id)}>
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
				aria-label="Scroll education left"
			>
				<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
					<polyline points="15 18 9 12 15 6"></polyline>
				</svg>
			</button>

			<div
				class="education-carousel"
				bind:this={scrollContainer}
				on:scroll={checkScroll}
			>
				{#each displayItems as item (item.id)}
					<article class="card education-card" class:inactive-card={!item.active}>
						<div class="edu-icon">
							<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="32" height="32">
								<path d="M22 10v6M2 10l10-5 10 5-10 5z" />
								<path d="M6 12v5c0 1.66 2.69 3 6 3s6-1.34 6-3v-5" />
							</svg>
						</div>
						<h3 class="h3 edu-institution">{item.institutionName}</h3>
						<p class="p muted edu-degree">{item.degree}</p>
					</article>
				{/each}
			</div>

			<button
				class="carousel-btn carousel-btn--right"
				on:click={() => scroll('right')}
				aria-label="Scroll education right"
			>
				<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
					<polyline points="9 18 15 12 9 6"></polyline>
				</svg>
			</button>
		</div>
	{/if}
</section>

{#if showModal}
	<div class="modal-layer" role="presentation" on:click={closeModal}>
		<div class="modal" role="dialog" aria-modal="true" on:click|stopPropagation>
			<div class="modal-head">
				<h3 class="h3" style="margin:0;">
					{editingItem ? 'Edit Education' : 'Add Education'}
				</h3>
				<button class="icon-x" type="button" aria-label="Close" on:click={closeModal}>✕</button>
			</div>

			<div class="modal-body">
				<label class="label">
					Institution Name
					<input
						class="input modal-input"
						bind:value={formData.institutionName}
						placeholder="e.g. Stanford University"
						required
					/>
				</label>

				<label class="label">
					Degree / Program
					<input
						class="input modal-input"
						bind:value={formData.degree}
						placeholder="e.g. Bachelor of Science in Computer Science"
						required
					/>
				</label>
			</div>

			<div class="modal-actions">
				<button class="btn ghost" type="button" on:click={closeModal}>
					Cancel
				</button>
				<button class="btn primary" type="button" on:click={handleSubmit}>
					{editingItem ? 'Update' : 'Create'}
				</button>
			</div>
		</div>
	</div>
{/if}

<style>
	.section-head {
		position: relative;
	}

	.btn-new-education {
		position: absolute;
		top: 0;
		right: 0;
		padding: 10px 16px;
		border-radius: 10px;
		border: none;
		background: rgba(56, 197, 94, 0.9);
		color: white;
		font-weight: 700;
		font-size: 13px;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.btn-new-education:hover {
		background: rgba(56, 197, 94, 1);
		transform: translateY(-1px);
	}

	.loading,
	.empty-state {
		text-align: center;
		padding: 3rem;
		color: #666;
		font-size: 1.1rem;
	}

	.admin-grid {
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		gap: 18px;
		margin-top: 32px;
	}
	@media (max-width: 980px) { .admin-grid { grid-template-columns: repeat(2, 1fr); } }
	@media (max-width: 640px) { .admin-grid { grid-template-columns: 1fr; } }

	.grid-card {
		flex: unset;
		max-width: unset;
		scroll-snap-align: unset;
		cursor: grab;
	}
	.grid-card:active { cursor: grabbing; }
	.grid-card.dragging { opacity: 0.4; }
	.grid-card.drag-over { border: 2px dashed rgba(56, 197, 94, 0.6); }

	.carousel-wrapper {
		display: flex;
		align-items: center;
		gap: 16px;
		position: relative;
		justify-content: center;
		max-width: 800px;
		margin: 0 auto;
	}

	.education-carousel {
		display: flex;
		gap: 200px;
		overflow: hidden;
		scroll-behavior: smooth;
		scroll-snap-type: x mandatory;
		flex: 1;
		scrollbar-width: none;
		padding: 0 calc(50% - 300px);
	}

	.education-carousel::-webkit-scrollbar {
		display: none;
	}

	.education-card {
		flex: 0 0 600px;
		max-width: 600px;
		scroll-snap-align: center;
		box-shadow: none;
		display: flex;
		flex-direction: column;
		align-items: center;
		text-align: center;
		padding: 32px 24px;
	}

	.inactive-card { opacity: 0.6; border: 2px dashed rgba(255, 152, 0, 0.4); }

	.edu-icon {
		color: var(--green);
		margin-bottom: 16px;
	}

	.edu-institution {
		margin: 0 0 8px;
		font-weight: 700;
	}

	.edu-degree {
		margin: 0 0 16px;
	}

	.admin-section {
		margin-top: 1.5rem;
		padding-top: 1.5rem;
		border-top: 1px solid rgba(0, 0, 0, 0.1);
		width: 100%;
	}

	.approval-badge {
		display: inline-block;
		padding: 0.4rem 1rem;
		border-radius: 20px;
		font-size: 0.9rem;
		font-weight: 600;
		margin-bottom: 1rem;
	}

	.approval-badge.approved {
		background: rgba(56, 197, 94, 0.2);
		color: rgba(56, 197, 94, 1);
	}

	.approval-badge.pending {
		background: rgba(255, 152, 0, 0.2);
		color: rgb(255, 152, 0);
	}

	.admin-actions {
		display: flex;
		gap: 0.5rem;
		justify-content: center;
		flex-wrap: wrap;
	}

	.btn-approve,
	.btn-edit,
	.btn-delete {
		padding: 0.5rem 1rem;
		border: none;
		border-radius: 6px;
		font-size: 0.9rem;
		font-weight: 600;
		cursor: pointer;
		transition: all 0.3s ease;
	}

	.btn-approve.approve {
		background: rgba(56, 197, 94, 1);
		color: white;
	}

	.btn-approve.approve:hover {
		background: rgba(56, 197, 94, 0.9);
	}

	.btn-approve.hide {
		background: #f44336;
		color: white;
	}

	.btn-approve.hide:hover {
		background: #d32f2f;
	}

	.btn-edit {
		background: #2196f3;
		color: white;
	}

	.btn-edit:hover {
		background: #1976d2;
	}

	.btn-delete {
		background: #f44336;
		color: white;
	}

	.btn-delete:hover {
		background: #d32f2f;
	}

	@media (max-width: 980px) {
		.education-carousel {
			padding: 0 calc(50% - 250px);
		}

		.education-card {
			flex: 0 0 500px;
			max-width: 500px;
		}
	}

	@media (max-width: 640px) {
		.education-carousel {
			padding: 0 20px;
		}

		.education-card {
			flex: 0 0 calc(100% - 40px);
			max-width: 100%;
		}
	}

	.carousel-btn {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 44px;
		height: 44px;
		border-radius: 50%;
		border: none;
		background: var(--green);
		color: white;
		cursor: pointer;
		transition: all 0.25s ease;
		flex-shrink: 0;
		z-index: 10;
	}

	.carousel-btn:hover:not(.disabled) {
		transform: scale(1.1);
		box-shadow: 0 8px 24px rgba(56, 197, 94, 0.3);
	}

	.carousel-btn.disabled {
		opacity: 0.4;
		cursor: not-allowed;
	}

	.carousel-btn svg {
		width: 20px;
		height: 20px;
	}

	/* ========== MODAL STYLES ========== */
	.modal-layer {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background: rgba(0, 0, 0, 0.55);
		z-index: 9999;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 20px;
		overflow-y: auto;
	}

	.modal {
		position: relative;
		width: min(640px, 100%);
		max-height: 85vh;
		background: white;
		border-radius: 14px;
		box-shadow: 0 30px 120px rgba(0, 0, 0, 0.35);
		overflow: hidden;
		display: flex;
		flex-direction: column;
		z-index: 10000;
		margin: auto;
	}

	.modal-head {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 16px 18px;
		border-bottom: 1px solid #eee;
		flex-shrink: 0;
	}

	.icon-x {
		border: none;
		background: transparent;
		font-size: 18px;
		cursor: pointer;
		opacity: 0.7;
		padding: 0;
		width: 32px;
		height: 32px;
		display: grid;
		place-items: center;
	}

	.icon-x:hover {
		opacity: 1;
	}

	.modal-body {
		padding: 18px;
		display: grid;
		gap: 18px;
		overflow-y: auto;
		flex: 1;
	}

	.label {
		display: grid;
		gap: 8px;
		font-weight: 700;
		font-size: 13px;
		color: #222;
	}

	.modal-input {
		background: #fff;
		border: 1px solid #e0e0e0;
		padding: 10px 12px;
		border-radius: 6px;
		font-family: inherit;
		font-size: 14px;
	}

	.modal-input:focus {
		outline: none;
		border-color: rgba(56, 197, 94, 0.5);
		box-shadow: 0 0 0 3px rgba(56, 197, 94, 0.1);
	}

	.modal-actions {
		display: flex;
		justify-content: flex-end;
		gap: 10px;
		padding: 14px 18px;
		border-top: 1px solid #eee;
		background: #fafafa;
		flex-shrink: 0;
	}

	.btn {
		padding: 10px 14px;
		border-radius: 10px;
		border: 1px solid rgba(0, 0, 0, 0.12);
		background: rgba(0, 0, 0, 0.04);
		cursor: pointer;
		font-weight: 800;
		transition: all 0.2s ease;
		font-size: 13px;
	}

	.btn:disabled {
		opacity: 0.6;
		cursor: not-allowed;
	}

	.btn.ghost {
		background: transparent;
	}

	.btn.primary {
		border-color: rgba(56, 197, 94, 0.35);
		background: rgba(56, 197, 94, 0.16);
		color: var(--green);
	}

	.btn.primary:hover:not(:disabled) {
		background: rgba(56, 197, 94, 0.25);
	}
</style>
