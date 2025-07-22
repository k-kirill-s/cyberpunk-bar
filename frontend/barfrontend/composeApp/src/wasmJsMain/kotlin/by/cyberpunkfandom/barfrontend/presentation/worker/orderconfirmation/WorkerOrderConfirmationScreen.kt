package by.cyberpunkfandom.barfrontend.presentation.worker.orderconfirmation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBigButton
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun WorkerOrderConfirmationScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onOrderFinished: () -> Unit,
    viewModel: WorkerOrderConfirmationViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onBackRequest.collect {
            onBackRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onOrderFinished.collect {
            onOrderFinished()
        }
    }

    WorkerOrderConfirmationScreen(
        order = viewModel.order.collectAsStateWithLifecycle().value,
        isConfirming = viewModel.isConfirming.collectAsStateWithLifecycle().value,
        onConfirmClick = viewModel::onConfirmClick,
        onCancelClick = viewModel::onCancelClick,
    )
}

@Composable
private fun WorkerOrderConfirmationScreen(
    order: OrderFull?,
    isConfirming: Boolean,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(AppTheme.dimensions.basePadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = order?.let { "Заказ №${it.name} готов" }.orEmpty(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.big,
            )
        }

        Column(modifier = Modifier.padding(horizontal = AppTheme.dimensions.basePadding)) {
            AppBigButton(
                title = "Подтвердить",
                onClick = onConfirmClick,
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.colorScheme.green,
                enabled = order != null && !isConfirming,
                isLoading = isConfirming,
            )

            Spacer(Modifier.height(AppTheme.dimensions.basePadding * 2))

            AppBigButton(
                title = "Вернуться к заказу",
                onClick = onCancelClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = order != null && !isConfirming,
            )
        }

        Spacer(Modifier.weight(1f))
    }
}
