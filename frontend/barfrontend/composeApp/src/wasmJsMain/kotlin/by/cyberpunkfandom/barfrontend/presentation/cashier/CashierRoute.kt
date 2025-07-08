package by.cyberpunkfandom.barfrontend.presentation.cashier

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object CashierRoute

fun NavGraphBuilder.cashierComposable(
    onBackRequest: () -> Unit,
) {
    composable<CashierRoute> { _ ->
        CashierScreen(
            onBackRequest = onBackRequest,
        )
    }
}

fun NavController.navigateToCashier(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierRoute
    navigate(route, builder)
}
