package by.cyberpunkfandom.barfrontend.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun AppBigButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.surface,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    Box(
        modifier = modifier
            .height(AppTheme.dimensions.bigButtonHeight)
            .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
            .background(color)
            .clickable(enabled = enabled && !isLoading, onClick = onClick)
            .padding(AppTheme.dimensions.basePadding),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            AppCircularProgressIndicator()
        } else {
            Text(
                text = title,
                style = AppTheme.typography.title,
            )
        }
    }
}

@Composable
fun AppBoxButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.accent,
    isLoading: Boolean = false,
) {
    Box(
        modifier = modifier
            .background(color)
            .clickable(
                enabled = !isLoading,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            AppCircularProgressIndicator()
        } else {
            Text(
                text = title,
                style = AppTheme.typography.title,
            )
        }
    }
}
