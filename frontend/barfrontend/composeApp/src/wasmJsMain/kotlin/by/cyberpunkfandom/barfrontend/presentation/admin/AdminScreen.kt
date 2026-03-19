package by.cyberpunkfandom.barfrontend.presentation.admin

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.admin.analytics.adminAnalyticsComposable
import by.cyberpunkfandom.barfrontend.presentation.admin.analytics.navigateToAdminAnalytics
import by.cyberpunkfandom.barfrontend.presentation.admin.auth.AdminLoginRoute
import by.cyberpunkfandom.barfrontend.presentation.admin.auth.adminLoginComposable
import by.cyberpunkfandom.barfrontend.presentation.admin.overview.AdminOverviewRoute
import by.cyberpunkfandom.barfrontend.presentation.admin.overview.adminOverviewComposable
import by.cyberpunkfandom.barfrontend.presentation.admin.overview.navigateToAdminOverview
import by.cyberpunkfandom.barfrontend.presentation.admin.positions.adminPositionEditorComposable
import by.cyberpunkfandom.barfrontend.presentation.admin.positions.adminPositionsComposable
import by.cyberpunkfandom.barfrontend.presentation.admin.positions.navigateToAdminPositionEditor
import by.cyberpunkfandom.barfrontend.presentation.admin.positions.navigateToAdminPositions
import by.cyberpunkfandom.barfrontend.presentation.admin.variants.adminVariantEditorComposable
import by.cyberpunkfandom.barfrontend.presentation.admin.variants.adminVariantsComposable
import by.cyberpunkfandom.barfrontend.presentation.admin.variants.navigateToAdminVariantEditor
import by.cyberpunkfandom.barfrontend.presentation.admin.variants.navigateToAdminVariants
import by.cyberpunkfandom.barfrontend.presentation.admin.workers.adminWorkerEditorComposable
import by.cyberpunkfandom.barfrontend.presentation.admin.workers.adminWorkersComposable
import by.cyberpunkfandom.barfrontend.presentation.admin.workers.navigateToAdminWorkerEditor
import by.cyberpunkfandom.barfrontend.presentation.admin.workers.navigateToAdminWorkers
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminScreen(
    onBackRequest: () -> Unit,
    authViewModel: AdminViewModel = koinViewModel(),
    catalogViewModel: AdminCatalogViewModel = koinViewModel(),
) {
    val isAuthorized = authViewModel.isAuthorized.collectAsStateWithLifecycle().value
    val isAuthorizing = authViewModel.isAuthorizing.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val initialRoute: Any = remember {
        if (isAuthorized) AdminOverviewRoute else AdminLoginRoute
    }
    var wasAuthorized by remember { mutableStateOf(isAuthorized) }

    fun showErrorSnackbar(code: ExceptionCodes) {
        scope.launch {
            snackbarHostState.showSnackbar(code.message)
        }
    }

    val handleLogoutRequest = remember(authViewModel, onBackRequest) {
        {
            authViewModel.logout()
            onBackRequest()
        }
    }

    LaunchedEffect(Unit) {
        authViewModel.onError.collect { showErrorSnackbar(it) }
    }

    LaunchedEffect(Unit) {
        catalogViewModel.onError.collect { showErrorSnackbar(it) }
    }

    LaunchedEffect(isAuthorized) {
        if (isAuthorized) {
            catalogViewModel.ensureLoaded()
            if (!wasAuthorized) {
                navController.navigateToAdminOverview {
                    popUpTo(AdminLoginRoute) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }

        wasAuthorized = isAuthorized
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        NavHost(
            navController = navController,
            startDestination = initialRoute,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            adminLoginComposable(
                isAuthorizing = isAuthorizing,
                onBackRequest = onBackRequest,
                onLoginRequest = authViewModel::login,
            )

            adminOverviewComposable(
                viewModel = catalogViewModel,
                onBackRequest = handleLogoutRequest,
                onLogoutRequest = handleLogoutRequest,
                onOpenAnalyticsRequest = { navController.navigateToAdminAnalytics() },
                onOpenWorkersRequest = { navController.navigateToAdminWorkers() },
                onOpenPositionsRequest = { navController.navigateToAdminPositions() },
                onOpenVariantsRequest = { navController.navigateToAdminVariants() },
            )

            adminAnalyticsComposable(
                viewModel = catalogViewModel,
                onBackRequest = { navController.popBackStack() },
            )

            adminWorkersComposable(
                viewModel = catalogViewModel,
                onBackRequest = { navController.popBackStack() },
                onCreateWorkerRequest = { navController.navigateToAdminWorkerEditor() },
                onEditWorkerRequest = { workerId ->
                    navController.navigateToAdminWorkerEditor(workerId)
                },
            )

            adminWorkerEditorComposable(
                viewModel = catalogViewModel,
                onBackRequest = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )

            adminPositionsComposable(
                viewModel = catalogViewModel,
                onBackRequest = { navController.popBackStack() },
                onCreatePositionRequest = { navController.navigateToAdminPositionEditor() },
                onEditPositionRequest = { positionId ->
                    navController.navigateToAdminPositionEditor(positionId)
                },
            )

            adminPositionEditorComposable(
                viewModel = catalogViewModel,
                onBackRequest = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )

            adminVariantsComposable(
                viewModel = catalogViewModel,
                onBackRequest = { navController.popBackStack() },
                onCreateVariantRequest = { navController.navigateToAdminVariantEditor() },
                onEditVariantRequest = { variantId ->
                    navController.navigateToAdminVariantEditor(variantId)
                },
            )

            adminVariantEditorComposable(
                viewModel = catalogViewModel,
                onBackRequest = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }
    }
}
