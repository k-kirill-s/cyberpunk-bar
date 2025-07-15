package by.cyberpunkfandom.barfrontend.presentation.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Stable
class AppTypography(
    val displayLarge: TextStyle,
    val display: TextStyle,
    val big: TextStyle,
    val title: TextStyle,
    val body: TextStyle,
)

internal val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        displayLarge = Typography().titleLarge,
        display = Typography().titleLarge,
        big = Typography().titleLarge,
        title = Typography().titleLarge,
        body = Typography().titleLarge,
    )
}
