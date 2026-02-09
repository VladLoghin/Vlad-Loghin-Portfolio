<script lang="ts">
  let scrollContainer: HTMLDivElement;
  let canScrollLeft = false;
  let canScrollRight = true;

  export type ProjectLink = {
    label: string;
    href: string;
    variant?: "primary" | "ghost";
    ariaLabel?: string;
  };

  export type Project = {
    id: string;
    name: string;
    badge?: string;
    badgeVariant?: "default" | "ghost";
    description: string;
    tags?: string[];
    links?: ProjectLink[];
  };

  export let title: string = "Projects";
  export let subtitle: string =
    "A few builds that show how I think: clean architecture, shippable UX, and pragmatic engineering.";

  export let projects: Project[] = [];
  export let isAdmin: boolean = false;
  export let onSave: ((next: Project[]) => Promise<void> | void) | undefined;

  let editOpen = false;
  let saving = false;
  let error = "";
  let editingId: string | null = null;
  let isNewProject = false;
  let draftName = "";
  let draftBadge = "";
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

  function openEdit(project: Project) {
    editingId = project.id;
    draftName = project.name;
    draftBadge = project.badge || "";
    draftDescription = project.description;
    draftTags = [...(project.tags || [])];
    error = "";
    editOpen = true;
  }

  function closeEdit() {
    editOpen = false;
    editingId = null;
    isNewProject = false;
    error = "";
  }

  function createNewProject() {
    const newId = `project-${Date.now()}`;
    editingId = newId;
    draftName = "";
    draftBadge = "";
    draftDescription = "";
    draftTags = [];
    error = "";
    editOpen = true;
    isNewProject = true;
  }

  async function saveEdit() {
    if (!editingId) return;

    const cleaned = {
      id: editingId,
      name: draftName.trim(),
      badge: draftBadge.trim() || undefined,
      description: draftDescription.trim(),
      tags: draftTags.map((x) => x.trim()).filter(Boolean),
      links: []
    };

    let nextProjects = projects;
    if (isNewProject) {
      nextProjects = [...projects, cleaned];
      isNewProject = false;
    } else {
      nextProjects = projects.map((p) => (p.id === editingId ? cleaned : p));
    }

    saving = true;
    error = "";
    try {
      await onSave?.(nextProjects);
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

<section id="projects" class="section shell snap-section">
  <div class="section-head">
    <div class="section-pill">
      <h2 class="h2">{title}</h2>
    </div>
    <p class="lead">{subtitle}</p>
    {#if isAdmin}
      <button class="btn-new-project" type="button" on:click={createNewProject}>
        + New Project
      </button>
    {/if}
  </div>

  <div class="carousel-wrapper">
    <button
      class="carousel-btn carousel-btn--left"
      class:disabled={!canScrollLeft}
      on:click={() => scroll("left")}
      aria-label="Scroll projects left"
    >
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <polyline points="15 18 9 12 15 6"></polyline>
      </svg>
    </button>

    <div
      class="projects-carousel"
      bind:this={scrollContainer}
      on:scroll={checkScroll}
    >
      {#each projects as project (project.id)}
        <article class="project-card card">
          <div class="project-top">
            <h3 class="h3">{project.name}</h3>

            <div class="project-actions-top">
              {#if project.badge}
                <span class={"badge " + (project.badgeVariant === "ghost" ? "ghost" : "")}>
                  {project.badge}
                </span>
              {/if}
              {#if isAdmin}
                <button
                  class="pen-button"
                  type="button"
                  aria-label={`Edit ${project.name}`}
                  on:click={() => openEdit(project)}
                >
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16">
                    <path d="M12 20h9" />
                    <path d="M16.5 3.5a2.1 2.1 0 0 1 3 3L8 18l-4 1 1-4 11.5-11.5z" />
                  </svg>
                </button>
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

          {#if project.links?.length}
            <div class="project-actions">
              {#each project.links as link}
                <a
                  class={"link-btn " + (link.variant === "ghost" ? "ghost" : "")}
                  href={link.href}
                  aria-label={link.ariaLabel ?? link.label}
                  target={link.href.startsWith("http") ? "_blank" : undefined}
                  rel={link.href.startsWith("http") ? "noreferrer" : undefined}
                >
                  {link.label}
                </a>
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
      aria-label="Scroll projects right"
    >
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <polyline points="9 18 15 12 9 6"></polyline>
      </svg>
    </button>
  </div>
</section>

{#if editOpen && editingId}
  <div class="modal-layer" role="presentation" on:click={closeEdit}>
    <div class="modal" role="dialog" aria-modal="true" on:click|stopPropagation>
      <div class="modal-head">
        <h3 class="h3" style="margin:0;">{isNewProject ? "New Project" : "Edit Project"}</h3>
        <button class="icon-x" type="button" aria-label="Close" on:click={closeEdit}>✕</button>
      </div>

      <div class="modal-body">
        <label class="label">
          Name
          <input
            class="input modal-input"
            type="text"
            bind:value={draftName}
            placeholder="Project name"
          />
        </label>

        <label class="label">
          Badge
          <input
            class="input modal-input"
            type="text"
            bind:value={draftBadge}
            placeholder="e.g., Full-stack"
          />
        </label>

        <label class="label">
          Description
          <textarea
            class="input modal-input textarea-input"
            rows="3"
            bind:value={draftDescription}
            placeholder="Project description"
          ></textarea>
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
        <button class="btn primary" type="button" on:click={saveEdit} disabled={saving}>
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

  .btn-new-project {
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

  .btn-new-project:hover {
    background: rgba(56, 197, 94, 1);
    transform: translateY(-1px);
  }

  .project-top {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    margin-bottom: 16px;
    gap: 12px;
    position: relative;
  }

  .project-actions-top {
    display: flex;
    align-items: center;
    gap: 12px;
    position: relative;
  }

  .pen-button {
    position: relative;
    width: 34px;
    height: 34px;
    border-radius: 10px;
    border: 1px solid rgba(0, 0, 0, 0.12);
    background: rgba(0, 0, 0, 0.05);
    color: rgba(0, 0, 0, 0.4);
    cursor: pointer;
    display: grid;
    place-items: center;
    transition: transform 0.15s ease, background 0.15s ease;
    padding: 0;
    font-size: 16px;
  }

  .pen-button:hover {
    transform: translateY(-1px);
    background: rgba(0, 0, 0, 0.08);
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

  .projects-carousel {
    display: flex;
    gap: 200px;
    overflow: hidden;
    scroll-behavior: smooth;
    scroll-snap-type: x mandatory;
    flex: 1;
    scrollbar-width: none;
    padding: 0 calc(50% - 300px);
  }

  .projects-carousel::-webkit-scrollbar {
    display: none;
  }

  .project-card {
    flex: 0 0 600px;
    max-width: 600px;
    scroll-snap-align: center;
    box-shadow: none;
  }

  @media (max-width: 980px) {
    .projects-carousel {
      padding: 0 calc(50% - 250px);
    }
    
    .project-card {
      flex: 0 0 500px;
      max-width: 500px;
    }
  }

  @media (max-width: 640px) {
    .projects-carousel {
      padding: 0 20px;
    }
    
    .project-card {
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
    min-height: 80px;
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
