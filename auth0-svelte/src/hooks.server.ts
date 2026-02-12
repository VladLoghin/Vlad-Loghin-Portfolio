import type { Handle } from '@sveltejs/kit';
import { env } from '$env/dynamic/private';

export const handle: Handle = async ({ event, resolve }) => {
	if (event.url.pathname.startsWith('/api')) {
		const backendUrl = `${env.BACKEND_URL || 'http://localhost:8080'}${event.url.pathname}${event.url.search}`;

		try {
			const headers = new Headers(event.request.headers);
			headers.delete('host');
			headers.delete('origin');
			headers.delete('referer');

			const hasBody = event.request.method !== 'GET' && event.request.method !== 'HEAD';

			const response = await fetch(backendUrl, {
				method: event.request.method,
				headers,
				body: hasBody ? await event.request.arrayBuffer() : undefined,
				// @ts-ignore - duplex needed for Node fetch with body
				duplex: 'half'
			});

			return new Response(response.body, {
				status: response.status,
				statusText: response.statusText,
				headers: response.headers
			});
		} catch (err) {
			console.error(`[proxy] ${event.request.method} ${backendUrl} failed:`, err);
			return new Response(JSON.stringify({ error: 'Backend unavailable' }), {
				status: 502,
				headers: { 'Content-Type': 'application/json' }
			});
		}
	}

	return resolve(event);
};
