package by.cyberpunkfandom.barfrontend.presentation.core.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class AppColorScheme(
    val background: Color,
    val surface: Color,
    val surfaceMuted: Color,
    val surfaceSelected: Color,
    val surfaceOverlay: Color,
    val text: Color,
    val textSecondary: Color,
    val divider: Color,
    val dividerStrong: Color,
    val accent: Color,
    val accentSecondary: Color,
    val accentGlow: Color,
    val warning: Color,
    val error: Color,
    val success: Color,
) {
    val red: Color
        get() = error

    val green: Color
        get() = success
}

internal val cyberpunkAppColorScheme = AppColorScheme(
    background = Color(0xFF070A0F),
    surface = Color(0xFF101722),
    surfaceMuted = Color(0xFF0B1119),
    surfaceSelected = Color(0xFF152331),
    surfaceOverlay = Color(0x263DF2FF),
    text = Color(0xFFE8F7FF),
    textSecondary = Color(0xFF8AA6B8),
    divider = Color(0xFF203040),
    dividerStrong = Color(0xFF3A5A73),
    accent = Color(0xFF3DF2FF),
    accentSecondary = Color(0xFFFF4D6D),
    accentGlow = Color(0xFF7AF7E3),
    warning = Color(0xFFFFB84D),
    error = Color(0xFFFF4D6D),
    success = Color(0xFF7AF7E3),
)

internal val LocalAppColorScheme = staticCompositionLocalOf {
    cyberpunkAppColorScheme
}
