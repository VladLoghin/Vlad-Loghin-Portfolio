<script lang="ts">
  export type Skill = {
    id: string;
    category: string;
    items: string[];
  };

  export let title: string = "Skills";
  export let subtitle: string =
    "Technologies and practices I use to build solid, maintainable software.";
  export let skills: Skill[] = [];
  export let isAdmin: boolean = false;

  export let onSave: ((next: Skill[]) => Promise<void> | void) | undefined;

  let editOpen = false;
  let saving = false;
  let error = "";

  let editingId: string | null = null;
  let draftCategory = "";
  let draftItems: string[] = [];

  function openEdit(skill: Skill) {
    editingId = skill.id;
    draftCategory = skill.category;
    draftItems = [...skill.items];
    error = "";
    editOpen = true;
  }

  function closeEdit() {
    editOpen = false;
    editingId = null;
    error = "";
  }

  function addItem() {
    draftItems = [...draftItems, ""];
  }

  function updateItem(i: number, value: string) {
    const next = draftItems.slice();
    next[i] = value;
    draftItems = next;
  }

  function removeItem(i: number) {
    const next = draftItems.slice();
    next.splice(i, 1);
    draftItems = next;
  }

  function createNewSkill() {
    const newId = `skill-${Date.now()}`;
    editingId = newId;
    draftCategory = "";
    draftItems = [];
    error = "";
    editOpen = true;
    isNewSkill = true;
  }

  let isNewSkill = false;

  async function saveEdit() {
    if (!editingId) return;

    const cleaned = {
      id: editingId,
      category: draftCategory.trim(),
      items: draftItems.map((x) => x.trim()).filter(Boolean)
    };

    let nextSkills = skills;
    if (isNewSkill) {
      nextSkills = [...skills, cleaned];
      isNewSkill = false;
    } else {
      nextSkills = skills.map((s) => (s.id === editingId ? cleaned : s));
    }

    saving = true;
    error = "";
    try {
      await onSave?.(nextSkills);
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

<section id="skills" class="section shell snap-section">
  <div class="section-head">
    <div class="section-pill">
      <h2 class="h2">{title}</h2>
    </div>
    <p class="lead">{subtitle}</p>
    {#if isAdmin}
      <button class="btn-new-skill" type="button" on:click={createNewSkill}>
        + New Skill
      </button>
    {/if}
  </div>

  <div class="skills-grid">
    {#each skills as skill (skill.id)}
      <article class="card skill-card">
        {#if isAdmin}
          <button
            class="pen-btn"
            type="button"
            aria-label={"Edit " + skill.category}
            on:click={() => openEdit(skill)}
          >
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
      </article>
    {/each}
  </div>

  {#if editOpen}
    <div class="modal-backdrop" role="presentation" on:click={closeEdit}>
      <div class="modal" role="dialog" aria-modal="true" on:click|stopPropagation>
        <div class="modal-head">
          <h3 class="h3" style="margin:0;">Edit skills</h3>
          <button class="icon-x" type="button" aria-label="Close" on:click={closeEdit}>✕</button>
        </div>

        <div class="modal-body">
          <label class="label">
            Category
            <input
              class="input modal-input"
              value={draftCategory}
              on:input={(e) => (draftCategory = (e.currentTarget as HTMLInputElement).value)}
              placeholder="e.g. Backend"
            />
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
                  on:input={(e) => updateItem(i, (e.currentTarget as HTMLInputElement).value)}
                  placeholder="e.g. Spring Boot"
                />
                <button
                  class="icon-btn remove-btn"
                  type="button"
                  aria-label="Remove item"
                  on:click={() => removeItem(i)}
                >
                  −
                </button>
              </div>
            {/each}
          </div>

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
</section>

<style>
  .skills-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 18px;
    margin-top: 32px;
  }
  @media (max-width: 980px) {
    .skills-grid { grid-template-columns: repeat(2, 1fr); }
  }
  @media (max-width: 640px) {
    .skills-grid { grid-template-columns: 1fr; }
  }

  .skill-card {
    position: relative;
    transition: transform 0.25s ease, box-shadow 0.25s ease;
  }
  .skill-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 24px 70px rgba(0, 0, 0, 0.2);
  }

  .skill-items {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }
  .skill-badge {
    display: inline-block;
    padding: 6px 12px;
    background: rgba(56, 197, 94, 0.1);
    color: var(--green-d);
    border-radius: 6px;
    font-size: 13px;
    font-weight: 600;
    border: 1px solid rgba(56, 197, 94, 0.2);
    transition: all 0.2s ease;
  }

  .pen-btn {
    position: absolute;
    top: 12px;
    right: 12px;
    width: 34px;
    height: 34px;
    border-radius: 10px;
    border: 1px solid rgba(0, 0, 0, 0.12);
    background: rgba(0, 0, 0, 0.05);
    color: rgba(0, 0, 0, 0.55);
    cursor: pointer;
    display: grid;
    place-items: center;
    transition: transform 0.15s ease, background 0.15s ease;
  }
  .pen-btn:hover {
    transform: translateY(-1px);
    background: rgba(0, 0, 0, 0.08);
  }
  .pen-btn svg {
    width: 16px;
    height: 16px;
  }

  .modal-backdrop {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.55);
    display: grid;
    place-items: center;
    padding: 18px;
    z-index: 999;
  }
  .modal {
    width: min(640px, 100%);
    background: white;
    border-radius: 14px;
    box-shadow: 0 30px 120px rgba(0, 0, 0, 0.35);
    overflow: hidden;
  }
  .modal-head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 18px;
    border-bottom: 1px solid #eee;
  }
  .icon-x {
    border: none;
    background: transparent;
    font-size: 18px;
    cursor: pointer;
    opacity: 0.7;
  }
  .icon-x:hover { opacity: 1; }

  .modal-body {
    padding: 18px;
    display: grid;
    gap: 18px;
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
  }

  .items-block {
    display: grid;
    gap: 14px;
    padding-top: 4px;
  }
  .items-head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 10px;
  }

  /* ✅ FIX HERE */
  .item-row {
    position: relative;
  }

  .item-input {
    padding-right: 52px; /* reserves room so text never goes under minus */
    box-sizing: border-box;
  }

  .icon-btn {
    width: 32px;
    height: 32px;
    border-radius: 6px;
    border: 1px solid rgba(0,0,0,0.12);
    background: rgba(0,0,0,0.04);
    cursor: pointer;
    font-weight: 900;
    font-size: 12px;
    display: grid;
    place-items: center;
    padding: 0;
  }

  .remove-btn {
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
  }

  .modal-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding: 14px 18px;
    border-top: 1px solid #eee;
    background: #fafafa;
  }

  .btn {
    padding: 10px 14px;
    border-radius: 10px;
    border: 1px solid rgba(0,0,0,0.12);
    background: rgba(0,0,0,0.04);
    cursor: pointer;
    font-weight: 800;
  }
  .btn.small { padding: 8px 10px; font-size: 13px; }
  .btn.ghost { background: transparent; }
  .btn.primary {
    border-color: rgba(56, 197, 94, 0.35);
    background: rgba(56, 197, 94, 0.16);
  }

  .section-head {
    position: relative;
  }

  .btn-new-skill {
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

  .btn-new-skill:hover {
    background: rgba(56, 197, 94, 1);
    transform: translateY(-1px);
  }
</style>
