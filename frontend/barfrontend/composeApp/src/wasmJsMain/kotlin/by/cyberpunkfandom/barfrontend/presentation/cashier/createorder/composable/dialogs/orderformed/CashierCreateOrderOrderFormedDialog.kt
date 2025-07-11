package by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.composable.dialogs.orderformed

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppAlertDialog
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun CashierCreateOrderOrderFormedDialog(
    state: CashierCreateOrderOrderFormedDialogState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppAlertDialog(
        onDismissRequest = onDismissRequest,
        title = "Заказ сформирован",
        text = "№${state.orderName}",
        modifier = modifier,
        textStyle = AppTheme.typography.big,
    )
}
