package by.cyberpunkfandom.barfrontend.presentation.admin.workers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import kotlinx.serialization.Serializable

@Serializable
data object AdminWorkersRoute

fun NavGraphBuilder.adminWorkersComposable(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onCreateWorkerRequest: () -> Unit,
    onEditWorkerRequest: (Int) -> Unit,
) {
    composable<AdminWorkersRoute> {
        AdminWorkersScreen(
            viewModel = viewModel,
            onBackRequest = onBackRequest,
            onCreateWorkerRequest = onCreateWorkerRequest,
            onEditWorkerRequest = onEditWorkerRequest,
        )
    }
}

fun NavController.navigateToAdminWorkers(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(AdminWorkersRoute, builder)
}
