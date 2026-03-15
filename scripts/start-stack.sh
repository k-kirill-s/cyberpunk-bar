#!/usr/bin/env sh

set -eu

ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
BUILD_SCRIPT="$ROOT_DIR/scripts/build-images.sh"
SHOW_URLS_SCRIPT="$ROOT_DIR/scripts/show-app-urls.sh"

load_env_file() {
    file_path=$1
    if [ -f "$file_path" ]; then
        set -a
        # shellcheck disable=SC1090
        . "$file_path"
        set +a
    fi
}

load_env_file "$ROOT_DIR/.env.example"
load_env_file "$ROOT_DIR/.env"

BACKEND_IMAGE=${BACKEND_IMAGE:-cyberpunk-bar-backend:local}
FRONTEND_IMAGE=${FRONTEND_IMAGE:-cyberpunk-bar-frontend:local}

missing_services=""

if ! docker image inspect "$BACKEND_IMAGE" >/dev/null 2>&1; then
    missing_services="backend"
fi

if ! docker image inspect "$FRONTEND_IMAGE" >/dev/null 2>&1; then
    if [ -n "$missing_services" ]; then
        missing_services="$missing_services frontend"
    else
        missing_services="frontend"
    fi
fi

if [ -n "$missing_services" ]; then
    printf 'Missing prebuilt images for: %s\n' "$missing_services" >&2

    if [ ! -t 0 ]; then
        printf 'Run %s first.\n' "$BUILD_SCRIPT $missing_services" >&2
        exit 1
    fi

    printf 'Start build now? [y/N]: '
    read -r reply

    case "$reply" in
        y|Y|yes|YES)
            "$BUILD_SCRIPT" $missing_services
            ;;
        *)
            printf 'Startup cancelled.\n' >&2
            exit 1
            ;;
    esac
fi

(cd "$ROOT_DIR" && docker compose up -d --no-build)
(cd "$ROOT_DIR" && docker compose ps)
"$SHOW_URLS_SCRIPT"
