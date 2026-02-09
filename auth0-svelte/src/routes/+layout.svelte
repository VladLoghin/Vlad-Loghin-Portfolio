<script lang="ts">
  import { onMount } from "svelte";
  import { initializeAuth } from "$lib/stores/auth";
  import "../style.css";

  onMount(() => {
    initializeAuth();
  });
</script>

<slot />

<style>
  :global(body) {
    margin: 0;
    font-family: Inter, system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif;
  }
</style>
