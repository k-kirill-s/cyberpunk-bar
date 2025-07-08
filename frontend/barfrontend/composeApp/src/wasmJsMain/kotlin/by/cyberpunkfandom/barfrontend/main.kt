package by.cyberpunkfandom.barfrontend

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import by.cyberpunkfandom.barfrontend.di.appModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.browser.document
import org.koin.compose.KoinApplication

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    Napier.base(DebugAntilog())
    ComposeViewport(document.body!!) {
        KoinApplication(application = { modules(appModule) }) {
            App()
        }
    }
}
