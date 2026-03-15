package by.cyberpunkfandom.barfrontend.presentation.main.routing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainRoutingScreen(
    onOpenCashierRequest: () -> Unit,
    onOpenWorkerRequest: () -> Unit,
    onOpenBoardRequest: () -> Unit,
    viewModel: MainRoutingViewModel = koinViewModel(),
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.dimensions.basePadding),
    ) {
        val isCompact = maxWidth < 860.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
        ) {
            Text(
                text = "Cyberpunk Bar",
                style = AppTheme.typography.displayLarge,
            )

            Text(
                text = "Операционный пульт для кассы, сборки и табло заказов.",
                style = AppTheme.typography.body,
            )

            if (isCompact) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
                ) {
                    MainMenuCard(
                        title = "Кассир",
                        description = "Создание, выдача и управление каталогом.",
                        color = AppTheme.colorScheme.accent,
                        onClick = onOpenCashierRequest,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    MainMenuCard(
                        title = "Сборщик",
                        description = "Выбор сотрудника и работа с текущим заказом.",
                        color = AppTheme.colorScheme.surfaceSelected,
                        onClick = onOpenWorkerRequest,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    MainMenuCard(
                        title = "Табло",
                        description = "Очередь, сборка и готовые заказы в реальном времени.",
                        color = AppTheme.colorScheme.green,
                        onClick = onOpenBoardRequest,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 360.dp),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
                ) {
                    MainMenuCard(
                        title = "Кассир",
                        description = "Создание, выдача и управление каталогом.",
                        color = AppTheme.colorScheme.accent,
                        onClick = onOpenCashierRequest,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                    MainMenuCard(
                        title = "Сборщик",
                        description = "Выбор сотрудника и работа с текущим заказом.",
                        color = AppTheme.colorScheme.surfaceSelected,
                        onClick = onOpenWorkerRequest,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                    MainMenuCard(
                        title = "Табло",
                        description = "Очередь, сборка и готовые заказы в реальном времени.",
                        color = AppTheme.colorScheme.green,
                        onClick = onOpenBoardRequest,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }
}

@Composable
private fun MainMenuCard(
    title: String,
    description: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
            .background(color)
            .clickable(onClick = onClick)
            .padding(AppTheme.dimensions.basePadding * 1.5f),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                style = AppTheme.typography.big,
            )
            Text(
                text = description,
                textAlign = TextAlign.Center,
                style = AppTheme.typography.body,
            )
        }
    }
}
