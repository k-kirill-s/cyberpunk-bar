# syntax=docker/dockerfile:1.7

FROM gradle:8-jdk21 AS deps

WORKDIR /home/gradle/src
ENV GRADLE_USER_HOME=/home/gradle/.gradle

# Copy only Gradle metadata first so dependency resolution stays cached
# when backend sources change.
COPY --chown=gradle:gradle gradlew gradlew.bat settings.gradle.kts build.gradle.kts gradle.properties ./
COPY --chown=gradle:gradle gradle gradle

RUN chmod +x gradlew

RUN --mount=type=cache,id=backend-gradle-cache,target=/home/gradle/.gradle,sharing=locked \
    ./gradlew --no-daemon dependencies --configuration compileClasspath \
    && ./gradlew --no-daemon dependencies --configuration runtimeClasspath

FROM deps AS build

COPY --chown=gradle:gradle src src

RUN --mount=type=cache,id=backend-gradle-cache,target=/home/gradle/.gradle,sharing=locked \
    ./gradlew --no-daemon buildFatJar

FROM amazoncorretto:22 AS runtime

EXPOSE 8080
RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-docker-sample.jar

ENTRYPOINT ["java","-jar","/app/ktor-docker-sample.jar"]
