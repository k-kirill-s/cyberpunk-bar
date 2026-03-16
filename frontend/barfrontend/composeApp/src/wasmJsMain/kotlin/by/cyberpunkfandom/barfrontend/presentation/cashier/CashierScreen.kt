package by.cyberpunkfandom.barfrontend.presentation.cashier

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.cashier.addposition.cashierAddPositionComposable
import by.cyberpunkfandom.barfrontend.presentation.cashier.addposition.navigateToCashierAddPosition
import by.cyberpunkfandom.barfrontend.presentation.cashier.cancelorder.cashierCancelOrderComposable
import by.cyberpunkfandom.barfrontend.presentation.cashier.cancelorder.navigateToCashierCancelOrder
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.CashierCreateOrderRoute
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.cashierCreateOrderComposable
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.navigateToCashierCreateOrder
import by.cyberpunkfandom.barfrontend.presentation.cashier.giveawayorder.cashierGiveAwayOrderComposable
import by.cyberpunkfandom.barfrontend.presentation.cashier.giveawayorder.navigateToCashierGiveAwayOrder
import by.cyberpunkfandom.barfrontend.presentation.cashier.home.CashierHomeRoute
import by.cyberpunkfandom.barfrontend.presentation.cashier.home.cashierHomeComposable
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CashierScreen(
    onBackRequest: () -> Unit,
    viewModel: CashierViewModel = koinViewModel(),
) {
    var currentOrderId by remember { mutableStateOf<Int?>(null) }

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    fun showErrorSnackbar(code: ExceptionCodes) {
        scope.launch {
            snackbarHostState.showSnackbar(message = code.message)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = CashierHomeRoute,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            cashierHomeComposable(
                onError = { showErrorSnackbar(it) },
                onBackRequest = onBackRequest,
                onOpenCreateOrderRequest = { orderId ->
                    currentOrderId = orderId
                    navController.navigateToCashierCreateOrder(orderId)
                },
                onGiveAwayOrderRequest = {
                    navController.navigateToCashierGiveAwayOrder()
                },
                onCancelOrderRequest = {
                    navController.navigateToCashierCancelOrder()
                },
            )

            cashierCreateOrderComposable(
                onError = { showErrorSnackbar(it) },
                onCloseRequest = { navController.popBackStack() },
                onAddPositionRequest = {
                    currentOrderId?.let { navController.navigateToCashierAddPosition(it) }
                },
                onOrderFormed = {
                    navController.popBackStack(CashierHomeRoute, inclusive = false)
                }
            )

            cashierAddPositionComposable(
                onError = { showErrorSnackbar(it) },
                onBackRequest = { navController.popBackStack() },
                onPositionItemAdded = {
                    currentOrderId?.let { orderId ->
                        navController.navigateToCashierCreateOrder(orderId = orderId) {
                            popUpTo(CashierCreateOrderRoute(orderId)) {
                                inclusive = true
                            }
                        }
                    }
                },
            )

            cashierGiveAwayOrderComposable(
                onError = { showErrorSnackbar(it) },
                onBackRequest = {
                    navController.popBackStack()
                }
            )

            cashierCancelOrderComposable(
                onError = { showErrorSnackbar(it) },
                onBackRequest = {
                    navController.popBackStack()
                }
            )
        }
    }
}
