FROM nginx:alpine AS runtime

COPY composeApp/build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html/
COPY composeApp/build/compileSync/wasmJs/main/productionExecutable/kotlin/composeApp.mjs /usr/share/nginx/html/
COPY composeApp/build/compileSync/wasmJs/main/productionExecutable/kotlin/composeApp.uninstantiated.mjs /usr/share/nginx/html/
COPY composeApp/build/compileSync/wasmJs/main/productionExecutable/kotlin/composeApp.wasm /usr/share/nginx/html/
COPY composeApp/build/compileSync/wasmJs/main/productionExecutable/kotlin/composeApp.wasm.map /usr/share/nginx/html/
COPY composeApp/build/compose/skiko-runtime-processed-wasmjs/ /usr/share/nginx/html/
COPY build/js/node_modules/@js-joda/core/dist/js-joda.esm.js /usr/share/nginx/html/vendor/js-joda.esm.js
RUN cat <<'EOF' > /usr/share/nginx/html/index.html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>barfrontend</title>
    <link type="text/css" rel="stylesheet" href="styles.css">
    <script type="importmap">
        {
            "imports": {
                "@js-joda/core": "/vendor/js-joda.esm.js"
            }
        }
    </script>
    <script type="module">
        if (location.hostname === "localhost" || location.hostname === "127.0.0.1") {
            const registrations = navigator.serviceWorker?.getRegistrations?.() ?? Promise.resolve([])
            registrations.then((items) => Promise.all(items.map((item) => item.unregister())))
            if ("caches" in window) {
                caches.keys().then((keys) => Promise.all(keys.map((key) => caches.delete(key))))
            }
        }
        import "/composeApp.mjs";
    </script>
</head>
<body>
</body>
</html>
EOF
RUN find /usr/share/nginx/html -type d -exec chmod 755 {} \; \
 && find /usr/share/nginx/html -type f -exec chmod 644 {} \;
COPY nginx.conf /etc/nginx/conf.d/default.conf

HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://127.0.0.1/ > /dev/null || exit 1
