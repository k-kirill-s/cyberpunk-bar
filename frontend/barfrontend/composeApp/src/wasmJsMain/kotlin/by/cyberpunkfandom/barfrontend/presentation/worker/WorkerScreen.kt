package by.cyberpunkfandom.barfrontend.presentation.worker

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import by.cyberpunkfandom.barfrontend.presentation.worker.auth.WorkerAuthRoute
import by.cyberpunkfandom.barfrontend.presentation.worker.auth.workerAuthComposable
import by.cyberpunkfandom.barfrontend.presentation.worker.home.navigateToWorkerHome
import by.cyberpunkfandom.barfrontend.presentation.worker.home.workerHomeComposable
import by.cyberpunkfandom.barfrontend.presentation.worker.order.navigateToWorkerOrder
import by.cyberpunkfandom.barfrontend.presentation.worker.order.workerOrderComposable
import by.cyberpunkfandom.barfrontend.presentation.worker.positiondetails.navigateToWorkerPositionDetails
import by.cyberpunkfandom.barfrontend.presentation.worker.positiondetails.workerPositionDetailsComposable

@Composable
fun WorkerScreen(
    onBackRequest: () -> Unit,
    viewModel: WorkerViewModel,
) {
    AppTheme(isTablet = false) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = WorkerAuthRoute,
        ) {

            workerAuthComposable(
                onBackRequest = onBackRequest,
                onWorkerSelected = { workerId ->
                    navController.navigateToWorkerHome(workerId)
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
                    navController.popBackStack(route = WorkerAuthRoute, inclusive = false)
                },
                onOrderFinished = {
                    // todo
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
        }
    }
}
