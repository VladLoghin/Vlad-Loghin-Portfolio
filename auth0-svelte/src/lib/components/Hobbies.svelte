<script lang="ts">
  let scrollContainer: HTMLDivElement;
  let canScrollLeft = false;
  let canScrollRight = true;

  export type Hobby = {
    id: string;
    title: string;
    description: string;
    tags?: string[];
  };

  export let title: string = "Hobbies";
  export let subtitle: string =
    "Outside of building software, I like creative projects that sharpen taste, iteration, and discipline.";
  export let hobbies: Hobby[] = [];
  export let isAdmin: boolean = false;
  export let onSave: ((next: Hobby[]) => Promise<void> | void) | undefined;

  let editOpen = false;
  let saving = false;
  let error = "";

  let draftTitle = "";
  let draftDescription = "";
  let draftTags: string[] = [];

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

  function openNewHobby() {
    draftTitle = "";
    draftDescription = "";
    draftTags = [];
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

  async function saveHobby() {
    const cleaned = {
      id: `hobby-${Date.now()}`,
      title: draftTitle.trim(),
      description: draftDescription.trim(),
      tags: draftTags.map((x) => x.trim()).filter(Boolean)
    };

    if (!cleaned.title || !cleaned.description) {
      error = "Please fill out all fields";
      return;
    }

    saving = true;
    error = "";
    try {
      hobbies = [...hobbies, cleaned];
      await onSave?.(hobbies);
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

<section id="hobbies" class="section shell snap-section">
  <div class="section-head">
    <div class="section-pill">
      <h2 class="h2">{title}</h2>
    </div>
    <p class="lead">{subtitle}</p>
    {#if isAdmin}
      <button class="btn-new-hobby" type="button" on:click={openNewHobby}>
        + New Hobby
      </button>
    {/if}
  </div>

  <div class="carousel-wrapper">
    <button
      class="carousel-btn carousel-btn--left"
      class:disabled={!canScrollLeft}
      on:click={() => scroll("left")}
      aria-label="Scroll hobbies left"
    >
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <polyline points="15 18 9 12 15 6"></polyline>
      </svg>
    </button>

    <div
      class="hobbies-carousel"
      bind:this={scrollContainer}
      on:scroll={checkScroll}
    >
      {#each hobbies as hobby (hobby.id)}
        <article class="card hobby-card">
          <h3 class="h3">{hobby.title}</h3>
          <p class="p muted">{hobby.description}</p>
          {#if hobby.tags?.length}
            <div class="pill-row">
              {#each hobby.tags as tag}
                <span class="pill">{tag}</span>
              {/each}
            </div>
          {/if}
        </article>
      {/each}
    </div>

    <button
      class="carousel-btn carousel-btn--right"
      class:disabled={!canScrollRight}
      on:click={() => scroll("right")}
      aria-label="Scroll hobbies right"
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
        <h3 class="h3" style="margin:0;">Add Hobby</h3>
        <button class="icon-x" type="button" aria-label="Close" on:click={closeEdit}>✕</button>
      </div>

      <div class="modal-body">
        <label class="label">
          Title
          <input
            class="input modal-input"
            value={draftTitle}
            on:input={(e) => (draftTitle = (e.currentTarget as HTMLInputElement).value)}
            placeholder="e.g. Music"
          />
        </label>

        <label class="label">
          Description
          <textarea
            class="input modal-input textarea-input"
            value={draftDescription}
            on:input={(e) => (draftDescription = (e.currentTarget as HTMLTextAreaElement).value)}
            placeholder="Describe your hobby..."
            rows="5"
          />
        </label>

        <label class="label">
          Tags (comma separated)
          <textarea
            class="input modal-input textarea-input"
            rows="2"
            on:input={(e) => (draftTags = (e.currentTarget as HTMLTextAreaElement).value.split(",").map((x) => x.trim()).filter(Boolean))}
          >{draftTags.join(", ")}</textarea>
        </label>

        {#if error}
          <p class="p small" style="margin:10px 0 0;color:#ff5252;">{error}</p>
        {/if}
      </div>

      <div class="modal-actions">
        <button class="btn ghost" type="button" on:click={closeEdit} disabled={saving}>
          Cancel
        </button>
        <button class="btn primary" type="button" on:click={saveHobby} disabled={saving}>
          {saving ? "Saving..." : "Save"}
        </button>
      </div>
    </div>
  </div>
{/if}

<style>
  .section-head {
    position: relative;
  }

  .btn-new-hobby {
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

  .btn-new-hobby:hover {
    background: rgba(56, 197, 94, 1);
    transform: translateY(-1px);
  }

  .carousel-wrapper {
    display: flex;
    align-items: center;
    gap: 16px;
    position: relative;
    justify-content: center;
    max-width: 800px;
    margin: 0 auto;
  }

  .hobbies-carousel {
    display: flex;
    gap: 200px;
    overflow: hidden;
    scroll-behavior: smooth;
    scroll-snap-type: x mandatory;
    flex: 1;
    scrollbar-width: none;
    padding: 0 calc(50% - 300px);
  }

  .hobbies-carousel::-webkit-scrollbar {
    display: none;
  }

  .hobby-card {
    flex: 0 0 600px;
    max-width: 600px;
    scroll-snap-align: center;
    box-shadow: none;
  }

  @media (max-width: 980px) {
    .hobbies-carousel {
      padding: 0 calc(50% - 250px);
    }

    .hobby-card {
      flex: 0 0 500px;
      max-width: 500px;
    }
  }

  @media (max-width: 640px) {
    .hobbies-carousel {
      padding: 0 20px;
    }

    .hobby-card {
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