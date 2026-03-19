package by.cyberpunkfandom.barfrontend.presentation.admin.workers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminContentContainer
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminLoadingState
import by.cyberpunkfandom.barfrontend.presentation.admin.components.WorkersPanel
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdminWorkersScreen(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onCreateWorkerRequest: () -> Unit,
    onEditWorkerRequest: (Int) -> Unit,
) {
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val workers = viewModel.workers.collectAsStateWithLifecycle().value
    val selectedWorkerId = viewModel.selectedWorkerId.collectAsStateWithLifecycle().value
    val selectedWorker = workers.firstOrNull { it.id == selectedWorkerId }

    Column(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Стендовики",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackRequest,
        )

        AdminContentContainer {
            if (isLoading && workers.isEmpty()) {
                AdminLoadingState(title = "Загружаем стендовиков")
            } else {
                Column(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxSize()
                        .padding(AppTheme.dimensions.basePadding),
                ) {
                    WorkersPanel(
                        workers = workers,
                        selectedWorker = selectedWorker,
                        onWorkerClick = viewModel::selectWorker,
                        onOpenCreate = onCreateWorkerRequest,
                        onOpenEdit = {
                            selectedWorker?.let { onEditWorkerRequest(it.id) }
                        },
                    )
                }
            }
        }
    }
}
