package by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.composable.dialogs.orderformed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun CashierCreateOrderOrderFormedDialog(
    state: CashierCreateOrderOrderFormedDialogState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = "ОК",
                    style = AppTheme.typography.title,
                )
            }
        },
        title = {
            Text(
                text = "Заказ сформирован",
                style = AppTheme.typography.title,
            )
        },
        text = {
            Box(
                modifier = modifier
                    .padding(horizontal = 100.dp, vertical = 50.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "№${state.orderName}",
                    style = AppTheme.typography.big,
                )
            }
        },
    )
}
