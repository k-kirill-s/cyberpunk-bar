package by.cyberpunkfandom.barfrontend.presentation.worker.order

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class WorkerOrderRoute(val orderId: Int)

fun NavGraphBuilder.workerOrderComposable(
    onCloseRequest: () -> Unit,
    onOrderFinished: (orderId: Int) -> Unit,
    onPositionDetailsRequest: (positionId: String) -> Unit,
) {
    composable<WorkerOrderRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<WorkerOrderRoute>()
        val viewModel = koinViewModel<WorkerOrderViewModel>(
            parameters = { parametersOf(route.orderId) }
        )
        WorkerOrderScreen(
            onCloseRequest = onCloseRequest,
            onOrderFinished = onOrderFinished,
            onPositionDetailsRequest = onPositionDetailsRequest,
            viewModel = viewModel,
        )
    }
}

fun NavController.navigateToWorkerOrder(
    orderId: Int,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = WorkerOrderRoute(orderId)
    navigate(route, builder)
}

