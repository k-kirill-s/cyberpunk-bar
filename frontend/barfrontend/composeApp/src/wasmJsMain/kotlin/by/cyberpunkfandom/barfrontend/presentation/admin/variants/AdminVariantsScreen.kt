package by.cyberpunkfandom.barfrontend.presentation.admin.variants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminContentContainer
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminLoadingState
import by.cyberpunkfandom.barfrontend.presentation.admin.components.VariantsPanel
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdminVariantsScreen(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onCreateVariantRequest: () -> Unit,
    onEditVariantRequest: (String) -> Unit,
) {
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val positionVariants = viewModel.positionVariants.collectAsStateWithLifecycle().value
    val selectedPositionVariantId = viewModel.selectedPositionVariantId.collectAsStateWithLifecycle().value
    val selectedPositionVariantIds = viewModel.selectedPositionVariantIds.collectAsStateWithLifecycle().value
    val selectedPositionVariant = positionVariants.firstOrNull { it.id == selectedPositionVariantId }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Товары",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackRequest,
        )

        AdminContentContainer {
            if (isLoading && positionVariants.isEmpty()) {
                AdminLoadingState(title = "Загружаем товары")
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppTheme.dimensions.basePadding),
                ) {
                    VariantsPanel(
                        positionVariants = positionVariants,
                        selectedPositionVariant = selectedPositionVariant,
                        selectedPositionVariantIds = selectedPositionVariantIds,
                        onPositionVariantClick = viewModel::selectPositionVariant,
                        onOpenCreate = onCreateVariantRequest,
                        onOpenEdit = {
                            selectedPositionVariant?.let { onEditVariantRequest(it.id) }
                        },
                    )
                }
            }
        }
    }
}
