package by.cyberpunkfandom.barfrontend.presentation.cashier.addpositionextra

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
internal data class CashierAddPositionExtraRoute(val positionItemId: Int)

internal fun NavGraphBuilder.cashierAddPositionExtraComposable(
    onBackRequest: () -> Unit,
    onPositionExtraItemAdded: (positionExtraItemId: Int) -> Unit,
) {
    composable<CashierAddPositionExtraRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<CashierAddPositionExtraRoute>()
        val viewModel = koinViewModel<CashierAddPositionExtraViewModel>(
            parameters = { parametersOf(route.positionItemId) }
        )
        CashierAddPositionExtraScreen(
            onBackRequest = onBackRequest,
            onPositionExtraItemAdded = onPositionExtraItemAdded,
            viewModel = viewModel,
        )
    }
}

internal fun NavController.navigateToCashierAddPositionExtra(
    positionItemId: Int,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierAddPositionExtraRoute(
        positionItemId = positionItemId,
    )
    navigate(route, builder)
}
