# AGENTS.md

## Project scope

This repository is a Docker-first local stack for a bar operations app. It includes:

- a Ktor backend in `backend/barbackend`
- a Compose Multiplatform Wasm frontend in `frontend/barfrontend`
- a PostgreSQL service and root Docker Compose orchestration in `compose.yaml`

The main user-facing flows are cashier, worker, and info board. Keep documentation and code changes aligned with that product model.

## Repository map

- `backend/barbackend/src/main/kotlin`
  API routing, DI, repositories, Exposed database entities, and domain models
- `backend/barbackend/src/main/resources/application.yaml`
  backend server config
- `frontend/barfrontend/composeApp/src/wasmJsMain/kotlin`
  Wasm UI, state, repositories, service layer, theme, and navigation
- `frontend/barfrontend/nginx.conf`
  frontend reverse proxy for `/api`
- `scripts/`
  local build/start helper scripts
- `Makefile`
  top-level convenience commands
- `.env.example`
  default local ports and image tags

## Preferred workflow

Use the root Docker flow unless the task specifically requires subproject-only work.

Primary commands:

```sh
cp .env.example .env
./scripts/build-images.sh
./scripts/start-stack.sh
make logs
make down
```

Useful subproject checks:

```sh
cd backend/barbackend && ./gradlew test
cd frontend/barfrontend && ./gradlew check
```

## Change guardrails

1. Do not hardcode backend hosts in frontend code for the containerized path.
   The current contract is relative `/api/...` requests from `MainService`, with nginx proxying to the backend container.

2. Keep orchestration files in sync when changing ports or image names.
   If you touch `compose.yaml`, `.env.example`, `scripts/`, `Makefile`, or the README, review the others in the same pass.

3. Preserve the backend/frontend contract end to end.
   Backend route or DTO changes usually require corresponding updates in frontend models, mappers, repositories, and screens.

4. Preserve the current UI language unless the user asks for copy changes.
   The frontend currently contains Russian product copy.

5. Treat the database layer as PostgreSQL-first.
   Database connection settings come from `DB_*` environment variables in `Databases.kt`, and schema creation happens through Exposed table definitions.

## Implementation notes

- Backend route entrypoints are grouped under `controller/*Routing.kt`.
- Frontend API calls are centralized in `frontend/barfrontend/composeApp/src/wasmJsMain/kotlin/by/cyberpunkfandom/barfrontend/data/services/MainService.kt`.
- The recommended local path for verifying full-stack behavior is the Docker stack, not the standalone Wasm dev server.
- Avoid committing generated files, Gradle outputs, local env files, or `.playwright-cli` artifacts.

## Before finishing a task

Run the narrowest relevant validation you can:

- backend-only changes: `cd backend/barbackend && ./gradlew test`
- frontend-only changes: `cd frontend/barfrontend && ./gradlew check`
- compose/docs/script changes: verify referenced commands, paths, and port values against the repo

If you cannot run validation, state that explicitly.
