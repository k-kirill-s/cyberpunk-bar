package by.cyberpunkfandom.barfrontend.presentation.cashier.createorder

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
internal data class CashierCreateOrderRoute(val orderId: Int)

internal fun NavGraphBuilder.cashierCreateOrderComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onCloseRequest: () -> Unit,
    onAddPositionRequest: () -> Unit,
    onAddPositionExtraRequest: (positionItemId: Int) -> Unit,
    onOrderFormed: () -> Unit,
) {
    composable<CashierCreateOrderRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<CashierCreateOrderRoute>()
        val viewModel = koinViewModel<CashierCreateOrderViewModel>(
            parameters = { parametersOf(route.orderId) }
        )
        CashierCreateOrderScreen(
            onError = onError,
            onCloseRequest = onCloseRequest,
            onAddPositionRequest = onAddPositionRequest,
            onAddPositionExtraRequest = onAddPositionExtraRequest,
            onOrderFormed = onOrderFormed,
            viewModel = viewModel,
        )
    }
}

internal fun NavController.navigateToCashierCreateOrder(
    orderId: Int,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierCreateOrderRoute(
        orderId = orderId,
    )
    navigate(route, builder)
}
