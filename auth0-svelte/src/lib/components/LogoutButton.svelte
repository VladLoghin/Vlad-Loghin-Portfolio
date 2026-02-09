<!-- src/lib/components/LogoutButton.svelte -->
<script lang="ts">
  import { logout, isLoading } from "../stores/auth";
</script>

<button class="nav-btn ghost" on:click={logout} disabled={$isLoading}>
  Log out
</button>
