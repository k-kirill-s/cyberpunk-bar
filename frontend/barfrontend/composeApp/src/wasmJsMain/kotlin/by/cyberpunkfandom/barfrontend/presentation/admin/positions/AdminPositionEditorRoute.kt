package by.cyberpunkfandom.barfrontend.presentation.admin.positions

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import kotlinx.serialization.Serializable

@Serializable
data class AdminPositionEditorRoute(val positionId: String? = null)

fun NavGraphBuilder.adminPositionEditorComposable(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onSaved: () -> Unit,
) {
    composable<AdminPositionEditorRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<AdminPositionEditorRoute>()
        AdminPositionEditorScreen(
            viewModel = viewModel,
            positionId = route.positionId,
            onBackRequest = onBackRequest,
            onSaved = onSaved,
        )
    }
}

fun NavController.navigateToAdminPositionEditor(
    positionId: String? = null,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(AdminPositionEditorRoute(positionId), builder)
}
