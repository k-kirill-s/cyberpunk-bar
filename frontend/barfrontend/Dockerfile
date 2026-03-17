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

RUN --mount=type=cache,target=/home/gradle/.gradle \
    --mount=type=cache,target=/home/gradle/.konan \
    --mount=type=cache,target=/home/gradle/.npm \
    --mount=type=cache,target=/home/gradle/src/kotlin-js-store \
    gradle kotlinNpmInstall kotlinBinaryenSetup --no-daemon

FROM deps AS build

# Copy project sources after the dependency/toolchain layer.
COPY --chown=gradle:gradle . .

# Build production distribution for Kotlin/Wasm (Compose Multiplatform).
RUN --mount=type=cache,target=/home/gradle/.gradle \
    --mount=type=cache,target=/home/gradle/.konan \
    --mount=type=cache,target=/home/gradle/.npm \
    --mount=type=cache,target=/home/gradle/src/kotlin-js-store \
    gradle :composeApp:wasmJsBrowserDistribution --no-daemon

# # Runtime stage: serve with nginx
FROM nginx:alpine AS runtime

COPY --from=build /home/gradle/src/composeApp/build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/conf.d/default.conf

HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost/ > /dev/null || exit 1
