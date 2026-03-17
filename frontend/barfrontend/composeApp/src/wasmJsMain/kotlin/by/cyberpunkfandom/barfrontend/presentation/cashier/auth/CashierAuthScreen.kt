package by.cyberpunkfandom.barfrontend.presentation.cashier.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.domain.Worker
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBigButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppStateMessage
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CashierAuthScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onCashierSelected: (cashierId: Int) -> Unit,
    viewModel: CashierAuthViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onCashierSelected.collect { cashierId ->
            onCashierSelected(cashierId)
        }
    }

    CashierAuthScreen(
        onBackClick = onBackRequest,
        workers = viewModel.workers.collectAsStateWithLifecycle().value,
        isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value,
        onWorkerClick = viewModel::onWorkerClick,
    )
}

@Composable
private fun CashierAuthScreen(
    onBackClick: () -> Unit,
    workers: List<Worker>,
    isLoading: Boolean,
    onWorkerClick: (worker: Worker) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Кто кассир?",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackClick,
        )

        AppHorizontalDivider()

        when {
            isLoading -> {
                AppStateMessage(
                    title = "Загружаем стендовиков",
                    isLoading = true,
                    modifier = Modifier.weight(1f),
                )
            }

            workers.isEmpty() -> {
                AppStateMessage(
                    title = "Стендовики-кассиры не найдены",
                    description = "Добавьте стендовика с ролью кассира в каталоге.",
                    modifier = Modifier.weight(1f),
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(AppTheme.dimensions.basePadding),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
                ) {
                    items(workers) { worker ->
                        AppBigButton(
                            title = buildString {
                                append(worker.name)
                                append(if (worker.isOnLine) " • в сети" else " • офлайн")
                            },
                            onClick = { onWorkerClick(worker) },
                            modifier = Modifier.fillMaxWidth(),
                            color = if (worker.isOnLine) AppTheme.colorScheme.green else AppTheme.colorScheme.surface,
                        )
                    }
                }
            }
        }
    }
}
