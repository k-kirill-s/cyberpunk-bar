This is a Kotlin Multiplatform project targeting Web.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.

## Docker

Build and run the production bundle with Docker:

1. Build image
   - `docker build -t bar-frontend:latest .`
2. Run container
   - `docker run --rm -p 8080:80 bar-frontend:latest`

Or use docker compose from the `frontend` folder:

- `docker compose up --build`

The app will be available at http://localhost:8080/.

Note: The backend host/port is currently hardcoded in `composeApp/src/wasmJsMain/kotlin/by/cyberpunkfandom/barfrontend/data/di/DataNetworkModule.kt`. Adjust it as needed for your environment.
