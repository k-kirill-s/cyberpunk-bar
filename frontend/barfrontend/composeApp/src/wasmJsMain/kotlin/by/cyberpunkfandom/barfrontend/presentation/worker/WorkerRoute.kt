package by.cyberpunkfandom.barfrontend.presentation.worker

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object WorkerRoute

fun NavGraphBuilder.workerComposable(
    onBackRequest: () -> Unit,
) {
    composable<WorkerRoute> { _ ->
        WorkerScreen(
            onBackRequest = onBackRequest,
            viewModel = koinViewModel()
        )
    }
}

fun NavController.navigateToWorker(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = WorkerRoute
    navigate(route, builder)
}
