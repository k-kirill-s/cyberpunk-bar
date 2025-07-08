package by.cyberpunkfandom.barfrontend.presentation.core.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
class AppDimensions(
    val topBarHeight: Dp,
    val bottomBarHeight: Dp,
    val basePadding: Dp,
    val iconSize: Dp,
    val itemHeight: Dp,
    val bigButtonHeight: Dp,
    val divider: Dp,
    val thinDivider: Dp,
    val cornerRadius: Dp,
)

internal val LocalAppDimensions = staticCompositionLocalOf {
    AppDimensions(
        topBarHeight = 0.dp,
        bottomBarHeight = 0.dp,
        basePadding = 0.dp,
        iconSize = 0.dp,
        itemHeight = 0.dp,
        bigButtonHeight = 0.dp,
        divider = 0.dp,
        thinDivider = 0.dp,
        cornerRadius = 0.dp,
    )
}
