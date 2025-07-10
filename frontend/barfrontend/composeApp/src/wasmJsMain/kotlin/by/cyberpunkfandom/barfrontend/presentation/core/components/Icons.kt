package by.cyberpunkfandom.barfrontend.presentation.core.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun AppIconButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        AppIcon(painter = painter)
    }
}

@Composable
fun AppIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        painter = painter,
        contentDescription = null,
        modifier = modifier.size(AppTheme.dimensions.iconSize),
        tint = tint,
    )
}
