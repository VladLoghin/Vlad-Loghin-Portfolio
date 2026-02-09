<script lang="ts">
  import { onMount } from "svelte";
  import { isLoading, user, login, logout, isAuthenticated, isAdmin } from "$lib/stores/auth";
  import Projects from "$lib/components/Projects.svelte";
  import Hobbies from "$lib/components/Hobbies.svelte";
  import Reviews from "$lib/components/Reviews.svelte";
  import Skills from "$lib/components/Skills.svelte";
  import Education from "$lib/components/Education.svelte";
  import type { Project } from "$lib/components/Projects.svelte";
  import type { Hobby } from "$lib/components/Hobbies.svelte";
  import type { Review } from "$lib/components/Reviews.svelte";
  import type { Skill } from "$lib/components/Skills.svelte";
  import type { EducationItem } from "$lib/components/Education.svelte";

  const projects: Project[] = [
    {
      id: "vladtech",
      name: "VladTech",
      badge: "Full-stack",
      description:
        "Renovation business platform with role-based access, reviews/portfolio modules, and production-ready deployment.",
      tags: ["Spring Boot", "React", "MongoDB", "Auth0", "Docker"],
      links: [
       
      ]
    },
    {
      id: "Unnamed Game Project",
      name: "Unnamed Game Project",
      badge: "In progress",
      badgeVariant: "ghost",
      description:
        "I am currently working and learning about game design and development for a 2D platformer puzzle game with plants. For this project I've called upon the help of a master's student in game design and a graduate from a level design bootcamp",
      tags: ["Unity 2d", "C#", "Game Design", "Puzzle Mechanics"],
      links: [
  
      ]
    },
    {
      id: "petclinic",
      name: "PetClinic",
      badge: "Full-stack",
      description:
        "Worked on the PetClinic Billing team, was Scrum Master and developer, worked on emailing and making requests not be static.",
      tags: ["Spring", "WebFlux", "Docker Compose", "CI"],
      links: [

      ]
    }
  ];

  let hobbies: Hobby[] = [
    {
      id: "music",
      title: "Music",
      description:
        "I love to play and learn songs that I love musically.",
      tags: ["Guitar"]
    },
    {
      id: "gamedesign",
      title: "Game Design",
      description:
        "Puzzle systems, time-based mechanics, and narrative worlds that reward observation and experimentation.",
      tags: ["Systems", "Level design", "Lore", "Prototyping"]
    },
    {
      id: "tabletop",
      title: "Tabletop & Strategy",
      description:
        "Competitive deckbuilding and strategy games—good practice for planning, tradeoffs, and adaptation.",
      tags: ["Magic: The Gathering", "Warhammer", "Collecting"]
    },
    {
      id: "reading",
      title: "Reading",
      description:
        "I enjoy reading from time to time to unwind and escape for a little while. Some of my favourite series to read are the A Song of Ice and Fire books, the new Ultimate Marvel universe comics, the DC Absolute universe comics, and my favourite philosophical book is Meditations by Marcus Aurelius.",
      tags: ["Comics", "Fantasy", "Philosophy"]
    },
    {
      id: "Gaming",
      title: "Gaming",
      description:
        "One of the hobbies that has stuck with me over the years, games are a form of higher art taking into account, music, visuals, storytelling and many more things. Some of my favourite games are Crusader Kings 3, Total War Warhammer 3 and Devil May Cry 5. My favourite game character is Dante. He's so cool!",
      tags: ["RPGs", "Strategy", "Puzzle", "Adventure", "Hack and Slash"]
    }
  ];

  const reviews: Review[] = [
    {
      id: "teammate",
      name: "Anonymous Teammate",
      role: "Team project",
      avatar: "A",
      content: "Vlad consistently took ownership of the hard parts and still made time to help others unblock.",
      rating: 5
    },
    {
      id: "mentor",
      name: "Mentor",
      role: "Code review",
      avatar: "M",
      content: "Strong engineering instincts. When something breaks, he traces it to the root cause instead of patching.",
      rating: 4
    },
    {
      id: "client",
      name: "Client",
      role: "Web app delivery",
      avatar: "C",
      content: "Clear communication, fast turnaround, and the end result felt polished—not a rough prototype.",
      rating: 5
    }
  ];

  let skills: Skill[] = [
    {
      id: "backend",
      category: "Backend",
      items: ["Java", "Spring Boot", "Spring WebFlux", "REST APIs", "Docker", "CI/CD"]
    },
    {
      id: "frontend",
      category: "Frontend",
      items: ["Svelte", "React", "TypeScript", "HTML/CSS", "Responsive Design"]
    },
    {
      id: "databases",
      category: "Databases & Tools",
      items: ["MongoDB", "MySQL", "Git", "Auth0"]
    },
    {
      id: "practices",
      category: "Best Practices",
      items: ["Clean Code", "Solid Principles", "Testing", "Agile", "Code Review"]
    }
  ];

  let education: EducationItem[] = [
    {
      id: "champlain",
      institutionName: "Champlain College St-Lambert",
      degree: "Computer Science"
    },
    {
      id: "selfstudy",
      institutionName: "Self-Study",
      degree: "Spring Boot + React + Docker"
    }
  ];

  let showContent = false;
  let rippleActive = false;
  let mobileMenuOpen = false;

  function handleReveal() {
    rippleActive = true;
    setTimeout(() => {
      showContent = true;
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

  onMount(() => {
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
          <a class="nav-link" href="#hobbies">Hobbies</a>
          <a class="nav-link" href="#reviews">Reviews</a>
          <a class="nav-link" href="#contact">Contact</a>
          {#if $isAdmin}
            <a class="nav-link admin-link" href="/admin">Admin</a>
          {/if}
        </div>

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
            <a class="mobile-link" href="#hobbies" on:click={closeMobileMenu}>Hobbies</a>
            <a class="mobile-link" href="#reviews" on:click={closeMobileMenu}>Reviews</a>
            <a class="mobile-link" href="#contact" on:click={closeMobileMenu}>Contact</a>
            {#if $isAdmin}
              <a class="mobile-link admin-link" href="/admin" on:click={closeMobileMenu}>Admin</a>
            {/if}
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
                I'm Vlad Loghin — a software developer who builds clean, reliable web apps with thoughtful UX.
                I like shipping features fast, then tightening the engineering until it's solid.
              </p>

            </div>

            <div class="about-grid">
              <article class="card">
                <h3 class="h3">What I do</h3>
                <ul class="list">
                  <li>Java / Spring Boot APIs</li>
                  <li>Svelte & React frontends</li>
                  <li>Docker & CI pipelines</li>
                </ul>
              </article>

              <article class="card">
                <h3 class="h3">What I care about</h3>
                <div class="pill-row">
                  <span class="pill">Good Software</span>
                  <span class="pill">Clean Code</span>
                  <span class="pill">New Challenges</span>
                  <span class="pill">Continuous Learning</span>
                  <span class="pill">Creativity</span>
                </div>
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

      <!-- EDUCATION -->
      <Education {education} isAdmin={$isAdmin} onSave={async (next) => {
        education = next;
      }} />

      <!-- PROJECTS -->
      <Projects {projects} isAdmin={$isAdmin} />

      <!-- SKILLS -->
      <Skills
  {skills}
  isAdmin={$isAdmin}
  onSave={async (next) => {
    const res = await fetch("/api/skills", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(next)
    });

    if (!res.ok) throw new Error("Failed to save skills");
    skills = next;
  }}
/>



      <!-- HOBBIES -->
      <Hobbies {hobbies} isAdmin={$isAdmin} onSave={async (next) => {
        // Add your API call here when ready
        hobbies = next;
      }} />

      <!-- REVIEWS -->
      <Reviews {reviews} isAdmin={$isAdmin} />

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
            <form class="contact-form">
              <div class="form-group">
                <input class="input" type="text" placeholder="Your name" required />
              </div>
              <div class="form-group">
                <input class="input" type="email" placeholder="Your email" required />
              </div>
              <div class="form-group">
                <textarea class="input textarea" placeholder="Tell me about your project..." rows="5"></textarea>
              </div>
              <button class="nav-btn primary" type="submit">Send message</button>
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
        <p class="p small muted">© {new Date().getFullYear()} Vlad Loghin</p>
      </footer>
    </main>
  </div>
{/if}

<style>
  :global(.ripple-active) {
    position: relative;
    overflow: hidden;
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

  .mobile-link.admin-link {
    color: #ff9800;
    font-weight: 700;
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

  .nav-btn {
    padding: 12px 24px;
    border: none;
    border-radius: 8px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s ease;
  }

  .nav-btn.primary {
    background: var(--green);
    color: white;
  }

  .nav-btn.primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(56, 197, 94, 0.3);
  }

  .admin-link {
    color: #ff9800 !important;
    font-weight: 700;
  }

  .admin-badge {
    display: inline-block;
    margin-left: 8px;
    padding: 2px 8px;
    background: #ff9800;
    color: white;
    font-size: 11px;
    font-weight: 700;
    border-radius: 4px;
    text-transform: uppercase;
  }
</style>
