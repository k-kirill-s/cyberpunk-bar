package by.cyberpunkfandom.barfrontend.presentation.admin.positions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminContentContainer
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminLoadingState
import by.cyberpunkfandom.barfrontend.presentation.admin.components.PositionsPanel
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdminPositionsScreen(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onCreatePositionRequest: () -> Unit,
    onEditPositionRequest: (String) -> Unit,
) {
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val positions = viewModel.positions.collectAsStateWithLifecycle().value
    val selectedPositionId = viewModel.selectedPositionId.collectAsStateWithLifecycle().value
    val selectedPositionVariantIds = viewModel.selectedPositionVariantIds.collectAsStateWithLifecycle().value
    val selectedPosition = positions.firstOrNull { it.id == selectedPositionId }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Напитки",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackRequest,
        )

        AdminContentContainer {
            if (isLoading && positions.isEmpty()) {
                AdminLoadingState(title = "Загружаем напитки")
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppTheme.dimensions.basePadding),
                ) {
                    PositionsPanel(
                        positions = positions,
                        selectedPosition = selectedPosition,
                        linkedProductsCount = selectedPositionVariantIds.size,
                        onPositionClick = viewModel::selectPosition,
                        onOpenCreate = onCreatePositionRequest,
                        onOpenEdit = {
                            selectedPosition?.let { onEditPositionRequest(it.id) }
                        },
                    )
                }
            }
        }
    }
}
