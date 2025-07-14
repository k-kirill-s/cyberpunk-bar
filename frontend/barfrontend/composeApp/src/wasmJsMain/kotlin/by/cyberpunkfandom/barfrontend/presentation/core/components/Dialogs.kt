package by.cyberpunkfandom.barfrontend.presentation.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun AppAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = AppTheme.typography.title,
    confirmButtonText: String = "ОК",
    onConfirmButtonClick: () -> Unit = onDismissRequest,
    dismissButtonText: String? = null,
    onDismissButtonClick: () -> Unit = onDismissRequest,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
                Text(
                    text = confirmButtonText,
                    style = AppTheme.typography.title,
                )
            }
        },
        dismissButton = dismissButtonText?.let {
            {
                TextButton(onClick = onDismissButtonClick) {
                    Text(
                        text = dismissButtonText,
                        style = AppTheme.typography.title,
                    )
                }
            }
        },
        title = {
            Text(
                text = title,
                style = AppTheme.typography.title,
            )
        },
        text = {
            Box(
                modifier = modifier
                    .padding(horizontal = 60.dp, vertical = 30.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = text,
                    style = textStyle,
                )
            }
        },
    )
}
