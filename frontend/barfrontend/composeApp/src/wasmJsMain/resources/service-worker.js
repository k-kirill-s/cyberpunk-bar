const APP_SHELL_CACHE = "cyberpunk-bar-shell-v3";
const RUNTIME_CACHE = "cyberpunk-bar-runtime-v3";
const OFFLINE_FALLBACK = "/offline.html";
const APP_SHELL_URLS = [
    "/",
    "/index.html",
    "/styles.css",
    "/manifest.webmanifest",
    "/register-service-worker.js",
    OFFLINE_FALLBACK,
    "/icons/pwa-icon.svg",
    "/icons/pwa-192.png",
    "/icons/pwa-512.png",
    "/icons/apple-touch-icon.png",
];

self.addEventListener("install", (event) => {
    event.waitUntil(
        caches.open(APP_SHELL_CACHE).then((cache) => cache.addAll(APP_SHELL_URLS))
    );
    self.skipWaiting();
});

self.addEventListener("activate", (event) => {
    event.waitUntil(
        caches.keys().then((cacheNames) =>
            Promise.all(
                cacheNames.map((cacheName) => {
                    if (cacheName === APP_SHELL_CACHE || cacheName === RUNTIME_CACHE) {
                        return Promise.resolve();
                    }

                    return caches.delete(cacheName);
                })
            )
        )
    );
    self.clients.claim();
});

self.addEventListener("fetch", (event) => {
    const { request } = event;
    const url = new URL(request.url);

    if (request.method !== "GET" || url.origin !== self.location.origin) {
        return;
    }

    if (url.pathname.startsWith("/api/")) {
        event.respondWith(fetch(request));
        return;
    }

    if (request.mode === "navigate") {
        event.respondWith(handleNavigationRequest(request));
        return;
    }

    if (isStaticAsset(request, url)) {
        event.respondWith(handleStaticAssetRequest(request));
    }
});

async function handleNavigationRequest(request) {
    try {
        const response = await fetch(request);
        const cache = await caches.open(RUNTIME_CACHE);
        cache.put(request, response.clone());
        return response;
    } catch (error) {
        const cachedResponse = await caches.match(request);
        if (cachedResponse) {
            return cachedResponse;
        }

        const cachedIndex = await caches.match("/index.html");
        if (cachedIndex) {
            return cachedIndex;
        }

        return caches.match(OFFLINE_FALLBACK);
    }
}

async function handleStaticAssetRequest(request) {
    const cachedResponse = await caches.match(request);
    if (cachedResponse) {
        return cachedResponse;
    }

    const response = await fetch(request);
    if (response.ok) {
        const cache = await caches.open(RUNTIME_CACHE);
        cache.put(request, response.clone());
    }
    return response;
}

function isStaticAsset(request, url) {
    if (url.pathname === "/service-worker.js") {
        return false;
    }

    if (request.destination === "script" ||
        request.destination === "style" ||
        request.destination === "image" ||
        request.destination === "font" ||
        request.destination === "manifest") {
        return true;
    }

    return url.pathname.endsWith(".wasm") ||
        url.pathname.endsWith(".js") ||
        url.pathname.endsWith(".css") ||
        url.pathname.endsWith(".svg") ||
        url.pathname.endsWith(".png");
}
