package by.cyberpunkfandom.barfrontend.presentation.worker.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.OrderStatus
import by.cyberpunkfandom.barfrontend.domain.Worker
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBigButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppStateMessage
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderfinished.WorkerHomeOrderFinishedDialog
import by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderfinished.WorkerHomeOrderFinishedDialogState
import by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderstarted.WorkerHomeOrderStartedOrCancelledDialog
import by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderstarted.WorkerHomeOrderStartedOrCancelledDialogState
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkerHomeScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onOrderStarted: (orderId: Int) -> Unit,
    viewModel: WorkerHomeViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onOrderStarted.collect { workerId ->
            onOrderStarted(workerId)
        }
    }

    WorkerAuthScreen(
        onBackClick = onBackRequest,
        worker = viewModel.worker.collectAsStateWithLifecycle().value,
        orderToCollect = viewModel.orderToCollect.collectAsStateWithLifecycle().value,
        isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value,
        isStartOrderLoading = viewModel.isStartOrderLoading.collectAsStateWithLifecycle().value,
        onStartOrderClick = viewModel::onStartOrderClick,
        orderFinishedDialogState = viewModel.orderFinishedDialogState.collectAsStateWithLifecycle().value,
        onOrderFinishedDialogDismissRequest = viewModel::onOrderFinishedDialogDismissRequest,
        orderStartedOrCancelledDialogState = viewModel.orderStartedOrCancelledDialogState.collectAsStateWithLifecycle().value,
        onOrderStartedDialogDismissRequest = viewModel::onOrderStartedDialogDismissRequest,
    )
}

@Composable
private fun WorkerAuthScreen(
    onBackClick: () -> Unit,
    worker: Worker?,
    orderToCollect: OrderFull?,
    isLoading: Boolean,
    isStartOrderLoading: Boolean,
    onStartOrderClick: () -> Unit,
    orderFinishedDialogState: WorkerHomeOrderFinishedDialogState?,
    onOrderFinishedDialogDismissRequest: () -> Unit,
    orderStartedOrCancelledDialogState: WorkerHomeOrderStartedOrCancelledDialogState?,
    onOrderStartedDialogDismissRequest: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = worker?.name.orEmpty(),
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackClick,
        )

        AppHorizontalDivider()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(AppTheme.dimensions.basePadding),
            contentAlignment = Alignment.Center,
        ) {
            val (title, color, enabled) = when {
                isLoading -> Triple("Загружаем очередь", AppTheme.colorScheme.surface, false)
                orderToCollect == null -> Triple("Нет доступных заказов", AppTheme.colorScheme.surface, false)
                orderToCollect.status == OrderStatus.FORMED -> Triple("Взять заказ №${orderToCollect.name}", AppTheme.colorScheme.accent, true)
                else -> Triple("Продолжить заказ №${orderToCollect.name}", AppTheme.colorScheme.accent, true)
            }
            if (isLoading && orderToCollect == null) {
                AppStateMessage(
                    title = title,
                    isLoading = true,
                )
            } else {
                AppBigButton(
                    title = title,
                    onClick = onStartOrderClick,
                    modifier = Modifier.fillMaxWidth(),
                    color = color,
                    enabled = enabled,
                    isLoading = isStartOrderLoading,
                )
            }
        }
    }

    orderFinishedDialogState?.let { state ->
        WorkerHomeOrderFinishedDialog(
            state = state,
            onDismissRequest = onOrderFinishedDialogDismissRequest,
        )
    }

    orderStartedOrCancelledDialogState?.let { state ->
        WorkerHomeOrderStartedOrCancelledDialog(
            state = state,
            onDismissRequest = onOrderStartedDialogDismissRequest,
        )
    }
}
