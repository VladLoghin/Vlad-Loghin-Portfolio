<!-- src/App.svelte -->
<script lang="ts">
  import { onMount } from "svelte";
  import {
    isLoading,
    user,
    login,
    logout,
    isAuthenticated,
    initializeAuth
  } from "../lib/stores/auth";

  let showContent = false;
  let rippleActive = false;

  onMount(() => {
    initializeAuth();
  });

  function handleReveal() {
    rippleActive = true;
    setTimeout(() => {
      showContent = true;
    }, 600);
  }

  const signOut = () => logout();
</script>

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
    <header class="site-header">
      <nav class="nav-shell">
        <a class="brand" href="#about">
          <span class="brand-dot"></span>
          <span class="brand-text">Vlad Loghin</span>
        </a>

        <div class="nav-links">
          <a class="nav-link active" href="#about">About</a>
          <a class="nav-link" href="#skills">Skills</a>
          <a class="nav-link" href="#work">Work</a>
          <a class="nav-link" href="#experience">Experience</a>
          <a class="nav-link" href="#contact">Contact</a>
        </div>

        <div class="nav-actions">
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
    </header>

    <main class="page">
      <!-- ABOUT -->
      <section id="about" class="section stagger-1 about-hero full-bleed">
        <div class="about-container">
          <div class="about-content">
            <div class="section-head">
              <div class="section-pill">
                <h2 class="h2">About</h2>
              </div>
              <p class="lead">
                I'm Vlad Loghin — a software developer who builds clean, reliable web apps with thoughtful UX.
                I like shipping features fast, then tightening the engineering until it's solid.
              </p>

              {#if $isAuthenticated}
                <p class="p small muted" style="margin-top: 10px;">
                  Signed in as: {($user?.name ?? $user?.email) || "user"}
                </p>
              {/if}
            </div>

            <div class="about-grid">
              <article class="card">
                <h3 class="h3">What I do</h3>
                <p class="p">
                  Full-stack development with a backend-first mindset. I enjoy designing APIs, building robust
                  data flows, and polishing front-end interactions so everything feels smooth.
                </p>
                <ul class="list">
                  <li>Java / Spring Boot APIs (security, testing, CI)</li>
                  <li>React front-ends (clean state, reusable components)</li>
                  <li>Docker + environment setups for dev/CI</li>
                </ul>
              </article>

              <article class="card">
                <h3 class="h3">What I care about</h3>
                <p class="p">
                  I'm big on clarity: readable code, predictable architecture, and design systems that don't
                  fight you. I'd rather be simple and correct than flashy and fragile.
                </p>
                <div class="pill-row">
                  <span class="pill">Team-oriented</span>
                  <span class="pill">Relentless</span>
                  <span class="pill">Compassionate</span>
                  <span class="pill">Ship-focused</span>
                </div>
              </article>
            </div>
          </div>

          <div class="about-image">
            <div class="image-placeholder">
              <img src="/your-photo.jpg" alt="Vlad Loghin" />
            </div>
          </div>
        </div>
      </section>

      <!-- SKILLS -->
      <section id="skills" class="section shell stagger-2">
        <div class="section-head">
          <h2 class="h2">Skills</h2>
          <p class="lead">
            A practical stack aimed at shipping production features: modern backend, modern front-end,
            and sensible tooling.
          </p>
        </div>

        <div class="grid-3">
          <article class="card">
            <h3 class="h3">Backend</h3>
            <div class="pill-row">
              <span class="pill">Java 17+</span>
              <span class="pill">Spring Boot</span>
              <span class="pill">REST APIs</span>
              <span class="pill">Spring Security</span>
              <span class="pill">Testing (JUnit)</span>
            </div>
            <p class="p small">
              I focus on clean boundaries, consistent DTOs, and tests that actually protect behavior.
            </p>
          </article>

          <article class="card">
            <h3 class="h3">Frontend</h3>
            <div class="pill-row">
              <span class="pill">Svelte</span>
              <span class="pill">TypeScript</span>
              <span class="pill">Vite</span>
              <span class="pill">Routing</span>
              <span class="pill">UI polish</span>
            </div>
            <p class="p small">
              I aim for readable components, good empty/loading states, and fewer “weird edge cases.”
            </p>
          </article>

          <article class="card">
            <h3 class="h3">Tooling</h3>
            <div class="pill-row">
              <span class="pill">Docker</span>
              <span class="pill">CI/CD</span>
              <span class="pill">Git</span>
              <span class="pill">MongoDB</span>
              <span class="pill">Auth0</span>
            </div>
            <p class="p small">
              I like repeatable environments: one command to run locally, and predictable builds in CI.
            </p>
          </article>
        </div>
      </section>

      <!-- WORK -->
      <section id="work" class="section shell stagger-3">
        <div class="section-head">
          <h2 class="h2">Work</h2>
          <p class="lead">
            A few projects that show how I think: architecture choices, UI detail, and shipping end-to-end.
          </p>
        </div>

        <div class="projects">
          <article class="card project">
            <div class="project-top">
              <h3 class="h3">Quest Log</h3>
              <div class="tag-row">
                <span class="tag">Svelte</span>
                <span class="tag">Spring Boot</span>
                <span class="tag">MongoDB</span>
              </div>
            </div>
            <p class="p">
              A gamified task manager inspired by RPG systems. Focused on simple workflows, solid data
              modeling, and a UI that feels satisfying to use.
            </p>
            <ul class="list">
              <li>Task “quests” with status, deadlines, and reward loops</li>
              <li>API-first design with clean DTOs and validation</li>
              <li>Responsive UI with friendly empty states</li>
            </ul>
            <div class="project-actions">
              <a class="link-btn" href="#">View details</a>
              <a class="link-btn ghost" href="#">GitHub</a>
            </div>
          </article>

          <article class="card project">
            <div class="project-top">
              <h3 class="h3">Portfolio Platform</h3>
              <div class="tag-row">
                <span class="tag">Spring Boot</span>
                <span class="tag">Auth</span>
                <span class="tag">CI</span>
              </div>
            </div>
            <p class="p">
              A full-stack portfolio system built to showcase real engineering: secure endpoints, image
              handling, and predictable deployments.
            </p>
            <ul class="list">
              <li>Role-based UI and protected APIs</li>
              <li>Image upload + storage strategy</li>
              <li>CI pipeline to keep builds honest</li>
            </ul>
            <div class="project-actions">
              <a class="link-btn" href="#">View details</a>
              <a class="link-btn ghost" href="#">Live demo</a>
            </div>
          </article>

          <article class="card project">
            <div class="project-top">
              <h3 class="h3">Service Integration Lab</h3>
              <div class="tag-row">
                <span class="tag">Docker</span>
                <span class="tag">Microservices</span>
                <span class="tag">Testing</span>
              </div>
            </div>
            <p class="p">
              A sandbox for practicing service-to-service communication, local orchestration, and tests
              that don’t break in CI.
            </p>
            <ul class="list">
              <li>Docker Compose environments for dev/test</li>
              <li>Contract-ish thinking (inputs/outputs stay stable)</li>
              <li>Integration tests that match real runtime behavior</li>
            </ul>
            <div class="project-actions">
              <a class="link-btn" href="#">View details</a>
              <a class="link-btn ghost" href="#">Readme</a>
            </div>
          </article>
        </div>
      </section>

      <!-- EXPERIENCE -->
      <section id="experience" class="section shell stagger-4">
        <div class="section-head">
          <h2 class="h2">Experience</h2>
          <p class="lead">A quick snapshot. This section is meant to be scannable in 10 seconds.</p>
        </div>

        <div class="timeline">
          <article class="card timeline-item">
            <div class="timeline-left">
              <div class="dot"></div>
              <div class="line"></div>
            </div>
            <div class="timeline-body">
              <div class="row">
                <h3 class="h3">Software Developer (Projects & Collaboration)</h3>
                <span class="meta">2025 — Present</span>
              </div>
              <p class="p">
                Built full-stack applications with attention to architecture, testing, and deployment realism.
                Focused on practical deliverables: features that work, and code that stays readable.
              </p>
              <div class="pill-row">
                <span class="pill">API design</span>
                <span class="pill">Auth</span>
                <span class="pill">CI stability</span>
                <span class="pill">UX polish</span>
              </div>
            </div>
          </article>

          <article class="card timeline-item">
            <div class="timeline-left">
              <div class="dot"></div>
              <div class="line"></div>
            </div>
            <div class="timeline-body">
              <div class="row">
                <h3 class="h3">Computer Science — Champlain College</h3>
                <span class="meta">Graduating soon</span>
              </div>
              <p class="p">
                Coursework and projects focused on real-world engineering: system design, testing,
                clean code practices, and building end-to-end apps.
              </p>
              <div class="pill-row">
                <span class="pill">Team projects</span>
                <span class="pill">Software engineering</span>
                <span class="pill">Full-stack</span>
              </div>
            </div>
          </article>
        </div>
      </section>

      <!-- CONTACT -->
      <section id="contact" class="section shell stagger-5">
        <div class="section-head">
          <h2 class="h2">Contact</h2>
          <p class="lead">Want to talk? Send a message and I’ll get back to you.</p>
        </div>

        <div class="grid-2">
          <article class="card">
            <h3 class="h3">Message</h3>

            <form class="form" action="#" method="post">
              <label class="field">
                <span class="label">Name</span>
                <input class="input" type="text" placeholder="Your name" />
              </label>

              <label class="field">
                <span class="label">Email</span>
                <input class="input" type="email" placeholder="you@example.com" />
              </label>

              <label class="field">
                <span class="label">Message</span>
                <textarea class="input textarea" placeholder="Tell me what you're working on..."></textarea>
              </label>

              <button class="nav-btn primary" type="submit">Send message</button>
              <p class="p small muted">
                (Sample form — wire it up later with your backend or a service like Formspree.)
              </p>
            </form>
          </article>

          <article class="card">
            <h3 class="h3">Links</h3>
            <p class="p">Keep this minimal. Recruiters want one click to the proof.</p>

            <div class="link-list">
              <a class="contact-link" href="#"><span class="contact-icon"></span><span>GitHub</span></a>
              <a class="contact-link" href="#"><span class="contact-icon"></span><span>LinkedIn</span></a>
              <a class="contact-link" href="#"><span class="contact-icon"></span><span>Email</span></a>
              <a class="contact-link" href="#"><span class="contact-icon"></span><span>Resume (PDF)</span></a>
            </div>

            <div class="callout">
              <div class="callout-dot"></div>
              <p class="p small">
                Looking for roles where I can own features end-to-end: design → implementation → tests → deployment.
              </p>
            </div>
          </article>
        </div>
      </section>

      <footer class="footer shell stagger-6">
        <p class="p small muted">© {new Date().getFullYear()} Vlad Loghin — Portfolio</p>
      </footer>
    </main>
  </div>
{/if}

<style>
  :global(.ripple-active) {
    position: relative;
    overflow: hidden;
  }

  /* If your old SvelteKit layout had a dark body background, kill it here */
  :global(body) {
    margin: 0;
    font-family: Inter, system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif;
  }

  /* Let the About section span full width while keeping its content centered */
  :global(.full-bleed) {
    width: 100%;
    padding-left: 0;
    padding-right: 0;
  }

  :global(.full-bleed .about-container) {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 24px;
  }

  /* Move the About pill outside the text block */
  :global(.about-hero .section-head) {
    position: relative;
    padding-top: 24px;
  }

  :global(.about-hero .section-pill) {
    position: absolute;
    top: -14px;
    left: 0;
  }
</style>
