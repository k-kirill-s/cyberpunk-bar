package by.cyberpunkfandom.barfrontend.presentation.admin.positions

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import kotlinx.serialization.Serializable

@Serializable
data object AdminPositionsRoute

fun NavGraphBuilder.adminPositionsComposable(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onCreatePositionRequest: () -> Unit,
    onEditPositionRequest: (String) -> Unit,
) {
    composable<AdminPositionsRoute> {
        AdminPositionsScreen(
            viewModel = viewModel,
            onBackRequest = onBackRequest,
            onCreatePositionRequest = onCreatePositionRequest,
            onEditPositionRequest = onEditPositionRequest,
        )
    }
}

fun NavController.navigateToAdminPositions(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(AdminPositionsRoute, builder)
}
