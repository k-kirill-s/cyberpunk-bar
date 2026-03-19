package by.cyberpunkfandom.barfrontend.presentation.main.routing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun MainRoutingScreen(
    onOpenCashierRequest: () -> Unit,
    onOpenWorkerRequest: () -> Unit,
    onOpenBoardRequest: () -> Unit,
    onOpenAdminRequest: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.dimensions.basePadding),
    ) {
        val isCompact = maxWidth < 860.dp
        val workerHighlight = AppTheme.colorScheme.accentGlow
        val adminHighlight = AppTheme.colorScheme.accentSecondary

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
        ) {
            Text(
                text = "Cyberpunk Bar",
                style = if (isCompact) AppTheme.typography.big else AppTheme.typography.displayLarge,
            )

            Text(
                text = "Операционный пульт для кассы, бармена и табло заказов.",
                style = AppTheme.typography.body,
            )

            if (isCompact) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
                ) {
                    MainMenuCard(
                        title = "Кассир",
                        description = "Создание, выдача и отмена заказов.",
                        color = AppTheme.colorScheme.accent,
                        isCompact = true,
                        onClick = onOpenCashierRequest,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    MainMenuCard(
                        title = "Бармен",
                        description = "Выбор стендовика и работа с текущим заказом.",
                        color = workerHighlight,
                        isCompact = true,
                        onClick = onOpenWorkerRequest,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    MainMenuCard(
                        title = "Табло",
                        description = "Очередь, сборка и готовые заказы в реальном времени.",
                        color = AppTheme.colorScheme.warning,
                        isCompact = true,
                        onClick = onOpenBoardRequest,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    MainMenuCard(
                        title = "Администратор",
                        description = "Каталог, цены и команда под паролем.",
                        color = adminHighlight,
                        isCompact = true,
                        onClick = onOpenAdminRequest,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 520.dp),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
                    ) {
                        MainMenuCard(
                            title = "Кассир",
                            description = "Создание, выдача и отмена заказов.",
                            color = AppTheme.colorScheme.accent,
                            isCompact = false,
                            onClick = onOpenCashierRequest,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                        MainMenuCard(
                            title = "Бармен",
                            description = "Выбор стендовика и работа с текущим заказом.",
                            color = workerHighlight,
                            isCompact = false,
                            onClick = onOpenWorkerRequest,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
                    ) {
                        MainMenuCard(
                            title = "Табло",
                            description = "Очередь, сборка и готовые заказы в реальном времени.",
                            color = AppTheme.colorScheme.warning,
                            isCompact = false,
                            onClick = onOpenBoardRequest,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                        MainMenuCard(
                            title = "Администратор",
                            description = "Каталог, цены и команда под паролем.",
                            color = adminHighlight,
                            isCompact = false,
                            onClick = onOpenAdminRequest,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    }
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
    isCompact: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .heightIn(min = if (isCompact) 156.dp else 0.dp)
            .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
            .background(color.copy(alpha = if (isCompact) 0.16f else 0.12f))
            .border(
                border = BorderStroke(
                    width = 2.dp,
                    color = color,
                ),
                shape = RoundedCornerShape(AppTheme.dimensions.cornerRadius),
            )
            .clickable(onClick = onClick)
            .padding(if (isCompact) AppTheme.dimensions.basePadding else AppTheme.dimensions.basePadding * 1.5f),
        contentAlignment = if (isCompact) Alignment.TopStart else Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
            horizontalAlignment = if (isCompact) Alignment.Start else Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                textAlign = if (isCompact) TextAlign.Start else TextAlign.Center,
                style = if (isCompact) AppTheme.typography.title else AppTheme.typography.big,
            )
            Text(
                text = description,
                textAlign = if (isCompact) TextAlign.Start else TextAlign.Center,
                style = AppTheme.typography.body.copy(color = AppTheme.colorScheme.textSecondary),
            )
        }
    }
}
