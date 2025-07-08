package by.cyberpunkfandom.barfrontend.presentation.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Stable
class AppTypography(
    val title: TextStyle,
    val big: TextStyle,
    val body: TextStyle,
)

internal val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        title = Typography().titleLarge,
        big = Typography().titleLarge,
        body = Typography().titleLarge,
    )
}
