package by.cyberpunkfandom.barfrontend.presentation.worker.home

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
data class WorkerHomeRoute(val workerId: Int)

fun NavGraphBuilder.workerHomeComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onOrderStarted: (orderId: Int) -> Unit,
) {
    composable<WorkerHomeRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<WorkerHomeRoute>()
        val viewModel = koinViewModel<WorkerHomeViewModel>(
            parameters = { parametersOf(route.workerId) }
        )
        WorkerHomeScreen(
            onError = onError,
            onBackRequest = onBackRequest,
            onOrderStarted = onOrderStarted,
            viewModel = viewModel,
        )
    }
}

fun NavController.navigateToWorkerHome(
    workerId: Int,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = WorkerHomeRoute(workerId)
    navigate(route, builder)
}
