package by.cyberpunkfandom.barfrontend.presentation.worker.order.composables.dialogs.close

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppAlertDialog

@Composable
fun WorkerOrderCloseDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppAlertDialog(
        onDismissRequest = onDismissRequest,
        title = "Заказ в процессе",
        text = "Вы действительно хотите прекратить собирать заказ?",
        modifier = modifier,
        confirmButtonText = "Да, прекратить заказ",
        onConfirmButtonClick = onConfirmClick,
        dismissButtonText = "Нет",
    )
}
