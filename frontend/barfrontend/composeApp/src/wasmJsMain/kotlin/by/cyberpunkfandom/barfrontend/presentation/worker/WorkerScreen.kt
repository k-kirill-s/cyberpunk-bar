package by.cyberpunkfandom.barfrontend.presentation.worker

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import by.cyberpunkfandom.barfrontend.presentation.worker.auth.WorkerAuthRoute
import by.cyberpunkfandom.barfrontend.presentation.worker.auth.workerAuthComposable
import by.cyberpunkfandom.barfrontend.presentation.worker.home.navigateToWorkerHome
import by.cyberpunkfandom.barfrontend.presentation.worker.home.workerHomeComposable

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
                onOrderStarted = { }
            )
        }
    }
}
