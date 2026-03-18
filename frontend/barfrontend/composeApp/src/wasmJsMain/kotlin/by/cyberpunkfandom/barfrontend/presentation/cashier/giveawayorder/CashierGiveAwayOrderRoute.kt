package by.cyberpunkfandom.barfrontend.presentation.cashier.giveawayorder

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
data class CashierGiveAwayOrderRoute(
    val cashierId: Int,
)

fun NavGraphBuilder.cashierGiveAwayOrderComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
) {
    composable<CashierGiveAwayOrderRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<CashierGiveAwayOrderRoute>()
        CashierGiveAwayOrderScreen(
            onError = onError,
            onBackRequest = onBackRequest,
            viewModel = koinViewModel(
                parameters = { parametersOf(route.cashierId) }
            ),
        )
    }
}

fun NavController.navigateToCashierGiveAwayOrder(
    cashierId: Int,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierGiveAwayOrderRoute(cashierId = cashierId)
    navigate(route, builder)
}
