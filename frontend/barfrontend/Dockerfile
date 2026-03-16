# Build on amd64 because the Kotlin/Wasm Binaryen download used by the
# distribution task is not available for linux/arm64.
FROM --platform=linux/amd64 gradle:8-jdk21 AS build

WORKDIR /home/gradle/src

# Copy project sources
COPY --chown=gradle:gradle . .

# Build production distribution for Kotlin/Wasm (Compose Multiplatform)
RUN gradle :composeApp:wasmJsBrowserDistribution --no-daemon

# # Runtime stage: serve with nginx
FROM nginx:alpine AS runtime

COPY --from=build /home/gradle/src/composeApp/build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/conf.d/default.conf

HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost/ > /dev/null || exit 1
