#!/usr/bin/env sh

set -eu

ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
ENV_FILE="$ROOT_DIR/.env"

frontend_port=8043

if [ -f "$ENV_FILE" ]; then
    frontend_port=$(awk -F= '
        $1 == "FRONTEND_PORT" {
            gsub(/^[[:space:]]+|[[:space:]]+$/, "", $2)
            print $2
            exit
        }
    ' "$ENV_FILE")
    frontend_port=${frontend_port:-8043}
fi

detect_lan_ip() {
    if command -v route >/dev/null 2>&1 && command -v ipconfig >/dev/null 2>&1; then
        iface=$(route get default 2>/dev/null | awk "/interface:/{print \$2; exit}")
        if [ -n "${iface:-}" ]; then
            lan_ip=$(ipconfig getifaddr "$iface" 2>/dev/null || true)
            if [ -n "${lan_ip:-}" ]; then
                printf '%s\n' "$lan_ip"
                return
            fi
        fi
    fi

    if command -v ifconfig >/dev/null 2>&1; then
        lan_ip=$(
            ifconfig 2>/dev/null | awk '
                /^[^ \t]/ {
                    active = 0
                    inet_addr = ""
                }
                /inet / && $2 != "127.0.0.1" {
                    inet_addr = $2
                }
                /status: active/ && inet_addr != "" {
                    print inet_addr
                    exit
                }
            '
        )
        if [ -n "${lan_ip:-}" ]; then
            printf '%s\n' "$lan_ip"
            return
        fi
    fi

    if command -v hostname >/dev/null 2>&1; then
        lan_ip=$(hostname -I 2>/dev/null | awk "{print \$1}" || true)
        if [ -n "${lan_ip:-}" ]; then
            printf '%s\n' "$lan_ip"
            return
        fi
    fi

    if command -v ip >/dev/null 2>&1; then
        ip route get 1.1.1.1 2>/dev/null | awk "{print \$7; exit}" || true
    fi
}

lan_ip=$(detect_lan_ip)

printf 'Local app: http://localhost:%s\n' "$frontend_port"

if [ -n "${lan_ip:-}" ]; then
    printf 'LAN app:   http://%s:%s\n' "$lan_ip" "$frontend_port"
else
    printf 'LAN app:   unavailable (could not detect LAN IP)\n'
fi
