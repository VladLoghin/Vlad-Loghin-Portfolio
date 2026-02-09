<!-- src/lib/components/LoginButton.svelte -->
<script lang="ts">
  import { login, isLoading } from "../stores/auth";
</script>

<button class="nav-btn primary" on:click={login} disabled={$isLoading}>
  {$isLoading ? "Loading..." : "Log in"}
</button>
