package by.cyberpunkfandom.barfrontend.presentation.worker.orderconfirmation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class WorkerOrderConfirmationRoute(val orderId: Int)

fun NavGraphBuilder.workerOrderConfirmationComposable(
    onBackRequest: () -> Unit,
    onOrderFinished: () -> Unit,
) {
    composable<WorkerOrderConfirmationRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<WorkerOrderConfirmationRoute>()
        val viewModel = koinViewModel<WorkerOrderConfirmationViewModel>(
            parameters = { parametersOf(route.orderId) }
        )
        WorkerOrderConfirmationScreen(
            onBackRequest = onBackRequest,
            onOrderFinished = onOrderFinished,
            viewModel = viewModel,
        )
    }
}

fun NavController.navigateToWorkerOrderConfirmation(
    orderId: Int,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = WorkerOrderConfirmationRoute(orderId)
    navigate(route, builder)
}

