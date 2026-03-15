# cyberpunk-bar

`cyberpunk-bar` is a Docker-first bar operations stack with three main surfaces in one web app:

- cashier flow for creating and giving away orders
- worker flow for picking up and completing active orders
- info board flow for showing the live queue and ready orders

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

## Configuration

Default local settings live in `.env.example`:

```dotenv
DB_PORT=5420
BACKEND_PORT=8020
FRONTEND_PORT=8021
BACKEND_IMAGE=cyberpunk-bar-backend:local
FRONTEND_IMAGE=cyberpunk-bar-frontend:local
```

Override them in `.env` before building or starting the stack.

## Runtime architecture

- `db` runs PostgreSQL 17 with persisted data in the `pgdata` Docker volume.
- `backend` exposes the Ktor API on container port `8080`.
- `frontend` serves the Compose/Wasm bundle through nginx on container port `80`.
- nginx proxies browser requests from `/api/...` to the backend container, so the frontend does not need a hardcoded backend host for the Docker flow.

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

The frontend service layer calls relative `/api/...` endpoints. That works out of the box in the Docker stack because nginx handles the proxy. If you run the frontend development server standalone, you need to provide an equivalent proxy or another compatible API path.

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
