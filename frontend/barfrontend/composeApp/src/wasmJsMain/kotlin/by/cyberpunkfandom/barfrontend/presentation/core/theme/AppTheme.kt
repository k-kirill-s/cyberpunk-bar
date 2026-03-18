package by.cyberpunkfandom.barfrontend.presentation.core.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.cyberpunkfandom.barfrontend.presentation.core.theme.material.materialThemeColorScheme
import kotlinx.browser.window

private val tabletAppDimensions = AppDimensions(
    topBarHeight = 96.dp,
    bottomBarHeight = 120.dp,
    basePadding = 32.dp,
    iconSize = 32.dp,
    itemHeight = 96.dp,
    bigButtonHeight = 120.dp,
    divider = 4.dp,
    thinDivider = 2.dp,
    cornerRadius = 10.dp,
)

private val phoneAppDimensions = AppDimensions(
    topBarHeight = 64.dp,
    bottomBarHeight = 96.dp,
    basePadding = 24.dp,
    iconSize = 24.dp,
    itemHeight = 64.dp,
    bigButtonHeight = 96.dp,
    divider = 2.dp,
    thinDivider = 1.dp,
    cornerRadius = 10.dp,
)

private val tabletAppTypography = AppTypography(
    displayLarge = Typography().displayLarge.copy(
        fontSize = 140.sp,
        lineHeight = 150.sp,
    ),
    display = Typography().displayLarge.copy(
        fontSize = 110.sp,
        lineHeight = 116.sp,
    ),
    big = Typography().displayMedium,
    title = Typography().headlineLarge,
    body = Typography().headlineSmall,
)

private val phoneAppTypography = AppTypography(
    displayLarge = Typography().displayLarge,
    display = Typography().displayMedium,
    big = Typography().displaySmall,
    title = Typography().headlineSmall,
    body = Typography().labelLarge,
)

@Composable
fun AppTheme(
    isTablet: Boolean? = null,
    content: @Composable () -> Unit,
) {
    val isTabletMode = isTablet ?: (window.innerWidth >= 900)
    val appDimensions = if (isTabletMode) tabletAppDimensions else phoneAppDimensions
    val appTypography = if (isTabletMode) tabletAppTypography else phoneAppTypography

    CompositionLocalProvider(
        LocalAppColorScheme provides cyberpunkAppColorScheme,
        LocalAppDimensions provides appDimensions,
        LocalAppTypography provides appTypography,
        LocalContentColor provides AppTheme.colorScheme.text,
    ) {
        MaterialTheme(
            colorScheme = materialThemeColorScheme,
            typography = Typography(),
            content = content,
        )
    }
}

object AppTheme {

    val colorScheme: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current

    val dimensions: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalAppDimensions.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
}
