# Build stage: produce static WASM web assets
FROM gradle:8-jdk21 AS build

WORKDIR /home/gradle/src

# Copy project sources
COPY --chown=gradle:gradle . .

# Build production distribution for Kotlin/Wasm (Compose Multiplatform)
RUN gradle :composeApp:wasmJsBrowserDistribution --no-daemon

# # Runtime stage: serve with nginx
FROM nginx:alpine AS runtime

COPY --from=build /home/gradle/src/composeApp/build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html/

HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost/ > /dev/null || exit 1