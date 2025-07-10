package by.cyberpunkfandom.barfrontend.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import by.cyberpunkfandom.barfrontend.presentation.cashier.cashierComposable
import by.cyberpunkfandom.barfrontend.presentation.cashier.navigateToCashier
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import by.cyberpunkfandom.barfrontend.presentation.main.routing.MainRoutingRoute
import by.cyberpunkfandom.barfrontend.presentation.main.routing.mainRoutingComposable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.background)
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = MainRoutingRoute,
        ) {
            mainRoutingComposable(
                onOpenCashierRequest = {
                    navController.navigateToCashier()
                },
                onOpenCollectorRequest = {
                },
                onOpenBoardRequest = {
                },
            )

            cashierComposable(
                onBackRequest = { navController.popBackStack() }
            )
        }
    }
}

