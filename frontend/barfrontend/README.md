# barfrontend

Compose Multiplatform Wasm frontend for the cyberpunk bar stack.

## What it contains

- cashier flow for creating and giving away orders
- worker flow for active order processing
- info board flow for queue display
- administrator UI for catalog and team management

Main frontend code lives in:

- `composeApp/src/wasmJsMain/kotlin`
- `composeApp/src/wasmJsMain/resources`
- `nginx.conf`

## Local Gradle commands

```sh
./gradlew check
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
./gradlew :composeApp:wasmJsBrowserDistribution
```

## Docker

Build the production image from this directory:

```sh
docker build -t bar-frontend:latest .
docker run --rm -p 8080:80 bar-frontend:latest
```

The container serves the built Wasm bundle with nginx on port `80`.

Important Docker detail:

- the build stage runs on `linux/amd64`, even on Apple Silicon, because the
  Kotlin/Wasm Binaryen download used by the production distribution task is not
  currently available for Linux `arm64`
- the Dockerfile uses named BuildKit cache mounts for Gradle, Konan, npm, and
  `kotlin-js-store`, so repeat builds are much faster than the first cold build

## API contract

The frontend service layer calls relative `/api/...` endpoints. In the normal
Docker flow, nginx proxies those requests to the backend container, so the
frontend must not hardcode a backend host for the containerized path.

If you run the Wasm dev server without nginx, provide an equivalent proxy to
the backend yourself.

## PWA resources

PWA-related files live under `composeApp/src/wasmJsMain/resources/`, including:

- `manifest.webmanifest`
- `service-worker.js`
- `register-service-worker.js`
- install icons
