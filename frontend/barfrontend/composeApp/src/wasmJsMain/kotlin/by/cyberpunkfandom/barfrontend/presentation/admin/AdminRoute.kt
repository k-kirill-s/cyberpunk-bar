package by.cyberpunkfandom.barfrontend.presentation.admin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object AdminRoute

fun NavGraphBuilder.adminComposable(
    onBackRequest: () -> Unit,
) {
    composable<AdminRoute> {
        AdminScreen(
            onBackRequest = onBackRequest,
        )
    }
}

fun NavController.navigateToAdmin(
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(AdminRoute, builder)
}
