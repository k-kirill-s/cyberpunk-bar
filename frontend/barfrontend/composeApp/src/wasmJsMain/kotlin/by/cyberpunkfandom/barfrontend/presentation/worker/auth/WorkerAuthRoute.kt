package by.cyberpunkfandom.barfrontend.presentation.worker.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object WorkerAuthRoute

fun NavGraphBuilder.workerAuthComposable(
    onBackRequest: () -> Unit,
    onWorkerSelected: (workerId: Int) -> Unit,
) {
    composable<WorkerAuthRoute> { _ ->
        WorkerAuthScreen(
            onBackRequest = onBackRequest,
            onWorkerSelected = onWorkerSelected,
            viewModel = koinViewModel(),
        )
    }
}

fun NavController.navigateToWorkerAuth(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = WorkerAuthRoute
    navigate(route, builder)
}
