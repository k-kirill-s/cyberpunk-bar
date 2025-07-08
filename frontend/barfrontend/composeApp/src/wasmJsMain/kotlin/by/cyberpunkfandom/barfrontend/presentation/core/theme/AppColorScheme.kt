package by.cyberpunkfandom.barfrontend.presentation.core.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class AppColorScheme(
    val background: Color,
    val surface: Color,
    val surfaceSelected: Color,
    val text: Color,
    val divider: Color,
    val accent: Color,
    val red: Color,
    val green: Color,
)

internal val LocalAppColorScheme = staticCompositionLocalOf {
    AppColorScheme(
        background = Color(0xFF121417),
        surface = Color(0xFF1C1C1C),
        surfaceSelected = Color(0xFF353535),
        text = Color(0xFFDEDEE6),
        divider = Color(0xFF3C3E45),
        accent = Color(0xFF0F81E6),
        red = Color(0xFFBA3D3D),
        green = Color(0xFF468949),
    )
}
