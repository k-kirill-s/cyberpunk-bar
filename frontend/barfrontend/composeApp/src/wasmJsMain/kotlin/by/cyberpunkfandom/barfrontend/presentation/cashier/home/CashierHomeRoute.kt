package by.cyberpunkfandom.barfrontend.presentation.cashier.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object CashierHomeRoute

fun NavGraphBuilder.cashierHomeComposable(
    onBackRequest: () -> Unit,
    onOpenCreateOrderRequest: (orderId: Int) -> Unit,
    onGiveAwayOrderRequest: () -> Unit,
    onCancelOrderRequest: () -> Unit,
    onTogglePositionsRequest: () -> Unit,
    onToggleExtraRequest: () -> Unit,
) {
    composable<CashierHomeRoute> { _ ->
        CashierHomeScreen(
            onBackRequest = onBackRequest,
            onOpenCreateOrderRequest = onOpenCreateOrderRequest,
            onGiveAwayOrderRequest = onGiveAwayOrderRequest,
            onCancelOrderRequest = onCancelOrderRequest,
            onTogglePositionsRequest = onTogglePositionsRequest,
            onToggleExtraRequest = onToggleExtraRequest,
        )
    }
}

fun NavController.navigateToCashierHome(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierHomeRoute
    navigate(route, builder)
}
