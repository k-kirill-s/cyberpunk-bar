#!/usr/bin/env sh

set -eu

ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)

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

printf 'Building images for: %s\n' "$*"
(cd "$ROOT_DIR" && docker compose build "$@")

printf 'Build completed for: %s\n' "$*"
