package by.cyberpunkfandom.barfrontend.presentation.admin.analytics

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import kotlinx.serialization.Serializable

@Serializable
data object AdminAnalyticsRoute

fun NavGraphBuilder.adminAnalyticsComposable(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
) {
    composable<AdminAnalyticsRoute> {
        AdminAnalyticsScreen(
            viewModel = viewModel,
            onBackRequest = onBackRequest,
        )
    }
}

fun NavController.navigateToAdminAnalytics(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(AdminAnalyticsRoute, builder)
}
