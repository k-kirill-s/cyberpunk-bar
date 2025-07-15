package by.cyberpunkfandom.barfrontend.presentation.infoboard

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object InfoBoardRoute

fun NavGraphBuilder.infoBoardComposable() {
    composable<InfoBoardRoute> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<InfoBoardRoute>()
        val viewModel = koinViewModel<InfoBoardViewModel>()
        InfoBoardScreen(viewModel = viewModel)
    }
}

fun NavController.navigateToInfoBoard(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val route = InfoBoardRoute
    navigate(route, builder)
}

