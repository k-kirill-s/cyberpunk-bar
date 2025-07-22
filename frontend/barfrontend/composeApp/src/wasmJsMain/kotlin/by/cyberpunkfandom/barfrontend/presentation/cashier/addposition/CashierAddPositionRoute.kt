package by.cyberpunkfandom.barfrontend.presentation.cashier.addposition

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
internal data class CashierAddPositionRoute(val orderId: Int)

internal fun NavGraphBuilder.cashierAddPositionComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onPositionItemAdded: (positionItemId: Int) -> Unit,
) {
    composable<CashierAddPositionRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<CashierAddPositionRoute>()
        val viewModel = koinViewModel<CashierAddPositionViewModel>(
            parameters = { parametersOf(route.orderId) }
        )
        CashierAddPositionScreen(
            onError = onError,
            onBackRequest = onBackRequest,
            onPositionItemAdded = onPositionItemAdded,
            viewModel = viewModel,
        )
    }
}

internal fun NavController.navigateToCashierAddPosition(
    orderId: Int,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierAddPositionRoute(
        orderId = orderId,
    )
    navigate(route, builder)
}
