package by.cyberpunkfandom.barfrontend.presentation.cashier.giveawayorder

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object CashierGiveAwayOrderRoute

fun NavGraphBuilder.cashierGiveAwayOrderComposable(
    onBackRequest: () -> Unit,
) {
    composable<CashierGiveAwayOrderRoute> { _ ->
        CashierGiveAwayOrderScreen(
            onBackRequest = onBackRequest,
            viewModel = koinViewModel(),
        )
    }
}

fun NavController.navigateToCashierGiveAwayOrder(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierGiveAwayOrderRoute
    navigate(route, builder)
}
