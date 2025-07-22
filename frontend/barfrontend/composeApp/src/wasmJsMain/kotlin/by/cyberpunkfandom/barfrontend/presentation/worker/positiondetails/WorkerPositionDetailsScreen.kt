package by.cyberpunkfandom.barfrontend.presentation.worker.positiondetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkerPositionDetailsScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    viewModel: WorkerPositionDetailsViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    WorkerPositionDetailsScreen(
        onBackClick = onBackRequest,
        position = viewModel.position.collectAsStateWithLifecycle().value,
    )
}

@Composable
private fun WorkerPositionDetailsScreen(
    onBackClick: () -> Unit,
    position: Position?,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            modifier = Modifier.fillMaxWidth(),
            title = position?.name.orEmpty(),
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackClick,
        )

        Text(
            text = position?.description.orEmpty(),
            modifier = Modifier.padding(AppTheme.dimensions.basePadding),
            textAlign = TextAlign.Justify,
            style = AppTheme.typography.body,
        )
    }
}
