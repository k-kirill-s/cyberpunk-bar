package by.cyberpunkfandom.barfrontend.presentation.worker.positiondetails

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
data class WorkerPositionDetailsRoute(val positionId: String)

fun NavGraphBuilder.workerPositionDetailsComposable(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
) {
    composable<WorkerPositionDetailsRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<WorkerPositionDetailsRoute>()
        val viewModel = koinViewModel<WorkerPositionDetailsViewModel>(
            parameters = { parametersOf(route.positionId) }
        )
        WorkerPositionDetailsScreen(
            onError = onError,
            onBackRequest = onBackRequest,
            viewModel = viewModel,
        )
    }
}

fun NavController.navigateToWorkerPositionDetails(
    positionId: String,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = WorkerPositionDetailsRoute(positionId)
    navigate(route, builder)
}

