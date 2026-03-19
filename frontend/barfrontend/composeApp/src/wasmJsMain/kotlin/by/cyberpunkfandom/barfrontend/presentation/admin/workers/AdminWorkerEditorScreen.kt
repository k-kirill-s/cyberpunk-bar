package by.cyberpunkfandom.barfrontend.presentation.admin.workers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminContentContainer
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminLoadingState
import by.cyberpunkfandom.barfrontend.presentation.admin.components.WorkerEditorContent
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdminWorkerEditorScreen(
    viewModel: AdminCatalogViewModel,
    workerId: Int?,
    onBackRequest: () -> Unit,
    onSaved: () -> Unit,
) {
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val isSaving = viewModel.isSaving.collectAsStateWithLifecycle().value
    val workers = viewModel.workers.collectAsStateWithLifecycle().value
    val worker = workerId?.let { currentWorkerId ->
        workers.firstOrNull { it.id == currentWorkerId }
    }

    LaunchedEffect(workerId) {
        viewModel.selectWorkerById(workerId)
    }

    Column(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
        AppTopBar(
            title = if (workerId == null) "Новый стендовик" else "Стендовик",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackRequest,
        )

        AdminContentContainer {
            if (workerId != null && worker == null && isLoading) {
                AdminLoadingState(title = "Загружаем стендовика")
            } else {
                WorkerEditorContent(
                    worker = worker,
                    isSaving = isSaving,
                    onSave = { name, isOnLine, canBeCashier, canBeBartender, onSuccess ->
                        if (workerId == null) {
                            viewModel.createWorker(name, canBeCashier, canBeBartender) {
                                onSuccess()
                                onSaved()
                            }
                        } else {
                            viewModel.updateWorker(workerId, name, isOnLine, canBeCashier, canBeBartender) {
                                onSuccess()
                                onSaved()
                            }
                        }
                    },
                    onDelete = if (workerId == null) {
                        null
                    } else {
                        {
                            worker?.let {
                                viewModel.deleteWorker(it.id) {
                                    onSaved()
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}
