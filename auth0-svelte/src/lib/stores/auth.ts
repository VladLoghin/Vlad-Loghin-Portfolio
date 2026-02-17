import { writable, derived, get, type Readable } from 'svelte/store';
import { createAuth0Client, type Auth0Client, type User } from '@auth0/auth0-spa-js';
import { browser } from '$app/environment';

export const auth0Client = writable<Auth0Client | null>(null);
export const user = writable<User | null>(null);
export const isAuthenticated = writable<boolean>(false);
export const isLoading = writable<boolean>(true);
export const error = writable<string | null>(null);
export const userRoles = writable<string[]>([]);

// Derived stores
export const isLoggedIn: Readable<boolean> = derived(
  [isAuthenticated, isLoading],
  ([$isAuthenticated, $isLoading]) => $isAuthenticated && !$isLoading
);

export const isAdmin: Readable<boolean> = derived(
  [userRoles],
  ([$userRoles]) => Array.isArray($userRoles) && $userRoles.some(role => role.toLowerCase() === 'admin')
);

export const emailVerified: Readable<boolean> = derived(
  [user, isAuthenticated],
  ([$user, $isAuthenticated]) => !$isAuthenticated || $user?.email_verified === true
);

export async function initializeAuth() {
  if (!browser) return;
  
  try {
    const client = await createAuth0Client({
      domain: import.meta.env.VITE_AUTH0_DOMAIN,
      clientId: import.meta.env.VITE_AUTH0_CLIENT_ID,
      authorizationParams: {
        redirect_uri: window.location.origin,
        audience: import.meta.env.VITE_AUTH0_AUDIENCE,
        scope: 'openid profile email'
      },
      useRefreshTokens: true,
      cacheLocation: 'localstorage'
    });

    auth0Client.set(client);

    // Handle Auth0 error redirect (e.g. email not verified)
    const params = new URLSearchParams(window.location.search);
    if (params.has('error')) {
      const errorDesc = params.get('error_description') || 'Authentication failed';
      error.set(errorDesc);
      window.history.replaceState({}, document.title, window.location.pathname);
      isLoading.set(false);
      return;
    }

    // Handle successful callback
    if (params.has('code')) {
      await client.handleRedirectCallback();
      window.history.replaceState({}, document.title, window.location.pathname);
    }

    // Check authentication status
    const authenticated = await client.isAuthenticated();
    isAuthenticated.set(authenticated);

    if (authenticated) {
      const userData = await client.getUser();
      user.set(userData || null);

      console.log('Full user object:', userData);

      // Debug: log all keys to find roles
      Object.keys(userData || {}).forEach(key => {
        if (key.includes('role') || key.includes('admin')) {
          console.log(`Found key: ${key}`, userData?.[key as keyof typeof userData]);
        }
      });

      let roles: string[] = [];
      
      const namespace = 'https://vladloghin.codes/roles';
      if (userData?.[namespace]) {
        roles = userData[namespace];
      }
      // Check other common locations
      else if (userData?.['app_metadata']?.roles) {
        roles = userData['app_metadata'].roles;
      }
      else if (userData?.['user_metadata']?.roles) {
        roles = userData['user_metadata'].roles;
      }
      else if (userData?.roles) {
        roles = userData.roles;
      }

      userRoles.set(Array.isArray(roles) ? roles : []);
      
      console.log('Full user object:', userData);
      console.log('Looking for namespace:', namespace);
      console.log('Extracted roles:', Array.isArray(roles) ? roles : []);
    }

    error.set(null);
  } catch (err) {
    console.error('Auth initialization error:', err);
    error.set(err instanceof Error ? err.message : 'Authentication initialization failed');
  } finally {
    isLoading.set(false);
  }
}

export async function login() {
  const client = get(auth0Client);
  if (!client) {
    console.error('Auth0 client not initialized — check your VITE_AUTH0_DOMAIN, VITE_AUTH0_CLIENT_ID, and VITE_AUTH0_AUDIENCE env vars');
    return;
  }
  try {
    console.log('Redirecting to Auth0...', {
      domain: import.meta.env.VITE_AUTH0_DOMAIN,
      clientId: import.meta.env.VITE_AUTH0_CLIENT_ID,
      audience: import.meta.env.VITE_AUTH0_AUDIENCE
    });
    await client.loginWithRedirect();
  } catch (err) {
    console.error('loginWithRedirect failed:', err);
  }
}

export async function signup() {
  const client = get(auth0Client);
  if (!client) return;
  try {
    await client.loginWithRedirect({
      authorizationParams: {
        screen_hint: 'signup'
      }
    });
  } catch (err) {
    console.error('signup redirect failed:', err);
  }
}

export async function logout() {
  const client = get(auth0Client);
  if (!client) return;

  user.set(null);
  isAuthenticated.set(false);
  userRoles.set([]);

  await client.logout({
    logoutParams: {
      returnTo: `${window.location.origin}`
    }
  });
}

export async function getToken(): Promise<string | null> {
  const client = get(auth0Client);
  if (!client) return null;
  
  try {
    return await client.getTokenSilently();
  } catch (err: any) {
    if (err.error === 'login_required') {
      await login();
    }
    return null;
  }
}