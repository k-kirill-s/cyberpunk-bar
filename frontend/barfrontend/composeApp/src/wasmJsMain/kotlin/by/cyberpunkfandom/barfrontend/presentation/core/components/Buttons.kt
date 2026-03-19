package by.cyberpunkfandom.barfrontend.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
    AppButtonContainer(
        modifier = modifier.height(AppTheme.dimensions.bigButtonHeight),
        onClick = onClick,
        color = color,
        enabled = enabled,
        isLoading = isLoading,
    ) { contentColor ->
        if (isLoading) {
            AppCircularProgressIndicator(color = contentColor)
        } else {
            Text(
                text = title,
                style = AppTheme.typography.title.copy(color = contentColor),
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
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    AppButtonContainer(
        modifier = modifier,
        onClick = onClick,
        color = color,
        enabled = enabled,
        isLoading = isLoading,
    ) { contentColor ->
        if (isLoading) {
            AppCircularProgressIndicator(color = contentColor)
        } else {
            Text(
                text = title,
                style = AppTheme.typography.title.copy(color = contentColor),
            )
        }
    }
}

@Composable
private fun AppButtonContainer(
    modifier: Modifier,
    onClick: () -> Unit,
    color: Color,
    enabled: Boolean,
    isLoading: Boolean,
    content: @Composable (Color) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val containerColor = when {
        !enabled -> AppTheme.colorScheme.surfaceSelected.copy(alpha = 0.42f)
        isPressed -> color.copy(alpha = 0.26f)
        isFocused || isHovered -> color.copy(alpha = 0.20f)
        else -> color.copy(alpha = 0.14f)
    }
    val borderColor = when {
        !enabled -> AppTheme.colorScheme.divider
        isPressed -> AppTheme.colorScheme.accentGlow
        isFocused || isHovered -> color
        else -> color.copy(alpha = 0.78f)
    }
    val contentColor = if (enabled) AppTheme.colorScheme.text else AppTheme.colorScheme.textSecondary

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier = modifier
                .alpha(if (enabled) 1f else 0.72f)
                .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
                .background(containerColor)
                .border(
                    width = AppTheme.dimensions.thinDivider * 2,
                    color = borderColor,
                    shape = RoundedCornerShape(AppTheme.dimensions.cornerRadius),
                )
                .hoverable(interactionSource = interactionSource, enabled = enabled && !isLoading)
                .focusable(interactionSource = interactionSource, enabled = enabled && !isLoading)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled && !isLoading,
                    onClick = onClick,
                )
                .padding(AppTheme.dimensions.basePadding),
            contentAlignment = Alignment.Center,
        ) {
            content(contentColor)
        }
    }
}
