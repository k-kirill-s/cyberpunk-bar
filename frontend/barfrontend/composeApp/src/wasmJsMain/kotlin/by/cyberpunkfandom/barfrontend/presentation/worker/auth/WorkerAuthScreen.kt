package by.cyberpunkfandom.barfrontend.presentation.worker.auth

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
fun WorkerAuthScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onWorkerSelected: (workerId: Int) -> Unit,
    viewModel: WorkerAuthViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onWorkerSelected.collect { workerId ->
            onWorkerSelected(workerId)
        }
    }

    WorkerAuthScreen(
        onBackClick = onBackRequest,
        workers = viewModel.workers.collectAsStateWithLifecycle().value,
        isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value,
        onWorkerClick = viewModel::onWorkerClick,
    )
}

@Composable
private fun WorkerAuthScreen(
    onBackClick: () -> Unit,
    workers: List<Worker>,
    isLoading: Boolean,
    onWorkerClick: (worker: Worker) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Хто я?",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackClick,
        )

        AppHorizontalDivider()

        when {
            isLoading -> {
                AppStateMessage(
                    title = "Загружаем сотрудников",
                    isLoading = true,
                    modifier = Modifier.weight(1f),
                )
            }

            workers.isEmpty() -> {
                AppStateMessage(
                    title = "Сотрудники не найдены",
                    description = "Добавьте сотрудников в каталоге кассира.",
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
