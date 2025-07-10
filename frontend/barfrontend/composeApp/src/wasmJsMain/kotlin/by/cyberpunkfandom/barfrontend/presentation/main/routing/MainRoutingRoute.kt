package by.cyberpunkfandom.barfrontend.presentation.main.routing

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object MainRoutingRoute

fun NavGraphBuilder.mainRoutingComposable(
    onOpenCashierRequest: () -> Unit,
    onOpenCollectorRequest: () -> Unit,
    onOpenBoardRequest: () -> Unit,
) {
    composable<MainRoutingRoute> { _ ->
        MainRoutingScreen(
            onOpenCashierRequest = onOpenCashierRequest,
            onOpenCollectorRequest = onOpenCollectorRequest,
            onOpenBoardRequest = onOpenBoardRequest,
        )
    }
}

fun NavController.navigateToMainRouting(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = MainRoutingRoute
    navigate(route, builder)
}
