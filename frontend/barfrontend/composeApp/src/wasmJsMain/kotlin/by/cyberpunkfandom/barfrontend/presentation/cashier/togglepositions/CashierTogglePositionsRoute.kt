package by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions

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
internal data class CashierTogglePositionsRoute(val type: String)

internal fun NavGraphBuilder.cashierTogglePositionsComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
) {
    composable<CashierTogglePositionsRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<CashierTogglePositionsRoute>()
        val viewModel = koinViewModel<CashierTogglePositionsViewModel>(
            parameters = { parametersOf(CashierTogglePositionsScreenType.valueOf(route.type)) }
        )
        CashierTogglePositionsScreen(
            onError = onError,
            onBackRequest = onBackRequest,
            viewModel = viewModel,
        )
    }
}

internal fun NavController.navigateToCashierTogglePositions(
    type: CashierTogglePositionsScreenType,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierTogglePositionsRoute(
        type = type.toString(),
    )
    navigate(route, builder)
}
