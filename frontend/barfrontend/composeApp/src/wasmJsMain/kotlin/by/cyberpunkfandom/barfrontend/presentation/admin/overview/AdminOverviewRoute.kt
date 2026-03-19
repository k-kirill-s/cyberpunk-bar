package by.cyberpunkfandom.barfrontend.presentation.admin.overview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import kotlinx.serialization.Serializable

@Serializable
data object AdminOverviewRoute

fun NavGraphBuilder.adminOverviewComposable(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
    onLogoutRequest: () -> Unit,
    onOpenAnalyticsRequest: () -> Unit,
    onOpenWorkersRequest: () -> Unit,
    onOpenPositionsRequest: () -> Unit,
    onOpenVariantsRequest: () -> Unit,
) {
    composable<AdminOverviewRoute> {
        AdminOverviewScreen(
            viewModel = viewModel,
            onBackRequest = onBackRequest,
            onLogoutRequest = onLogoutRequest,
            onOpenAnalyticsRequest = onOpenAnalyticsRequest,
            onOpenWorkersRequest = onOpenWorkersRequest,
            onOpenPositionsRequest = onOpenPositionsRequest,
            onOpenVariantsRequest = onOpenVariantsRequest,
        )
    }
}

fun NavController.navigateToAdminOverview(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(AdminOverviewRoute, builder)
}
