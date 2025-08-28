# Build stage: produce static WASM web assets
FROM gradle:8-jdk21 AS build

WORKDIR /home/gradle/src

# Copy project sources
COPY --chown=gradle:gradle . .

# Build production distribution for Kotlin/Wasm (Compose Multiplatform)
RUN gradle :composeApp:wasmJsBrowserDistribution --no-daemon

# Collect the built distribution into a stable path
RUN set -eux; \
    DIST_DIR=$(find composeApp/build -type d -name productionExecutable -path '*/wasmJs/*' -print -quit); \
    echo "Using distribution directory: $DIST_DIR"; \
    mkdir -p /dist; \
    cp -r "$DIST_DIR"/* /dist/

# Runtime stage: serve with nginx
FROM nginx:alpine AS runtime

EXPOSE 8080

COPY --from=build /dist/ /usr/share/nginx/html/

HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost/ > /dev/null || exit 1
