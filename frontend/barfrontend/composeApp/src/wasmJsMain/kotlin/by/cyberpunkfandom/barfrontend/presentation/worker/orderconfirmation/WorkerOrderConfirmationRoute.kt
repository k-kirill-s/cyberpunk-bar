package by.cyberpunkfandom.barfrontend.presentation.worker.orderconfirmation

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
data class WorkerOrderConfirmationRoute(
    val orderId: Int,
    val workerId: Int,
)

fun NavGraphBuilder.workerOrderConfirmationComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onOrderFinished: () -> Unit,
) {
    composable<WorkerOrderConfirmationRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<WorkerOrderConfirmationRoute>()
        val viewModel = koinViewModel<WorkerOrderConfirmationViewModel>(
            parameters = { parametersOf(route.orderId, route.workerId) }
        )
        WorkerOrderConfirmationScreen(
            onError = onError,
            onBackRequest = onBackRequest,
            onOrderFinished = onOrderFinished,
            viewModel = viewModel,
        )
    }
}

fun NavController.navigateToWorkerOrderConfirmation(
    orderId: Int,
    workerId: Int,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = WorkerOrderConfirmationRoute(
        orderId = orderId,
        workerId = workerId,
    )
    navigate(route, builder)
}
