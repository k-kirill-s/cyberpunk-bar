# syntax=docker/dockerfile:1.7

# Build on amd64 because the Kotlin/Wasm Binaryen download used by the
# distribution task is not available for linux/arm64.
FROM --platform=linux/amd64 gradle:8-jdk21 AS deps

WORKDIR /home/gradle/src
ENV GRADLE_USER_HOME=/home/gradle/.gradle
ENV KONAN_DATA_DIR=/home/gradle/.konan

# Copy only build configuration first so dependency/toolchain setup stays cached
# when application sources change.
COPY --chown=gradle:gradle settings.gradle.kts build.gradle.kts gradle.properties ./
COPY --chown=gradle:gradle gradle/libs.versions.toml gradle/libs.versions.toml
COPY --chown=gradle:gradle composeApp/build.gradle.kts composeApp/build.gradle.kts

RUN mkdir -p composeApp

RUN --mount=type=cache,id=frontend-gradle-cache,target=/home/gradle/.gradle,sharing=locked \
    --mount=type=cache,id=frontend-konan-cache,target=/home/gradle/.konan,sharing=locked \
    --mount=type=cache,id=frontend-npm-cache,target=/home/gradle/.npm,sharing=locked \
    --mount=type=cache,id=frontend-kotlin-js-store-cache,target=/home/gradle/src/kotlin-js-store,sharing=locked \
    gradle kotlinNpmInstall kotlinBinaryenSetup --no-daemon

FROM deps AS build

# Copy project sources after the dependency/toolchain layer.
COPY --chown=gradle:gradle . .

# Build production distribution for Kotlin/Wasm (Compose Multiplatform).
# `compileProductionExecutableKotlinWasmJsOptimize` hangs under Docker's
# linux/amd64 emulation on Apple Silicon, while the distribution itself
# builds and runs correctly without that final Binaryen optimization pass.
RUN --mount=type=cache,id=frontend-gradle-cache,target=/home/gradle/.gradle,sharing=locked \
    --mount=type=cache,id=frontend-konan-cache,target=/home/gradle/.konan,sharing=locked \
    --mount=type=cache,id=frontend-npm-cache,target=/home/gradle/.npm,sharing=locked \
    --mount=type=cache,id=frontend-kotlin-js-store-cache,target=/home/gradle/src/kotlin-js-store,sharing=locked \
    gradle :composeApp:wasmJsBrowserDistribution \
      -x :composeApp:compileProductionExecutableKotlinWasmJsOptimize \
      --no-daemon

# # Runtime stage: serve with nginx
FROM nginx:alpine AS runtime

COPY --from=build /home/gradle/src/composeApp/build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/conf.d/default.conf

HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://127.0.0.1/ > /dev/null || exit 1
