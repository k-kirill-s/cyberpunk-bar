package by.cyberpunkfandom.barfrontend.presentation.admin.variants

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import kotlinx.serialization.Serializable

@Serializable
data object AdminVariantsRoute

fun NavGraphBuilder.adminVariantsComposable(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onCreateVariantRequest: () -> Unit,
    onEditVariantRequest: (String) -> Unit,
) {
    composable<AdminVariantsRoute> {
        AdminVariantsScreen(
            viewModel = viewModel,
            onBackRequest = onBackRequest,
            onCreateVariantRequest = onCreateVariantRequest,
            onEditVariantRequest = onEditVariantRequest,
        )
    }
}

fun NavController.navigateToAdminVariants(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(AdminVariantsRoute, builder)
}
