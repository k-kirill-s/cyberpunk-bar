package by.cyberpunkfandom.barfrontend.presentation.admin.variants

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import kotlinx.serialization.Serializable

@Serializable
data class AdminVariantEditorRoute(val variantId: String? = null)

fun NavGraphBuilder.adminVariantEditorComposable(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onSaved: () -> Unit,
) {
    composable<AdminVariantEditorRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<AdminVariantEditorRoute>()
        AdminVariantEditorScreen(
            viewModel = viewModel,
            variantId = route.variantId,
            onBackRequest = onBackRequest,
            onSaved = onSaved,
        )
    }
}

fun NavController.navigateToAdminVariantEditor(
    variantId: String? = null,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(AdminVariantEditorRoute(variantId), builder)
}
