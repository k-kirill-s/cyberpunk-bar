#!/usr/bin/env python3
from __future__ import annotations

import http.client
import io
import os
import pathlib
import shutil
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import urlsplit


ROOT = pathlib.Path(__file__).resolve().parents[1]
DIST_DIR = ROOT / "frontend" / "barfrontend" / "composeApp" / "build" / "dist" / "wasmJs" / "developmentExecutable"
BACKEND_HOST = os.environ.get("BACKEND_HOST", "127.0.0.1")
BACKEND_PORT = int(os.environ.get("BACKEND_PORT", "8042"))
PORT = int(os.environ.get("PORT", "8050"))


class FrontendHandler(SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=str(DIST_DIR), **kwargs)

    def do_GET(self):
        if self.path.startswith("/api/"):
            self._proxy()
            return
        return super().do_GET()

    def do_POST(self):
        self._proxy()

    def do_PUT(self):
        self._proxy()

    def do_PATCH(self):
        self._proxy()

    def do_DELETE(self):
        self._proxy()

    def do_OPTIONS(self):
        self._proxy()

    def translate_path(self, path: str) -> str:
        translated = super().translate_path(path)
        requested = pathlib.Path(translated)
        if requested.exists():
            return translated
        # Serve the SPA entrypoint for client-side routes.
        return str(DIST_DIR / "index.html")

    def _proxy(self):
        url = urlsplit(self.path)
        body = None
        length = self.headers.get("Content-Length")
        if length:
            body = self.rfile.read(int(length))

        headers = {k: v for k, v in self.headers.items() if k.lower() != "host"}
        conn = http.client.HTTPConnection(BACKEND_HOST, BACKEND_PORT, timeout=30)
        try:
            conn.request(self.command, self.path, body=body, headers=headers)
            resp = conn.getresponse()
            self.send_response(resp.status, resp.reason)
            for key, value in resp.getheaders():
                lower = key.lower()
                if lower in {"transfer-encoding", "connection", "keep-alive"}:
                    continue
                self.send_header(key, value)
            self.end_headers()
            shutil.copyfileobj(resp, self.wfile)
        finally:
            conn.close()


def main():
    if not DIST_DIR.exists():
        raise SystemExit(f"Missing frontend dist: {DIST_DIR}")
    server = ThreadingHTTPServer(("0.0.0.0", PORT), FrontendHandler)
    print(f"Serving {DIST_DIR} on http://127.0.0.1:{PORT} with /api -> http://{BACKEND_HOST}:{BACKEND_PORT}", flush=True)
    server.serve_forever()


if __name__ == "__main__":
    main()
