package by.cyberpunkfandom.barfrontend.presentation.admin.positions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminContentContainer
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminLoadingState
import by.cyberpunkfandom.barfrontend.presentation.admin.components.PositionEditorContent
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdminPositionEditorScreen(
    viewModel: AdminCatalogViewModel,
    positionId: String?,
    onBackRequest: () -> Unit,
    onSaved: () -> Unit,
) {
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val isSaving = viewModel.isSaving.collectAsStateWithLifecycle().value
    val positions = viewModel.positions.collectAsStateWithLifecycle().value
    val positionVariants = viewModel.positionVariants.collectAsStateWithLifecycle().value
    val selectedPositionId = viewModel.selectedPositionId.collectAsStateWithLifecycle().value
    val selectedPositionVariantIds = viewModel.selectedPositionVariantIds.collectAsStateWithLifecycle().value
    val position = positionId?.let { currentPositionId ->
        positions.firstOrNull { it.id == currentPositionId }
    }

    LaunchedEffect(positionId) {
        if (positionId != null) {
            viewModel.selectPositionById(positionId)
        }
    }

    val isPreparingSelection = positionId != null && selectedPositionId != positionId && isLoading

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = if (positionId == null) "Новый напиток" else "Напиток",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackRequest,
        )

        AdminContentContainer {
            if (isPreparingSelection) {
                AdminLoadingState(title = "Загружаем напиток")
            } else {
                PositionEditorContent(
                    position = position,
                    availablePositionVariants = positionVariants,
                    initiallySelectedPositionVariantIds = if (positionId != null && selectedPositionId == positionId) {
                        selectedPositionVariantIds
                    } else {
                        emptySet()
                    },
                    isSaving = isSaving,
                    onSave = { name, description, positionVariantIds, onSuccess ->
                        if (positionId == null) {
                            viewModel.createPosition(name, description, positionVariantIds) {
                                onSuccess()
                                onSaved()
                            }
                        } else {
                            viewModel.updatePosition(positionId, name, description, positionVariantIds) {
                                onSuccess()
                                onSaved()
                            }
                        }
                    },
                    onDelete = if (positionId == null) {
                        null
                    } else {
                        {
                            position?.let {
                                viewModel.deletePosition(it.id) {
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
