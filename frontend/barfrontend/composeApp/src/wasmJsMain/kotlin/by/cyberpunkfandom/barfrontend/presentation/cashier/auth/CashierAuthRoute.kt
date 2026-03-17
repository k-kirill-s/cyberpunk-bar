package by.cyberpunkfandom.barfrontend.presentation.cashier.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object CashierAuthRoute

fun NavGraphBuilder.cashierAuthComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onCashierSelected: (cashierId: Int) -> Unit,
) {
    composable<CashierAuthRoute> { _ ->
        CashierAuthScreen(
            onError = onError,
            onBackRequest = onBackRequest,
            onCashierSelected = onCashierSelected,
            viewModel = koinViewModel(),
        )
    }
}

fun NavController.navigateToCashierAuth(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = CashierAuthRoute
    navigate(route, builder)
}
