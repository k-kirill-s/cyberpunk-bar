package by.cyberpunkfandom.barfrontend.presentation.admin.workers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import kotlinx.serialization.Serializable

@Serializable
data class AdminWorkerEditorRoute(val workerId: Int? = null)

fun NavGraphBuilder.adminWorkerEditorComposable(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onSaved: () -> Unit,
) {
    composable<AdminWorkerEditorRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<AdminWorkerEditorRoute>()
        AdminWorkerEditorScreen(
            viewModel = viewModel,
            workerId = route.workerId,
            onBackRequest = onBackRequest,
            onSaved = onSaved,
        )
    }
}

fun NavController.navigateToAdminWorkerEditor(
    workerId: Int? = null,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(AdminWorkerEditorRoute(workerId), builder)
}
