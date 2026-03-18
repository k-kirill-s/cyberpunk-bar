#!/usr/bin/env sh

set -eu

ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)

export DOCKER_BUILDKIT="${DOCKER_BUILDKIT:-1}"
export COMPOSE_BAKE="${COMPOSE_BAKE:-true}"

usage() {
    cat <<'EOF'
Usage:
  ./scripts/build-images.sh
  ./scripts/build-images.sh backend
  ./scripts/build-images.sh frontend
  ./scripts/build-images.sh backend frontend
EOF
}

validate_service() {
    case "$1" in
        backend|frontend) ;;
        *)
            printf 'Unsupported service: %s\n' "$1" >&2
            usage >&2
            exit 1
            ;;
    esac
}

if [ "$#" -eq 0 ]; then
    set -- backend frontend
fi

for service in "$@"; do
    validate_service "$service"
done

build_frontend_bundle() {
    printf 'Building frontend bundle on host...\n'
    (
        cd "$ROOT_DIR/frontend/barfrontend" &&
        ./gradlew \
            :composeApp:compileProductionExecutableKotlinWasmJs \
            :composeApp:wasmJsBrowserProductionWebpack \
            :composeApp:wasmJsBrowserDistribution \
            --rerun-tasks
    )
}

for service in "$@"; do
    if [ "$service" = "frontend" ]; then
        build_frontend_bundle
        break
    fi
done

printf 'Building images for: %s\n' "$*"
(cd "$ROOT_DIR" && docker compose build "$@")

printf 'Build completed for: %s\n' "$*"
