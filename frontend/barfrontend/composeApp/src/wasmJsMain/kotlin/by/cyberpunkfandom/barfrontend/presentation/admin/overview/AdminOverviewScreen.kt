package by.cyberpunkfandom.barfrontend.presentation.admin.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminContentContainer
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminLoadingState
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminOverviewContent
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdminOverviewScreen(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onLogoutRequest: () -> Unit,
    onOpenAnalyticsRequest: () -> Unit,
    onOpenWorkersRequest: () -> Unit,
    onOpenPositionsRequest: () -> Unit,
    onOpenVariantsRequest: () -> Unit,
) {
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val workers = viewModel.workers.collectAsStateWithLifecycle().value
    val positions = viewModel.positions.collectAsStateWithLifecycle().value
    val positionVariants = viewModel.positionVariants.collectAsStateWithLifecycle().value
    val selectedWorkerId = viewModel.selectedWorkerId.collectAsStateWithLifecycle().value
    val selectedPositionId = viewModel.selectedPositionId.collectAsStateWithLifecycle().value
    val selectedPositionVariantIds = viewModel.selectedPositionVariantIds.collectAsStateWithLifecycle().value
    val selectedPositionVariantId = viewModel.selectedPositionVariantId.collectAsStateWithLifecycle().value

    val selectedWorker = workers.firstOrNull { it.id == selectedWorkerId }
    val selectedPosition = positions.firstOrNull { it.id == selectedPositionId }
    val selectedPositionVariant = positionVariants.firstOrNull { it.id == selectedPositionVariantId }

    Column(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Каталог и команда",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackRequest,
        )

        AdminContentContainer {
            if (isLoading && workers.isEmpty() && positions.isEmpty() && positionVariants.isEmpty()) {
                AdminLoadingState(title = "Загружаем админку")
            } else {
                AdminOverviewContent(
                    workers = workers,
                    selectedWorker = selectedWorker,
                    positions = positions,
                    selectedPosition = selectedPosition,
                    selectedPositionVariantIds = selectedPositionVariantIds,
                    positionVariants = positionVariants,
                    selectedPositionVariant = selectedPositionVariant,
                    onOpenAnalyticsRequest = onOpenAnalyticsRequest,
                    onOpenWorkersRequest = onOpenWorkersRequest,
                    onOpenPositionsRequest = onOpenPositionsRequest,
                    onOpenVariantsRequest = onOpenVariantsRequest,
                    onLogoutRequest = onLogoutRequest,
                )
            }
        }
    }
}
