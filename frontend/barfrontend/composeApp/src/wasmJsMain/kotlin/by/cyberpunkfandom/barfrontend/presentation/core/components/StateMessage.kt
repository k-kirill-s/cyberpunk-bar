package by.cyberpunkfandom.barfrontend.presentation.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun AppStateMessage(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppTheme.dimensions.basePadding * 2),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (isLoading) {
            AppCircularProgressIndicator()
        }

        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = AppTheme.typography.title,
        )

        description?.takeIf { it.isNotBlank() }?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = AppTheme.dimensions.basePadding),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.body.copy(color = AppTheme.colorScheme.textSecondary),
            )
        }
    }
}
