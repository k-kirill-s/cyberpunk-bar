package by.cyberpunkfandom.barfrontend.presentation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import by.cyberpunkfandom.barfrontend.presentation.admin.adminComposable
import by.cyberpunkfandom.barfrontend.presentation.admin.navigateToAdmin
import by.cyberpunkfandom.barfrontend.presentation.cashier.cashierComposable
import by.cyberpunkfandom.barfrontend.presentation.cashier.navigateToCashier
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import by.cyberpunkfandom.barfrontend.presentation.infoboard.infoBoardComposable
import by.cyberpunkfandom.barfrontend.presentation.infoboard.navigateToInfoBoard
import by.cyberpunkfandom.barfrontend.presentation.main.routing.MainRoutingRoute
import by.cyberpunkfandom.barfrontend.presentation.main.routing.mainRoutingComposable
import by.cyberpunkfandom.barfrontend.presentation.worker.navigateToWorker
import by.cyberpunkfandom.barfrontend.presentation.worker.workerComposable

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.background)
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = MainRoutingRoute,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            mainRoutingComposable(
                onOpenCashierRequest = {
                    navController.navigateToCashier()
                },
                onOpenWorkerRequest = {
                    navController.navigateToWorker()
                },
                onOpenBoardRequest = {
                    navController.navigateToInfoBoard()
                },
                onOpenAdminRequest = {
                    navController.navigateToAdmin()
                },
            )

            cashierComposable(
                onBackRequest = { navController.popBackStack() }
            )

            workerComposable(
                onBackRequest = { navController.popBackStack() }
            )

            infoBoardComposable()

            adminComposable(
                onBackRequest = { navController.popBackStack() }
            )
        }
    }
}
