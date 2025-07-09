package by.cyberpunkfandom.barfrontend.presentation.cashier.cancelorder

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object CashierCancelOrderRoute

fun NavGraphBuilder.cashierCancelOrderComposable(
    onBackRequest: () -> Unit,
) {
    composable<CashierCancelOrderRoute> { _ ->
        CashierCancelOrderScreen(
            onBackRequest = onBackRequest,
            viewModel = koinViewModel(),
        )
    }
}

fun NavController.navigateToCashierCancelOrder(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierCancelOrderRoute
    navigate(route, builder)
}
