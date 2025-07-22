package by.cyberpunkfandom.barfrontend.presentation.worker

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
import kotlinx.coroutines.launch

@Composable
fun WorkerScreen(
    onBackRequest: () -> Unit,
    viewModel: WorkerViewModel,
) {
    var workerId: Int? by remember { mutableStateOf(null) }

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

        AppTheme(isTablet = false) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = WorkerAuthRoute,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
            ) {

                workerAuthComposable(
                    onError = { showErrorSnackbar(it) },
                    onBackRequest = onBackRequest,
                    onWorkerSelected = { selectedWorkerId ->
                        workerId = selectedWorkerId
                        navController.navigateToWorkerHome(selectedWorkerId)
                    }
                )

                workerHomeComposable(
                    onError = { showErrorSnackbar(it) },
                    onBackRequest = onBackRequest,
                    onOrderStarted = { orderId ->
                        navController.navigateToWorkerOrder(orderId)
                    }
                )

                workerOrderComposable(
                    onError = { showErrorSnackbar(it) },
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
                    onError = { showErrorSnackbar(it) },
                    onBackRequest = {
                        navController.popBackStack()
                    }
                )

                workerOrderConfirmationComposable(
                    onError = { showErrorSnackbar(it) },
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
}
