package by.cyberpunkfandom.barfrontend.presentation.core.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun AppCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.accentGlow,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color,
    )
}
