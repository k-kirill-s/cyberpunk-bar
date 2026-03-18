# barbackend

Ktor backend for the cyberpunk bar stack.

## Responsibilities

- REST API for cashier, worker, info board, and admin flows
- PostgreSQL access through Exposed
- dependency injection via Koin
- startup-time schema creation from table definitions

Main code lives in `src/main/kotlin`.

## Useful commands

```sh
./gradlew test
./gradlew build
./gradlew buildFatJar
./gradlew run
```

## Docker build behavior

The backend Docker image now uses a two-step Gradle flow:

1. a dependency warmup stage that resolves compile/runtime classpaths
2. a real build stage that copies `src/` and runs `buildFatJar`

That keeps Docker layer caching effective without running a misleading
source-less Gradle build.

## Configuration

The service reads these environment variables:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `ADMIN_USERNAME`
- `ADMIN_PASSWORD`

The HTTP server listens on container port `8080`.

## Notes

- route entrypoints are grouped under `controller/*Routing.kt`
- database schema creation currently happens at startup through Exposed tables
- local Docker Compose is the recommended full-stack verification path
