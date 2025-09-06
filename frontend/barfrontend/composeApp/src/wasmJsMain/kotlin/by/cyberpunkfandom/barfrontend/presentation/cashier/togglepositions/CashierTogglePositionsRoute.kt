package by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
internal data object CashierTogglePositionsRoute

internal fun NavGraphBuilder.cashierTogglePositionsComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
) {
    composable<CashierTogglePositionsRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<CashierTogglePositionsRoute>()
        val viewModel = koinViewModel<CashierTogglePositionsViewModel>()
        CashierTogglePositionsScreen(
            onError = onError,
            onBackRequest = onBackRequest,
            viewModel = viewModel,
        )
    }
}

internal fun NavController.navigateToCashierTogglePositions(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierTogglePositionsRoute
    navigate(route, builder)
}
