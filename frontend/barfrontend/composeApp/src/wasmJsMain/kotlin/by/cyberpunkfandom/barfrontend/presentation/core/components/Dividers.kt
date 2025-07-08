package by.cyberpunkfandom.barfrontend.presentation.core.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

enum class DividerType { NORMAL, THIN }

@Composable
fun AppHorizontalDivider(
    modifier: Modifier = Modifier,
    type: DividerType = DividerType.NORMAL,
) {
    val thickness = when (type) {
        DividerType.NORMAL -> AppTheme.dimensions.divider
        DividerType.THIN -> AppTheme.dimensions.thinDivider
    }
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = AppTheme.colorScheme.divider,
    )
}

@Composable
fun AppDashedHorizontalDivider(
    modifier: Modifier = Modifier,
    type: DividerType = DividerType.NORMAL,
) {
    val thickness = when (type) {
        DividerType.NORMAL -> AppTheme.dimensions.divider
        DividerType.THIN -> AppTheme.dimensions.thinDivider
    }

    val color = AppTheme.colorScheme.divider

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness),
    ) {
        drawLine(
            color = color,
            strokeWidth = thickness.toPx(),
            pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(10f, 30f)),
            start = Offset(0f, thickness.toPx() / 2),
            end = Offset(size.width, thickness.toPx() / 2),
        )
    }
}

@Composable
fun AppVerticalDivider(
    modifier: Modifier = Modifier,
    type: DividerType = DividerType.NORMAL,
) {
    val thickness = when (type) {
        DividerType.NORMAL -> AppTheme.dimensions.divider
        DividerType.THIN -> AppTheme.dimensions.thinDivider
    }
    VerticalDivider(
        modifier = modifier,
        thickness = thickness,
        color = AppTheme.colorScheme.divider,
    )
}
