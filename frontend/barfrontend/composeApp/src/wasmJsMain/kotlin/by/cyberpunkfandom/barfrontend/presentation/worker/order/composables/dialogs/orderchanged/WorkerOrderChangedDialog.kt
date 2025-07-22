package by.cyberpunkfandom.barfrontend.presentation.worker.order.composables.dialogs.orderchanged

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppAlertDialog

@Composable
fun WorkerOrderChangedDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppAlertDialog(
        onDismissRequest = onDismissRequest,
        title = "Заказ изменен",
        text = "Возможно, заказ отменен",
        modifier = modifier,
        confirmButtonText = "Выйти",
    )
}
