package by.cyberpunkfandom.barfrontend.presentation.cashier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import by.cyberpunkfandom.barfrontend.presentation.cashier.addposition.cashierAddPositionComposable
import by.cyberpunkfandom.barfrontend.presentation.cashier.addposition.navigateToCashierAddPosition
import by.cyberpunkfandom.barfrontend.presentation.cashier.addpositionextra.cashierAddPositionExtraComposable
import by.cyberpunkfandom.barfrontend.presentation.cashier.addpositionextra.navigateToCashierAddPositionExtra
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.CashierCreateOrderRoute
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.cashierCreateOrderComposable
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.navigateToCashierCreateOrder
import by.cyberpunkfandom.barfrontend.presentation.cashier.home.CashierHomeRoute
import by.cyberpunkfandom.barfrontend.presentation.cashier.home.cashierHomeComposable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CashierScreen(
    onBackRequest: () -> Unit,
    viewModel: CashierViewModel = koinViewModel(),
) {
    var currentOrderId by remember { mutableStateOf<Int?>(null) }

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CashierHomeRoute,
    ) {
        cashierHomeComposable(
            onBackRequest = onBackRequest,
            onOpenCreateOrderRequest = { orderId ->
                currentOrderId = orderId
                navController.navigateToCashierCreateOrder(orderId)
            },
            onCancelOrderRequest = {},
            onTogglePositionsRequest = {},
            onToggleExtraRequest = {},
        )

        cashierCreateOrderComposable(
            onCloseRequest = { navController.popBackStack() },
            onAddPositionRequest = {
                currentOrderId?.let { navController.navigateToCashierAddPosition(it) }
            },
            onAddPositionExtraRequest = { positionItemId ->
                navController.navigateToCashierAddPositionExtra(positionItemId)
            },
            onOrderFormed = {
                navController.popBackStack(CashierHomeRoute, inclusive = false)
            }
        )

        cashierAddPositionComposable(
            onBackRequest = { navController.popBackStack() },
            onPositionItemAdded = { positionItemId ->
                navController.navigateToCashierAddPositionExtra(
                    positionItemId = positionItemId
                )
            },
        )

        cashierAddPositionExtraComposable(
            onBackRequest = {
                currentOrderId?.let { orderId ->
                    navController.navigateToCashierCreateOrder(orderId = orderId) {
                        popUpTo(CashierCreateOrderRoute(orderId)) {
                            inclusive = true
                        }
                    }
                }
            },
            onPositionExtraItemAdded = { positionExtraItemId ->
                currentOrderId?.let { orderId ->
                    navController.navigateToCashierCreateOrder(orderId = orderId) {
                        popUpTo(CashierCreateOrderRoute(orderId)) {
                            inclusive = true
                        }
                    }
                }
            }
        )
    }
}
