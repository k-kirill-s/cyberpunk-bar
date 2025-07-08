package by.cyberpunkfandom.barfrontend

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import by.cyberpunkfandom.barfrontend.presentation.main.MainScreen

@Composable
fun App() {
    AppTheme {
        Scaffold { _ ->
            MainScreen()
        }
    }
}
