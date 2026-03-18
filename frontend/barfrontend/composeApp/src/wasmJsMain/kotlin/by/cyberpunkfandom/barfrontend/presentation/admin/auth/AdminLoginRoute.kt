package by.cyberpunkfandom.barfrontend.presentation.admin.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object AdminLoginRoute

fun NavGraphBuilder.adminLoginComposable(
    isAuthorizing: Boolean,
    onBackRequest: () -> Unit,
    onLoginRequest: (String, String) -> Unit,
) {
    composable<AdminLoginRoute> {
        AdminLoginScreen(
            isAuthorizing = isAuthorizing,
            onBackRequest = onBackRequest,
            onLoginRequest = onLoginRequest,
        )
    }
}
