<script lang="ts">
  import { onMount } from "svelte";
  import { isLoading, user, login, logout, isAuthenticated, isAdmin, getToken } from "$lib/stores/auth";
  import Projects from "$lib/components/Projects.svelte";
  import Hobbies from "$lib/components/Hobbies.svelte";
  import Reviews from "$lib/components/Reviews.svelte";
  import Skills from "$lib/components/Skills.svelte";
  import Education from "$lib/components/Education.svelte";

  // About sections from API
  type AboutSection = {
    id: string;
    title: string;
    type: "PARAGRAPH" | "BULLETS" | "TAGS";
    body: string | null;
    items: string[] | null;
  };

  let aboutSections: AboutSection[] = [];
  let aboutLoading = true;
  let aboutEditOpen = false;
  let editingSection: AboutSection | null = null;
  let editBody = "";
  let editItems = "";

  async function fetchAboutSections() {
    aboutLoading = true;
    try {
      const res = await fetch("/api/about");
      if (res.ok) aboutSections = await res.json();
    } catch (e) {
      console.error("Failed to fetch about sections:", e);
    } finally {
      aboutLoading = false;
    }
  }

  function openAboutEdit(section: AboutSection) {
    editingSection = section;
    editBody = section.body || "";
    editItems = (section.items || []).join(", ");
    aboutEditOpen = true;
    document.body.style.overflow = "hidden";
  }

  function closeAboutEdit() {
    aboutEditOpen = false;
    editingSection = null;
    document.body.style.overflow = "";
  }

  async function saveAboutEdit() {
    if (!editingSection) return;
    const token = await getToken();
    if (!token) return;

    const payload: any = {
      title: editingSection.title,
      type: editingSection.type
    };

    if (editingSection.type === "PARAGRAPH") {
      payload.body = editBody;
    } else {
      payload.items = editItems.split(",").map((s: string) => s.trim()).filter(Boolean);
    }

    try {
      const res = await fetch(`/api/about/${editingSection.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });
      if (res.ok) {
        await fetchAboutSections();
        closeAboutEdit();
      }
    } catch (e) {
      console.error("Failed to save about section:", e);
    }
  }

  // Helpers to find sections by type
  $: aboutParagraph = aboutSections.find(s => s.type === "PARAGRAPH");
  $: aboutBullets = aboutSections.find(s => s.type === "BULLETS");
  $: aboutTags = aboutSections.find(s => s.type === "TAGS");

  let showContent = typeof sessionStorage !== "undefined" && sessionStorage.getItem("portfolioRevealed") === "true";
  let rippleActive = false;
  let mobileMenuOpen = false;

  // --- Language toggle ---
  let currentLang: "en" | "fr" = "en";
  let translateReady = false;

  function handleReveal() {
    rippleActive = true;
    setTimeout(() => {
      showContent = true;
      sessionStorage.setItem("portfolioRevealed", "true");
      requestAnimationFrame(() => window.scrollTo({ top: 0, behavior: "instant" as any }));
    }, 600);
  }

  function closeMobileMenu() {
    mobileMenuOpen = false;
  }

  const signOut = () => {
    closeMobileMenu();
    logout();
  };

  // --- Google Translate ---
  function clearGoogTransCookies() {
    // Google Translate sets cookies on multiple domains/paths - clear them all
    const host = window.location.hostname;
    const expiry = "expires=Thu, 01 Jan 1970 00:00:00 UTC";
    document.cookie = `googtrans=; path=/; ${expiry}`;
    document.cookie = `googtrans=; path=/; domain=${host}; ${expiry}`;
    document.cookie = `googtrans=; path=/; domain=.${host}; ${expiry}`;
  }

  function initGoogleTranslate() {
    // Detect current language from cookie BEFORE initializing
    const match = document.cookie.match(/googtrans=\/en\/(\w+)/);
    if (match && match[1] === "fr") {
      currentLang = "fr";
    }

    // @ts-ignore
    new google.translate.TranslateElement(
      {
        pageLanguage: "en",
        includedLanguages: "en,fr",
        autoDisplay: false,
        layout: 0
      },
      "google_translate_element"
    );
    translateReady = true;
  }

  function toggleLanguage() {
    const target = currentLang === "en" ? "fr" : "en";

    if (target === "en") {
      // Switching back to English: clear all cookies and reload cleanly
      clearGoogTransCookies();
      currentLang = "en";
      window.location.reload();
    } else {
      // Switching to French: set cookie and reload
      clearGoogTransCookies();
      document.cookie = "googtrans=/en/fr; path=/";
      currentLang = "fr";
      window.location.reload();
    }
  }

  // --- CV viewer ---
  let cvLang: "en" | "fr" = "en";
  let cvKey = 0; // bump to force iframe reload after upload
  $: cvUrl = `/api/public/cv?lang=${cvLang}&_=${cvKey}`;
  let cvUploading = false;
  let cvUploadStatus: "idle" | "success" | "error" = "idle";

  async function handleCvUpload(e: Event) {
    const input = e.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    cvUploading = true;
    cvUploadStatus = "idle";

    try {
      const token = await getToken();
      const formData = new FormData();
      formData.append("file", file);
      formData.append("lang", cvLang);

      const res = await fetch("/api/cv/upload", {
        method: "POST",
        headers: token ? { Authorization: `Bearer ${token}` } : {},
        body: formData
      });

      if (!res.ok) throw new Error("Upload failed");
      cvUploadStatus = "success";
      cvKey++; // force iframe to reload with new PDF
      // Reset after a few seconds
      setTimeout(() => (cvUploadStatus = "idle"), 3000);
    } catch {
      cvUploadStatus = "error";
    } finally {
      cvUploading = false;
      input.value = ""; // reset file input
    }
  }

  // --- Contact form ---
  let contactName = "";
  let contactEmail = "";
  let contactMessage = "";
  let contactSending = false;
  let contactStatus: "idle" | "success" | "error" = "idle";

  async function handleContactSubmit(e: Event) {
    e.preventDefault();
    contactSending = true;
    contactStatus = "idle";

    try {
      const res = await fetch("/api/public/contact", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: contactName,
          email: contactEmail,
          message: contactMessage
        })
      });

      if (!res.ok) throw new Error("Failed to send");
      contactStatus = "success";
      contactName = "";
      contactEmail = "";
      contactMessage = "";
    } catch {
      contactStatus = "error";
    } finally {
      contactSending = false;
    }
  }

  onMount(() => {
    // Fetch about sections from API
    fetchAboutSections();

    // Load Google Translate script
    // @ts-ignore
    window.googleTranslateElementInit = initGoogleTranslate;
    const script = document.createElement("script");
    script.src = "//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit";
    script.async = true;
    document.head.appendChild(script);

    let cleanup: (() => void) | undefined;

    const setup = () => {
      const sections = Array.from(document.querySelectorAll<HTMLElement>(".snap-section"));
      if (!sections.length) return;

      const links = Array.from(document.querySelectorAll<HTMLAnchorElement>(".nav-link"));

      const setActive = (id: string) => {
        links.forEach((a) => a.classList.remove("active"));
        const active = links.find((a) => a.getAttribute("href") === `#${id}`);
        active?.classList.add("active");
      };

      setActive(sections[0].id);

      const io = new IntersectionObserver(
        (entries) => {
          const visible = entries
            .filter((e) => e.isIntersecting)
            .sort((a, b) => (b.intersectionRatio ?? 0) - (a.intersectionRatio ?? 0))[0];

          if (!visible) return;
          setActive((visible.target as HTMLElement).id);
        },
        { threshold: [0.5, 0.6, 0.7] }
      );

      sections.forEach((s) => io.observe(s));
      return () => io.disconnect();
    };

    const t = setInterval(() => {
      if (showContent && !cleanup) {
        cleanup = setup();
        clearInterval(t);
      }
    }, 50);

    return () => {
      clearInterval(t);
      cleanup?.();
    };
  });
</script>

<!-- Hidden Google Translate container -->
<div id="google_translate_element" style="display:none;"></div>

<!-- Background -->
<div class="leaf-bg" aria-hidden="true">
  <span class="leaf l1"></span>
  <span class="leaf l2"></span>
  <span class="leaf l3"></span>
  <span class="leaf l4"></span>
  <span class="leaf l5"></span>
  <span class="leaf l6"></span>
  <span class="leaf l7"></span>
  <span class="leaf l8"></span>
  <span class="leaf l9"></span>
  <span class="leaf l10"></span>
  <span class="leaf l11"></span>
  <span class="leaf l12"></span>
</div>

{#if !showContent}
  <!-- Title screen -->
  <section class="title-card" class:ripple-active={rippleActive}>
    <h1 class="name">
      <span>Vlad</span>
      <span>Loghin</span>
    </h1>

    <p class="subtitle">Portfolio</p>

    <button
      class="arrow-btn"
      on:click={handleReveal}
      aria-label="View portfolio"
      disabled={rippleActive}
    >
      <svg class="arrow-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
        <path
          d="M12 5v14M19 12l-7 7-7-7"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>

      {#if rippleActive}
        <span class="ripple"></span>
      {/if}
    </button>
  </section>
{:else}
  <div class="content-reveal">
    <!-- Header -->
    <header class="site-header">
      <nav class="nav-shell">
        <a class="brand" href="#about" on:click={closeMobileMenu}>
          <span class="brand-dot"></span>
          <span class="brand-text">Vlad Loghin</span>
        </a>

        <!-- Desktop Navigation -->
        <div class="nav-links-desktop">
          <a class="nav-link active" href="#about">About</a>
          <a class="nav-link" href="#education">Education</a>
          <a class="nav-link" href="#projects">Projects</a>
          <a class="nav-link" href="#skills">Skills</a>
          <a class="nav-link" href="#resume">Resume</a>
          <a class="nav-link" href="#hobbies">Hobbies</a>
          <a class="nav-link" href="#reviews">Reviews</a>
          <a class="nav-link" href="#contact">Contact</a>
          {#if $isAdmin}
            <span class="admin-badge">Admin</span>
          {/if}
        </div>

        <!-- Language Toggle -->
        <button class="lang-toggle" on:click={toggleLanguage} aria-label="Toggle language">
          {currentLang === "en" ? "FR" : "EN"}
        </button>

        <!-- Mobile Hamburger -->
        <button
          class="hamburger"
          class:active={mobileMenuOpen}
          on:click={() => (mobileMenuOpen = !mobileMenuOpen)}
          aria-label="Toggle menu"
          aria-expanded={mobileMenuOpen}
        >
          <span></span>
          <span></span>
          <span></span>
        </button>

        <!-- Desktop Actions -->
        <div class="nav-actions-desktop">
          {#if $isLoading}
            <button class="nav-btn ghost" disabled>Loading...</button>
          {:else if $isAuthenticated}
            <button class="nav-btn ghost" on:click={signOut}>Log out</button>
          {:else}
            <button class="nav-btn ghost" on:click={login}>Log in</button>
            <button class="nav-btn primary" on:click={login}>Sign up</button>
          {/if}
        </div>
      </nav>

      <!-- Mobile Menu -->
      {#if mobileMenuOpen}
        <div class="mobile-menu" on:click={closeMobileMenu}>
          <div class="mobile-menu-content" on:click|stopPropagation>
            <a class="mobile-link" href="#about" on:click={closeMobileMenu}>About</a>
            <a class="mobile-link" href="#education" on:click={closeMobileMenu}>Education</a>
            <a class="mobile-link" href="#projects" on:click={closeMobileMenu}>Projects</a>
            <a class="mobile-link" href="#skills" on:click={closeMobileMenu}>Skills</a>
            <a class="mobile-link" href="#resume" on:click={closeMobileMenu}>Resume</a>
            <a class="mobile-link" href="#hobbies" on:click={closeMobileMenu}>Hobbies</a>
            <a class="mobile-link" href="#reviews" on:click={closeMobileMenu}>Reviews</a>
            <a class="mobile-link" href="#contact" on:click={closeMobileMenu}>Contact</a>
            {#if $isAdmin}
              <span class="mobile-admin-badge">Admin</span>
            {/if}

            <!-- Mobile Language Toggle -->
            <button class="lang-toggle mobile-lang" on:click={toggleLanguage}>
              {currentLang === "en" ? "Switch to French" : "Switch to English"}
            </button>

            <div class="mobile-actions">
              {#if $isLoading}
                <button class="nav-btn ghost" disabled>Loading...</button>
              {:else if $isAuthenticated}
                <button class="nav-btn ghost" on:click={signOut}>Log out</button>
              {:else}
                <button class="nav-btn ghost" on:click={() => { login(); closeMobileMenu(); }}>Log in</button>
                <button class="nav-btn primary" on:click={() => { login(); closeMobileMenu(); }}>Sign up</button>
              {/if}
            </div>
          </div>
        </div>
      {/if}
    </header>

    <!-- Page -->
    <main class="page snap-container">
      <!-- ABOUT -->
      <section id="about" class="section shell snap-section about-hero">
        <div class="about-container">
          <div class="about-content">
            <div class="section-head">
              <div class="section-pill">
                <h2 class="h2">About</h2>
              </div>

              <p class="lead">
                {aboutParagraph ? aboutParagraph.body : "I'm Vlad Loghin — a software developer who builds clean, reliable web apps with thoughtful UX. I like shipping features fast, then tightening the engineering until it's solid."}
              </p>
              {#if $isAdmin && aboutParagraph}
                <button class="about-edit-btn" type="button" on:click={() => openAboutEdit(aboutParagraph)}>Edit</button>
              {/if}
            </div>

            <div class="about-grid">
              <article class="card">
                <h3 class="h3">{aboutBullets ? aboutBullets.title : "What I do"}</h3>
                <ul class="list">
                  {#each (aboutBullets?.items || ["Java / Spring Boot APIs", "Svelte & React frontends", "Docker & CI pipelines"]) as item}
                    <li>{item}</li>
                  {/each}
                </ul>
                {#if $isAdmin && aboutBullets}
                  <button class="about-edit-btn" type="button" on:click={() => openAboutEdit(aboutBullets)}>Edit</button>
                {/if}
              </article>

              <article class="card">
                <h3 class="h3">{aboutTags ? aboutTags.title : "What I care about"}</h3>
                <div class="pill-row">
                  {#each (aboutTags?.items || ["Good Software", "Clean Code", "New Challenges", "Continuous Learning", "Creativity"]) as item}
                    <span class="pill">{item}</span>
                  {/each}
                </div>
                {#if $isAdmin && aboutTags}
                  <button class="about-edit-btn" type="button" on:click={() => openAboutEdit(aboutTags)}>Edit</button>
                {/if}
              </article>
            </div>
          </div>

          <div class="about-image">
            <div class="image-placeholder">
              <img src="/profile.jpg" alt="Vlad Loghin" />
            </div>
          </div>
        </div>
      </section>

      <!-- About Edit Modal -->
      <!-- svelte-ignore a11y_click_events_have_key_events a11y_no_static_element_interactions -->
      {#if aboutEditOpen && editingSection}
        <div class="modal-layer" on:click={closeAboutEdit}>
          <div class="modal" role="dialog" aria-modal="true" on:click|stopPropagation>
            <div class="modal-head">
              <h3 class="h3" style="margin:0;">Edit: {editingSection.title}</h3>
              <button class="icon-x" type="button" aria-label="Close" on:click={closeAboutEdit}>&#10005;</button>
            </div>
            <div class="modal-body">
              {#if editingSection.type === "PARAGRAPH"}
                <label class="label">
                  Content
                  <textarea class="input modal-input textarea" rows="5" bind:value={editBody}></textarea>
                </label>
              {:else}
                <label class="label">
                  Items (comma separated)
                  <textarea class="input modal-input textarea" rows="3" bind:value={editItems}></textarea>
                </label>
              {/if}
            </div>
            <div class="modal-actions">
              <button class="btn ghost" type="button" on:click={closeAboutEdit}>Cancel</button>
              <button class="btn primary" type="button" on:click={saveAboutEdit}>Save</button>
            </div>
          </div>
        </div>
      {/if}

      <!-- EDUCATION -->
      <Education />

      <!-- PROJECTS -->
      <Projects />

      <!-- SKILLS -->
      <Skills />

      <!-- RESUME / CV -->
      <section id="resume" class="section shell snap-section">
        <div class="section-head">
          <div class="section-pill">
            <h2 class="h2">Resume</h2>
          </div>
          <p class="lead">
            View or download my CV in English or French.
          </p>
        </div>

        <div class="cv-toggle-row">
          <button
            class="cv-tab"
            class:active={cvLang === "en"}
            on:click={() => (cvLang = "en")}
          >
            English
          </button>
          <button
            class="cv-tab"
            class:active={cvLang === "fr"}
            on:click={() => (cvLang = "fr")}
          >
            French
          </button>

          {#if $isAdmin}
            <label class="cv-upload-btn">
              {cvUploading ? "Uploading..." : "Upload new PDF"}
              <input
                type="file"
                accept="application/pdf"
                on:change={handleCvUpload}
                disabled={cvUploading}
                hidden
              />
            </label>
          {/if}
        </div>

        {#if cvUploadStatus === "success"}
          <p class="cv-upload-msg success">CV uploaded successfully!</p>
        {/if}
        {#if cvUploadStatus === "error"}
          <p class="cv-upload-msg error">Upload failed. Please try again.</p>
        {/if}

        <div class="cv-viewer card">
          <iframe
            title="CV - {cvLang === 'en' ? 'English' : 'French'}"
            src={cvUrl}
            width="100%"
            height="800"
            style="border: none; border-radius: 12px;"
          ></iframe>
        </div>
      </section>

      <!-- HOBBIES -->
      <Hobbies />

      <!-- REVIEWS -->
      <Reviews />

      <!-- CONTACT -->
      <section id="contact" class="section shell snap-section">
        <div class="section-head">
          <div class="section-pill">
            <h2 class="h2">Contact</h2>
          </div>
          <p class="lead">
            Got a project in mind or just want to chat? Reach out—I'm always up for interesting conversations.
          </p>
        </div>

        <div class="contact-container">
          <article class="card contact-form-card">
            <h3 class="h3">Send me a message</h3>
            <form class="contact-form" on:submit={handleContactSubmit}>
              <div class="form-group">
                <input class="input" type="text" placeholder="Your name" required bind:value={contactName} />
              </div>
              <div class="form-group">
                <input class="input" type="email" placeholder="Your email" required bind:value={contactEmail} />
              </div>
              <div class="form-group">
                <textarea class="input textarea" placeholder="Tell me about your project..." rows="5" bind:value={contactMessage}></textarea>
              </div>
              <button class="nav-btn primary" type="submit" disabled={contactSending}>
                {contactSending ? "Sending..." : "Send message"}
              </button>

              {#if contactStatus === "success"}
                <p class="contact-success">Message sent! I'll get back to you soon.</p>
              {/if}
              {#if contactStatus === "error"}
                <p class="contact-error">Something went wrong. Please try again or email me directly.</p>
              {/if}
            </form>
          </article>

          <article class="card contact-info-card">
            <h3 class="h3">Let's connect</h3>
            <div class="contact-links">
              <a class="contact-link" href="https://github.com/VladLoghin" target="_blank" rel="noreferrer">
                <span class="contact-icon">→</span>
                <div>
                  <p class="p contact-link-title">GitHub</p>
                  <p class="p small muted">VladLoghin</p>
                </div>
              </a>
              <a class="contact-link" href="https://www.linkedin.com/in/vlad-loghin-b65a44199/" target="_blank" rel="noreferrer">
                <span class="contact-icon">→</span>
                <div>
                  <p class="p contact-link-title">LinkedIn</p>
                  <p class="p small muted">Vlad Loghin</p>
                </div>
              </a>
              <a class="contact-link" href="mailto:blade.loghin@gmail.com">
                <span class="contact-icon">→</span>
                <div>
                  <p class="p contact-link-title">Email</p>
                  <p class="p small muted">blade.loghin@gmail.com</p>
                </div>
              </a>
            </div>
          </article>
        </div>
      </section>

      <footer class="footer shell">
        <p class="p small muted">&copy; {new Date().getFullYear()} Vlad Loghin</p>
      </footer>
    </main>
  </div>
{/if}

<style>
  :global(.ripple-active) {
    position: relative;
    overflow: hidden;
  }

  /* Hide Google Translate bar that appears at top */
  :global(.skiptranslate) {
    display: none !important;
  }
  :global(body) {
    top: 0 !important;
  }

  /* --- snap (optional) --- */
  .snap-container {
    scroll-behavior: smooth;
  }

  .snap-container {
    scroll-snap-type: y proximity;
  }

  .snap-section {
    scroll-snap-align: start;
    scroll-margin-top: 120px;
  }

  /* --- LANGUAGE TOGGLE --- */
  .lang-toggle {
    padding: 6px 14px;
    border-radius: 999px;
    border: 1px solid var(--border);
    background: rgba(56, 197, 94, 0.1);
    color: var(--green-d);
    font-weight: 700;
    font-size: 0.85rem;
    cursor: pointer;
    transition: all 0.25s ease;
    white-space: nowrap;
  }

  .lang-toggle:hover {
    background: rgba(56, 197, 94, 0.2);
    transform: translateY(-1px);
  }

  .mobile-lang {
    width: 100%;
    padding: 12px 16px;
    font-size: 0.95rem;
    border-radius: 8px;
    margin-top: 8px;
  }

  /* --- MOBILE MENU --- */
  .nav-links-desktop,
  .nav-actions-desktop {
    display: flex;
    align-items: center;
    gap: 24px;
  }

  .hamburger {
    display: none;
    flex-direction: column;
    gap: 5px;
    background: none;
    border: none;
    cursor: pointer;
    padding: 8px;
  }

  .hamburger span {
    width: 24px;
    height: 2.5px;
    background: currentColor;
    border-radius: 2px;
    transition: all 0.3s ease;
  }

  .hamburger.active span:nth-child(1) {
    transform: rotate(45deg) translate(10px, 10px);
  }

  .hamburger.active span:nth-child(2) {
    opacity: 0;
  }

  .hamburger.active span:nth-child(3) {
    transform: rotate(-45deg) translate(7px, -7px);
  }

  .mobile-menu {
    display: none;
    position: fixed;
    top: 80px;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 99;
    animation: fadeIn 0.2s ease;
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }

  .mobile-menu-content {
    background: white;
    position: absolute;
    top: 0;
    right: 0;
    width: 100%;
    max-width: 300px;
    height: 100vh;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    padding: 24px;
    gap: 16px;
    box-shadow: -4px 0 12px rgba(0, 0, 0, 0.15);
    animation: slideIn 0.3s ease;
  }

  @keyframes slideIn {
    from {
      transform: translateX(100%);
    }
    to {
      transform: translateX(0);
    }
  }

  .mobile-link {
    padding: 12px 16px;
    text-decoration: none;
    color: #333;
    font-weight: 500;
    border-radius: 6px;
    transition: all 0.2s ease;
  }

  .mobile-link:hover {
    background: rgba(56, 197, 94, 0.1);
    color: var(--green);
  }

  .mobile-admin-badge {
    display: inline-block;
    padding: 6px 14px;
    background: #ff9800;
    color: white;
    font-size: 12px;
    font-weight: 700;
    border-radius: 6px;
    text-transform: uppercase;
    align-self: flex-start;
    margin-left: 16px;
  }

  .mobile-actions {
    display: flex;
    flex-direction: column;
    gap: 12px;
    margin-top: 24px;
    border-top: 1px solid #e0e0e0;
    padding-top: 24px;
  }

  .mobile-actions .nav-btn {
    width: 100%;
  }

  @media (max-width: 768px) {
    .nav-links-desktop,
    .nav-actions-desktop {
      display: none;
    }

    .hamburger {
      display: flex;
    }

    .mobile-menu {
      display: block;
    }

    .snap-section {
      scroll-margin-top: 80px;
    }

  }

  /* ---------- EXISTING GRID STYLES ---------- */
  .projects-grid,
  .skills-grid,
  .hobbies-grid,
  .reviews-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 18px;
  }

  @media (max-width: 980px) {
    .projects-grid,
    .skills-grid,
    .hobbies-grid,
    .reviews-grid {
      grid-template-columns: 1fr;
    }
  }

  .project-card,
  .skill-card,
  .hobby-card,
  .review-card {
    transition: transform 0.25s ease, box-shadow 0.25s ease;
  }

  .project-card:hover,
  .skill-card:hover,
  .hobby-card:hover,
  .review-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 24px 70px rgba(0, 0, 0, 0.2);
  }

  .badge {
    font-size: 12px;
    padding: 6px 10px;
    border-radius: 999px;
    border: 1px solid rgba(207, 196, 173, 0.9);
    background: rgba(244, 241, 234, 0.75);
    color: rgba(23, 58, 37, 0.75);
    white-space: nowrap;
  }

  .badge.ghost {
    background: transparent;
  }

  .review-top {
    display: grid;
    grid-template-columns: 44px 1fr;
    gap: 12px;
    align-items: start;
    margin-bottom: 10px;
  }

  .review-avatar {
    width: 44px;
    height: 44px;
    border-radius: 999px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 900;
    color: #0e2f1a;
    background: radial-gradient(circle at 30% 30%, #7ff0a2, var(--green));
    box-shadow: 0 0 0 5px rgba(56, 197, 94, 0.16);
  }

  .review-name {
    font-weight: 800;
    margin: 0;
  }

  .stars {
    display: inline-flex;
    gap: 6px;
    margin-top: 10px;
    font-size: 14px;
    user-select: none;
  }

  .star {
    color: rgba(23, 58, 37, 0.25);
  }

  .star.filled {
    color: var(--green-d);
  }

  .contact-container {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 24px;
    margin-top: 32px;
  }

  @media (max-width: 768px) {
    .contact-container {
      grid-template-columns: 1fr;
    }
  }

  .contact-form-card,
  .contact-info-card {
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }

  .contact-form {
    display: flex;
    flex-direction: column;
    gap: 16px;
    margin-top: 16px;
    width: 100%;
  }

  .form-group {
    display: flex;
    flex-direction: column;
    width: 100%;
  }

  .input {
    padding: 12px 16px;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    font-family: inherit;
    font-size: 14px;
    background: white;
    transition: border-color 0.2s ease, box-shadow 0.2s ease;
    width: 100%;
    box-sizing: border-box;
  }

  .input:focus {
    outline: none;
    border-color: var(--green);
    box-shadow: 0 0 0 3px rgba(56, 197, 94, 0.1);
  }

  .textarea {
    resize: vertical;
    min-height: 120px;
  }

  .contact-links {
    display: flex;
    flex-direction: column;
    gap: 16px;
    margin-top: 16px;
  }

  .contact-link {
    display: flex;
    align-items: flex-start;
    gap: 12px;
    padding: 16px;
    border-radius: 8px;
    background: rgba(56, 197, 94, 0.05);
    text-decoration: none;
    color: inherit;
    transition: all 0.2s ease;
  }

  .contact-link:hover {
    background: rgba(56, 197, 94, 0.1);
    transform: translateX(4px);
  }

  .contact-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 24px;
    height: 24px;
    color: var(--green);
    font-weight: bold;
    flex-shrink: 0;
  }

  .contact-link-title {
    font-weight: 600;
    margin: 0 0 4px 0;
  }

  .contact-success {
    color: var(--green-d);
    font-weight: 600;
    margin: 0;
    padding: 12px;
    background: rgba(56, 197, 94, 0.1);
    border-radius: 8px;
    border: 1px solid rgba(56, 197, 94, 0.25);
  }

  .contact-error {
    color: #d32f2f;
    font-weight: 600;
    margin: 0;
    padding: 12px;
    background: rgba(211, 47, 47, 0.08);
    border-radius: 8px;
    border: 1px solid rgba(211, 47, 47, 0.25);
  }

  .nav-btn {
    padding: 12px 24px;
    border: none;
    border-radius: 8px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s ease;
  }

  .nav-btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }

  .nav-btn.primary {
    background: var(--green);
    color: white;
  }

  .nav-btn.primary:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(56, 197, 94, 0.3);
  }

  /* --- About edit button --- */
  .about-edit-btn {
    margin-top: 10px;
    padding: 6px 14px;
    border-radius: 6px;
    border: 1px solid rgba(56, 197, 94, 0.3);
    background: rgba(56, 197, 94, 0.1);
    color: var(--green-d);
    font-weight: 600;
    font-size: 12px;
    cursor: pointer;
    transition: all 0.2s ease;
  }

  .about-edit-btn:hover {
    background: rgba(56, 197, 94, 0.2);
    transform: translateY(-1px);
  }

  /* --- About modal styles --- */
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
  .btn.ghost { background: transparent; }
  .btn.primary { border-color: rgba(56,197,94,0.35); background: rgba(56,197,94,0.16); color: var(--green); }
  .btn.primary:hover:not(:disabled) { background: rgba(56,197,94,0.25); }

  /* --- Admin badge (no link) --- */
  .admin-badge {
    display: inline-block;
    margin-left: 8px;
    padding: 4px 10px;
    background: #ff9800;
    color: white;
    font-size: 11px;
    font-weight: 700;
    border-radius: 4px;
    text-transform: uppercase;
  }

  /* --- CV Viewer --- */
  .cv-toggle-row {
    display: flex;
    gap: 8px;
    margin-bottom: 16px;
  }

  .cv-tab {
    padding: 10px 24px;
    border: 1px solid var(--border);
    border-radius: 999px;
    background: transparent;
    color: var(--muted);
    font-weight: 600;
    cursor: pointer;
    transition: all 0.25s ease;
  }

  .cv-tab.active {
    background: var(--green);
    color: white;
    border-color: var(--green);
  }

  .cv-tab:hover:not(.active) {
    background: rgba(56, 197, 94, 0.1);
    color: var(--green-d);
  }

  .cv-upload-btn {
    padding: 10px 24px;
    border: 1px dashed var(--green);
    border-radius: 999px;
    background: rgba(56, 197, 94, 0.08);
    color: var(--green-d);
    font-weight: 600;
    cursor: pointer;
    transition: all 0.25s ease;
    margin-left: auto;
  }

  .cv-upload-btn:hover {
    background: rgba(56, 197, 94, 0.18);
    transform: translateY(-1px);
  }

  .cv-upload-msg {
    margin: 0 0 12px;
    padding: 10px 16px;
    border-radius: 8px;
    font-weight: 600;
    font-size: 0.9rem;
  }

  .cv-upload-msg.success {
    color: var(--green-d);
    background: rgba(56, 197, 94, 0.1);
    border: 1px solid rgba(56, 197, 94, 0.25);
  }

  .cv-upload-msg.error {
    color: #d32f2f;
    background: rgba(211, 47, 47, 0.08);
    border: 1px solid rgba(211, 47, 47, 0.25);
  }

  .cv-viewer {
    padding: 8px;
    overflow: hidden;
  }

  .cv-viewer iframe {
    display: block;
    width: 100%;
    min-height: 600px;
  }

  @media (max-width: 768px) {
    .cv-viewer iframe {
      min-height: 400px;
    }
  }
</style>
