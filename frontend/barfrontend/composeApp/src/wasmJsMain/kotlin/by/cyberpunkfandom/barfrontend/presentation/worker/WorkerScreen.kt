package by.cyberpunkfandom.barfrontend.presentation.worker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import by.cyberpunkfandom.barfrontend.presentation.worker.auth.WorkerAuthRoute
import by.cyberpunkfandom.barfrontend.presentation.worker.auth.workerAuthComposable
import by.cyberpunkfandom.barfrontend.presentation.worker.home.WorkerHomeRoute
import by.cyberpunkfandom.barfrontend.presentation.worker.home.navigateToWorkerHome
import by.cyberpunkfandom.barfrontend.presentation.worker.home.workerHomeComposable
import by.cyberpunkfandom.barfrontend.presentation.worker.order.navigateToWorkerOrder
import by.cyberpunkfandom.barfrontend.presentation.worker.order.workerOrderComposable
import by.cyberpunkfandom.barfrontend.presentation.worker.orderconfirmation.navigateToWorkerOrderConfirmation
import by.cyberpunkfandom.barfrontend.presentation.worker.orderconfirmation.workerOrderConfirmationComposable
import by.cyberpunkfandom.barfrontend.presentation.worker.positiondetails.navigateToWorkerPositionDetails
import by.cyberpunkfandom.barfrontend.presentation.worker.positiondetails.workerPositionDetailsComposable

@Composable
fun WorkerScreen(
    onBackRequest: () -> Unit,
    viewModel: WorkerViewModel,
) {
    var workerId: Int? by remember { mutableStateOf(null) }

    AppTheme(isTablet = false) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = WorkerAuthRoute,
        ) {

            workerAuthComposable(
                onBackRequest = onBackRequest,
                onWorkerSelected = { selectedWorkerId ->
                    workerId = selectedWorkerId
                    navController.navigateToWorkerHome(selectedWorkerId)
                }
            )

            workerHomeComposable(
                onBackRequest = onBackRequest,
                onOrderStarted = { orderId ->
                    navController.navigateToWorkerOrder(orderId)
                }
            )

            workerOrderComposable(
                onCloseRequest = {
                    navController.popBackStack(route = WorkerHomeRoute(workerId!!), inclusive = false)
                },
                onOrderFinished = { orderId ->
                    navController.navigateToWorkerOrderConfirmation(orderId)
                },
                onPositionDetailsRequest = { positionId ->
                    navController.navigateToWorkerPositionDetails(positionId)
                }
            )

            workerPositionDetailsComposable(
                onBackRequest = {
                    navController.popBackStack()
                }
            )

            workerOrderConfirmationComposable(
                onBackRequest = {
                    navController.popBackStack()
                },
                onOrderFinished = {
                    navController.navigateToWorkerHome(workerId!!) {
                        popUpTo(WorkerHomeRoute(workerId!!)) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
