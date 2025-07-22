package by.cyberpunkfandom.barfrontend.presentation.cashier.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import kotlinx.serialization.Serializable

@Serializable
data object CashierHomeRoute

fun NavGraphBuilder.cashierHomeComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onOpenCreateOrderRequest: (orderId: Int) -> Unit,
    onGiveAwayOrderRequest: () -> Unit,
    onCancelOrderRequest: () -> Unit,
    onTogglePositionsRequest: () -> Unit,
    onToggleExtraRequest: () -> Unit,
) {
    composable<CashierHomeRoute> { _ ->
        CashierHomeScreen(
            onError = onError,
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
