package by.cyberpunkfandom.barfrontend.presentation.core.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun AppCircularProgressIndicator(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = AppTheme.colorScheme.text,
    )
}
