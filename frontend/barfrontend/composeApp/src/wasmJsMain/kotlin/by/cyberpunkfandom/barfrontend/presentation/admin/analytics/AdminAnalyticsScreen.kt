package by.cyberpunkfandom.barfrontend.presentation.admin.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminAnalyticsContent
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminContentContainer
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminLoadingState
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdminAnalyticsScreen(
    viewModel: AdminCatalogViewModel,
    onBackRequest: () -> Unit,
) {
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val analytics = viewModel.analytics.collectAsStateWithLifecycle().value
    val isAnalyticsRefreshing = viewModel.isAnalyticsRefreshing.collectAsStateWithLifecycle().value

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Аналитика",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackRequest,
        )

        AdminContentContainer {
            if (isLoading && analytics == null) {
                AdminLoadingState(title = "Загружаем аналитику")
            } else {
                AdminAnalyticsContent(
                    analytics = analytics,
                    isAnalyticsRefreshing = isAnalyticsRefreshing,
                    onAnalyticsRefreshClick = viewModel::onAnalyticsRefreshClick,
                )
            }
        }
    }
}
