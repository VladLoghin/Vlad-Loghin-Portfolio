<script lang="ts">
  import { isAuthenticated } from "$lib/stores/auth";

  let scrollContainer: HTMLDivElement;
  let canScrollLeft = false;
  let canScrollRight = true;

  export type Review = {
    id: string;
    name: string;
    role: string;
    avatar: string;
    content: string;
    rating: number;
    approved?: boolean;
  };

  export let title: string = "Reviews";
  export let subtitle: string =
    "What people typically say after working with me: I ship, I communicate, and I clean up after myself.";
  export let reviews: Review[] = [];
  export let isAdmin: boolean = false;
  export let onSave: ((next: Review[]) => Promise<void> | void) | undefined;

  let editOpen = false;
  let saving = false;
  let error = "";

  let draftName = "";
  let draftRole = "";
  let draftContent = "";
  let draftRating = 5;

  function checkScroll() {
    if (!scrollContainer) return;
    canScrollLeft = scrollContainer.scrollLeft > 0;
    canScrollRight =
      scrollContainer.scrollLeft < scrollContainer.scrollWidth - scrollContainer.clientWidth - 10;
  }

  function scroll(direction: "left" | "right") {
    if (!scrollContainer) return;
    const amount = 400;
    scrollContainer.scrollBy({
      left: direction === "left" ? -amount : amount,
      behavior: "smooth"
    });
  }

  function openNewReview() {
    draftName = "";
    draftRole = "";
    draftContent = "";
    draftRating = 5;
    error = "";
    editOpen = true;
  }

  function closeEdit() {
    editOpen = false;
    error = "";
  }

  $: if (typeof document !== 'undefined') {
    if (editOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
  }

  async function saveReview() {
    const cleaned = {
      id: `review-${Date.now()}`,
      name: draftName.trim(),
      role: draftRole.trim(),
      avatar: "",
      content: draftContent.trim(),
      rating: draftRating,
      approved: false
    };

    if (!cleaned.name || !cleaned.role || !cleaned.content) {
      error = "Please fill out all fields";
      return;
    }

    saving = true;
    error = "";
    try {
      reviews = [...reviews, cleaned];
      await onSave?.(reviews);
      closeEdit();
    } catch (e: any) {
      error = e?.message ?? "Failed to save";
    } finally {
      saving = false;
    }
  }

  function onKeydown(e: KeyboardEvent) {
    if (e.key === "Escape" && editOpen) closeEdit();
  }
</script>

<svelte:window on:keydown={onKeydown} />

<section id="reviews" class="section shell snap-section">
  <div class="section-head">
    <div class="section-pill">
      <h2 class="h2">{title}</h2>
    </div>
    <p class="lead">{subtitle}</p>
    {#if isAuthenticated && !isAdmin}

    <button class="btn-new-review" type="button" on:click={openNewReview}>
      + New Review
    </button>
    {/if}
  </div>

  <div class="carousel-wrapper">
    <button
      class="carousel-btn carousel-btn--left"
      class:disabled={!canScrollLeft}
      on:click={() => scroll("left")}
      aria-label="Scroll reviews left"
    >
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <polyline points="15 18 9 12 15 6"></polyline>
      </svg>
    </button>

    <div
      class="reviews-carousel"
      bind:this={scrollContainer}
      on:scroll={checkScroll}
    >
      {#each reviews as review (review.id)}
        <article class="card review-card">
          <div class="review-meta">
            <p class="p review-name">{review.name}</p>
            <p class="p small muted">{review.role}</p>
          </div>
          <p class="p review-content">{review.content}</p>
          <div class="stars" aria-label="{review.rating} out of 5 stars">
            {#each Array(5) as _, i}
              <svg class="star" class:filled={i < review.rating} viewBox="0 0 24 24" fill="currentColor">
                <polygon points="12 2 15.09 10.26 24 10.35 17.77 16.01 19.85 24.29 12 18.54 4.15 24.29 6.23 16.01 0 10.35 8.91 10.26"/>
              </svg>
            {/each}
          </div>
        </article>
      {/each}
    </div>

    <button
      class="carousel-btn carousel-btn--right"
      class:disabled={!canScrollRight}
      on:click={() => scroll("right")}
      aria-label="Scroll reviews right"
    >
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <polyline points="9 18 15 12 9 6"></polyline>
      </svg>
    </button>
  </div>
</section>

{#if editOpen}
  
    <div class="modal-layer" role="presentation" on:click={closeEdit}>
      <div class="modal" role="dialog" aria-modal="true" on:click|stopPropagation>
        <div class="modal-head">
          <h3 class="h3" style="margin:0;">Add Review</h3>
          <button class="icon-x" type="button" aria-label="Close" on:click={closeEdit}>✕</button>
        </div>

        <div class="modal-body">
          <label class="label">
            Name
            <input
              class="input modal-input"
              value={draftName}
              on:input={(e) => (draftName = (e.currentTarget as HTMLInputElement).value)}
              placeholder="e.g. John Smith"
            />
          </label>

          <label class="label">
            Role / Company
            <input
              class="input modal-input"
              value={draftRole}
              on:input={(e) => (draftRole = (e.currentTarget as HTMLInputElement).value)}
              placeholder="e.g. CEO at TechCorp"
            />
          </label>

          <label class="label">
            Rating
            <select class="input modal-input" bind:value={draftRating}>
              <option value={5}>⭐ 5 Stars</option>
              <option value={4}>⭐ 4 Stars</option>
              <option value={3}>⭐ 3 Stars</option>
              <option value={2}>⭐ 2 Stars</option>
              <option value={1}>⭐ 1 Star</option>
            </select>
          </label>

          <label class="label">
            Review
            <textarea
              class="input modal-input textarea-input"
              value={draftContent}
              on:input={(e) => (draftContent = (e.currentTarget as HTMLTextAreaElement).value)}
              placeholder="Write your review here..."
              rows="5"
            />
          </label>

          {#if error}
            <p class="p small" style="margin:10px 0 0;color:#ff5252;">{error}</p>
          {/if}
        </div>

        <div class="modal-actions">
          <button class="btn ghost" type="button" on:click={closeEdit} disabled={saving}>
            Cancel
          </button>
          <button class="btn primary" type="button" on:click={saveReview} disabled={saving}>
            {saving ? "Saving..." : "Save"}
          </button>
        </div>
      </div>
    </div>
{/if}

<style>
  .carousel-wrapper {
    display: flex;
    align-items: center;
    gap: 16px;
    position: relative;
    justify-content: center;
    max-width: 800px;
    margin: 0 auto;
  }

  .reviews-carousel {
    display: flex;
    gap: 200px;
    overflow: hidden;
    scroll-behavior: smooth;
    scroll-snap-type: x mandatory;
    flex: 1;
    scrollbar-width: none;
    padding: 0 calc(50% - 300px);
  }

  .reviews-carousel::-webkit-scrollbar {
    display: none;
  }

  .review-card {
    flex: 0 0 600px;
    max-width: 600px;
    scroll-snap-align: center;
    box-shadow: none;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding: 24px;
  }

  @media (max-width: 980px) {
    .reviews-carousel {
      padding: 0 calc(50% - 250px);
    }
    
    .review-card {
      flex: 0 0 500px;
      max-width: 500px;
    }
  }

  @media (max-width: 640px) {
    .reviews-carousel {
      padding: 0 20px;
    }
    
    .review-card {
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

  .review-name {
    font-weight: 600;
    margin: 0;
  }

  .review-content {
    margin: 16px 0;
  }

  .stars {
    display: flex;
    gap: 8px;
    justify-content: center;
    margin-top: 16px;
  }

  .star {
    width: 24px;
    height: 24px;
    fill: #e0e0e0;
    transition: fill 0.2s ease;
  }

  .star.filled {
    fill: var(--green);
  }

  .section-head {
    position: relative;
  }

  .btn-new-review {
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

  .btn-new-review:hover {
    background: rgba(56, 197, 94, 1);
    transform: translateY(-1px);
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

  .textarea-input {
    resize: vertical;
    min-height: 120px;
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