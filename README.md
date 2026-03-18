# cyberpunk-bar

`cyberpunk-bar` is a Docker-first bar operations stack with three main surfaces in one web app:

- cashier flow for creating and giving away orders
- worker flow for picking up and completing active orders
- info board flow for showing the live queue and ready orders

It also includes a protected administrator surface for catalog and team management.

The repository contains a Kotlin/Ktor backend, a Kotlin Multiplatform Compose/Wasm frontend, and a PostgreSQL database wired together through Docker Compose.

## Stack

- Backend: Ktor, Koin, Exposed, PostgreSQL
- Frontend: Kotlin Multiplatform, Compose Multiplatform, Wasm, nginx
- Local orchestration: Docker Compose, shell scripts, Makefile helpers

## Repository layout

```text
.
|-- backend/barbackend      # Ktor API + database layer
|-- frontend/barfrontend    # Compose/Wasm frontend + nginx proxy config
|-- scripts/                # image build, stack start, URL discovery
|-- compose.yaml            # root local stack
|-- Makefile                # convenience commands
|-- .env.example            # default local ports and image tags
```

## Quick start

1. Create a local config file:

   ```sh
   cp .env.example .env
   ```

2. Build the Docker images:

   ```sh
   ./scripts/build-images.sh
   ```

   The script enables Docker BuildKit and Compose Bake by default. The first
   frontend build can still take a while because the Kotlin/Wasm production
   bundle runs an optimization step, but repeated builds should reuse Docker
   and Gradle caches aggressively.

3. Start the stack:

   ```sh
   ./scripts/start-stack.sh
   ```

`start-stack.sh` starts the database, backend, and frontend containers from prebuilt images. If the frontend or backend image is missing, the script prompts to build it first.

After startup, the script prints:

- the local frontend URL
- the detected LAN URL for other devices on the same network

## Common commands

Use either the scripts directly or the Makefile wrappers.

```sh
make build      # build backend and frontend images
make start      # start the stack from local images
make logs       # follow container logs
make ps         # show container status
make urls       # print local and LAN app URLs
make down       # remove containers
```

Service-specific image builds are also supported:

```sh
./scripts/build-images.sh backend
./scripts/build-images.sh frontend
```

`./scripts/build-images.sh` exports:

- `DOCKER_BUILDKIT=1`
- `COMPOSE_BAKE=true`

unless you already set them yourself.

## Configuration

Default local settings live in `.env.example`:

```dotenv
DB_PORT=5420
BACKEND_PORT=8042
FRONTEND_PORT=8043
BACKEND_IMAGE=cyberpunk-bar-backend:local
FRONTEND_IMAGE=cyberpunk-bar-frontend:local
ADMIN_USERNAME=cyberadm
ADMIN_PASSWORD=cyberadm
```

Override them in `.env` before building or starting the stack.

`ADMIN_USERNAME` and `ADMIN_PASSWORD` are passed to the backend container and protect the `Администратор` section of the web UI. If you keep the defaults, the initial admin login is `cyberadm / cyberadm`.

## Publishing images

The repository now includes a GitHub Actions workflow at
`.github/workflows/publish-images.yml` that builds and pushes both app images
to GitHub Container Registry (`ghcr.io`):

- `ghcr.io/<owner>/cyberpunk-bar-backend`
- `ghcr.io/<owner>/cyberpunk-bar-frontend`

The workflow runs when you push a Git tag that starts with `v`, for example:

```sh
git tag v1.0.0
git push origin v1.0.0
```

It publishes multi-platform images for `linux/amd64` and `linux/arm64`, and
adds version, major/minor, and commit SHA tags automatically.

The current frontend Dockerfile intentionally uses an `amd64` build stage even
when publishing `arm64` images, because the Kotlin/Wasm Binaryen download used
by the production build is not currently available for Linux `arm64`. The final
runtime image is still published for both target platforms.

After the first publish, verify that the packages are visible in GitHub
Packages and set them to public if you want anonymous pulls.

To run the existing Compose stack against published images instead of local
ones, override the image tags in `.env`:

```dotenv
BACKEND_IMAGE=ghcr.io/<owner>/cyberpunk-bar-backend:1.0.0
FRONTEND_IMAGE=ghcr.io/<owner>/cyberpunk-bar-frontend:1.0.0
```

Then start the stack as usual:

```sh
./scripts/start-stack.sh
```

## Runtime architecture

- `db` runs PostgreSQL 17 with persisted data in the `pgdata` Docker volume.
- `backend` exposes the Ktor API on container port `8080`.
- `frontend` serves the Compose/Wasm bundle through nginx on container port `80`.
- nginx proxies browser requests from `/api/...` to the backend container, so the frontend does not need a hardcoded backend host for the Docker flow.
- the administrator screen uses those same relative `/api/...` requests and sends credentials through request headers after a successful admin login

That proxy setup is the reason published host ports can change without frontend source edits.

## Development notes

### Backend

The backend lives in `backend/barbackend`.

Useful commands:

```sh
cd backend/barbackend
./gradlew test
./gradlew build
./gradlew buildFatJar
./gradlew run
```

At startup the application reads `DB_*` environment variables and creates missing tables with Exposed.

The backend Docker image now uses a dedicated dependency warmup stage instead of
running a full source-less Gradle build. That keeps the Docker cache useful
without the misleading `NO-SOURCE` output from a fake compile phase.

Main route groups currently cover:

- orders
- positions
- position variants
- position items
- workers

### Frontend

The frontend lives in `frontend/barfrontend`.

Useful commands:

```sh
cd frontend/barfrontend
./gradlew check
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
./gradlew :composeApp:wasmJsBrowserDistribution
```

The production Docker image builds the Wasm distribution and serves it with nginx using [`frontend/barfrontend/nginx.conf`](frontend/barfrontend/nginx.conf).

For local and CI Docker builds, the frontend image keeps stable named caches for
Gradle, Konan, npm, and `kotlin-js-store`. Warm builds are therefore much
faster than the first cold build.

The frontend service layer calls relative `/api/...` endpoints. That works out of the box in the Docker stack because nginx handles the proxy. If you run the frontend development server standalone, you need to provide an equivalent proxy or another compatible API path.

### PWA layer

The web bundle now includes a first-pass PWA layer in
`frontend/barfrontend/composeApp/src/wasmJsMain/resources/`:

- `manifest.webmanifest`
- `service-worker.js`
- `register-service-worker.js`
- install icons for Android and iOS home screen usage

This pass keeps API traffic network-first and does not cache `/api/...` responses, so cashier, worker, and info board data still come from the backend directly.

For real installability on phones and tablets, serve the frontend over HTTPS. Localhost remains fine for development, but LAN `http://...` URLs generally will not get full install and service worker behavior on mobile browsers.

If you later package the web UI in an Android or iOS wrapper, plan to add a configurable backend base URL. The current relative `/api/...` contract assumes the nginx reverse proxy from the Docker/web deployment path.

## API/frontend contract notes

- Backend routes accept form parameters for most create and update operations.
- Frontend API access is centralized in `frontend/barfrontend/composeApp/src/wasmJsMain/kotlin/by/cyberpunkfandom/barfrontend/data/services/MainService.kt`.
- If you change backend DTOs or route contracts, update the matching frontend models, mappers, repositories, and screens in the same change.

## Stopping the stack

Pause containers:

```sh
docker compose stop
```

Remove containers:

```sh
docker compose down
```
