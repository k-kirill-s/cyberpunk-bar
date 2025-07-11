package by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderstarted

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppAlertDialog

@Composable
fun WorkerHomeOrderStartedDialog(
    state: WorkerHomeOrderStartedDialogState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppAlertDialog(
        onDismissRequest = onDismissRequest,
        title = "Ошибка",
        text = "Заказ №${state.orderName} уже начат",
        modifier = modifier,
    )
}
