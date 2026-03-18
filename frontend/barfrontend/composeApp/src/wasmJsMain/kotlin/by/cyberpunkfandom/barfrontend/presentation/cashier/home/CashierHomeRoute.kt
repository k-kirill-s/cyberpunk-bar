package by.cyberpunkfandom.barfrontend.presentation.cashier.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class CashierHomeRoute(val cashierId: Int)

fun NavGraphBuilder.cashierHomeComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onOpenCreateOrderRequest: (orderId: Int) -> Unit,
    onGiveAwayOrderRequest: () -> Unit,
    onCancelOrderRequest: () -> Unit,
) {
    composable<CashierHomeRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<CashierHomeRoute>()
        CashierHomeScreen(
            onError = onError,
            onBackRequest = onBackRequest,
            onOpenCreateOrderRequest = onOpenCreateOrderRequest,
            onGiveAwayOrderRequest = onGiveAwayOrderRequest,
            onCancelOrderRequest = onCancelOrderRequest,
            viewModel = koinViewModel(
                parameters = { parametersOf(route.cashierId) }
            ),
        )
    }
}

fun NavController.navigateToCashierHome(
    cashierId: Int,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierHomeRoute(cashierId)
    navigate(route, builder)
}
