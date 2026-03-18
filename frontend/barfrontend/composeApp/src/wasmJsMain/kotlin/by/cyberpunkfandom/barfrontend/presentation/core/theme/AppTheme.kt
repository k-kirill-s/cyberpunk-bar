package by.cyberpunkfandom.barfrontend.presentation.core.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

private val appFontFamily = FontFamily.SansSerif

private fun appTextStyle(
    fontSize: androidx.compose.ui.unit.TextUnit,
    lineHeight: androidx.compose.ui.unit.TextUnit,
    fontWeight: FontWeight = FontWeight.Normal,
) = TextStyle(
    fontFamily = appFontFamily,
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
)

private val tabletMaterialTypography = Typography(
    displayLarge = appTextStyle(
        fontSize = 140.sp,
        lineHeight = 150.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    displayMedium = appTextStyle(
        fontSize = 110.sp,
        lineHeight = 116.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    displaySmall = appTextStyle(
        fontSize = 68.sp,
        lineHeight = 76.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineLarge = appTextStyle(
        fontSize = 48.sp,
        lineHeight = 56.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineMedium = appTextStyle(
        fontSize = 40.sp,
        lineHeight = 48.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineSmall = appTextStyle(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Medium,
    ),
    titleLarge = appTextStyle(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Medium,
    ),
    bodyLarge = appTextStyle(
        fontSize = 22.sp,
        lineHeight = 30.sp,
    ),
    labelLarge = appTextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Medium,
    ),
)

private val phoneMaterialTypography = Typography(
    displayLarge = appTextStyle(
        fontSize = 72.sp,
        lineHeight = 80.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    displayMedium = appTextStyle(
        fontSize = 54.sp,
        lineHeight = 60.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    displaySmall = appTextStyle(
        fontSize = 40.sp,
        lineHeight = 46.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineLarge = appTextStyle(
        fontSize = 32.sp,
        lineHeight = 38.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineMedium = appTextStyle(
        fontSize = 28.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineSmall = appTextStyle(
        fontSize = 24.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.Medium,
    ),
    titleLarge = appTextStyle(
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Medium,
    ),
    bodyLarge = appTextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    labelLarge = appTextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Medium,
    ),
)

private val tabletAppTypography = AppTypography(
    displayLarge = tabletMaterialTypography.displayLarge,
    display = tabletMaterialTypography.displayMedium,
    big = tabletMaterialTypography.displaySmall,
    title = tabletMaterialTypography.headlineSmall,
    body = tabletMaterialTypography.bodyLarge,
)

private val phoneAppTypography = AppTypography(
    displayLarge = phoneMaterialTypography.displayLarge,
    display = phoneMaterialTypography.displayMedium,
    big = phoneMaterialTypography.displaySmall,
    title = phoneMaterialTypography.headlineSmall,
    body = phoneMaterialTypography.bodyLarge,
)

@Composable
fun AppTheme(
    isTablet: Boolean? = null,
    content: @Composable () -> Unit,
) {
    val isTabletMode = isTablet ?: (window.innerWidth >= 900)
    val appDimensions = if (isTabletMode) tabletAppDimensions else phoneAppDimensions
    val materialTypography = if (isTabletMode) tabletMaterialTypography else phoneMaterialTypography
    val appTypography = if (isTabletMode) tabletAppTypography else phoneAppTypography

    CompositionLocalProvider(
        LocalAppColorScheme provides cyberpunkAppColorScheme,
        LocalAppDimensions provides appDimensions,
        LocalAppTypography provides appTypography,
        LocalContentColor provides AppTheme.colorScheme.text,
    ) {
        MaterialTheme(
            colorScheme = materialThemeColorScheme,
            typography = materialTypography,
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
