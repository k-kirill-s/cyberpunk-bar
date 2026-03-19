package by.cyberpunkfandom.barfrontend.presentation.infoboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppIcon
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppVerticalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppStateMessage
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun InfoBoardScreen(
    onBackRequest: () -> Unit,
    viewModel: InfoBoardViewModel,
) {
    InfoBoardScreen(
        onBackRequest = onBackRequest,
        formedOrdersNames = viewModel.formedOrdersNames.collectAsStateWithLifecycle().value,
        startedOrdersNames = viewModel.startedOrdersNames.collectAsStateWithLifecycle().value,
        finishedOrdersNames = viewModel.finishedOrdersNames.collectAsStateWithLifecycle().value,
        isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value,
        errorMessage = viewModel.errorMessage.collectAsStateWithLifecycle().value,
    )
}

@Composable
private fun InfoBoardScreen(
    onBackRequest: () -> Unit,
    formedOrdersNames: List<String>,
    startedOrdersNames: List<String>,
    finishedOrdersNames: List<String>,
    isLoading: Boolean,
    errorMessage: String?,
) {
    if (isLoading && formedOrdersNames.isEmpty() && startedOrdersNames.isEmpty() && finishedOrdersNames.isEmpty()) {
        AppStateMessage(
            title = "Загружаем табло",
            isLoading = true,
        )
        return
    }

    if (!errorMessage.isNullOrBlank() && formedOrdersNames.isEmpty() && startedOrdersNames.isEmpty() && finishedOrdersNames.isEmpty()) {
        AppStateMessage(
            title = errorMessage,
            description = "Попробуйте обновить страницу через несколько секунд.",
        )
        return
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .hoverable(interactionSource = interactionSource)
    ) {
        val isCompact = maxWidth < 900.dp
        val showBackButton = isCompact || isHovered
        val boardHeaderHeight = if (isCompact) 88.dp else 104.dp
        val boardFooterHeight = if (isCompact) 84.dp else 96.dp
        val currentTimeLabel = rememberInfoBoardTimeLabel()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = boardHeaderHeight,
                    bottom = boardFooterHeight,
                ),
        ) {
            if (isCompact) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    OrdersBox(
                        ordersNames = formedOrdersNames,
                        title = "В очереди",
                        textColor = AppTheme.colorScheme.text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 280.dp),
                    )

                    AppHorizontalDivider()

                    OrdersBox(
                        ordersNames = startedOrdersNames,
                        title = "В процессе",
                        textColor = AppTheme.colorScheme.text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 280.dp),
                    )

                    AppHorizontalDivider()

                    OrdersBox(
                        ordersNames = finishedOrdersNames,
                        title = "Готовы",
                        textColor = AppTheme.colorScheme.success,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 280.dp),
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {

                    OrdersBox(
                        ordersNames = formedOrdersNames,
                        title = "В очереди",
                        textColor = AppTheme.colorScheme.text,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )

                    AppVerticalDivider()

                    OrdersBox(
                        ordersNames = startedOrdersNames,
                        title = "В процессе",
                        textColor = AppTheme.colorScheme.text,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )

                    AppVerticalDivider()

                    OrdersBox(
                        ordersNames = finishedOrdersNames,
                        title = "Готовы",
                        textColor = AppTheme.colorScheme.success,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                }
            }
        }

        InfoBoardChrome(
            currentTimeLabel = currentTimeLabel,
            isCompact = isCompact,
            modifier = Modifier.fillMaxSize(),
        )

        BackButtonOverlay(
            isVisible = showBackButton,
            onClick = onBackRequest,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = AppTheme.dimensions.basePadding,
                    top = boardHeaderHeight + AppTheme.dimensions.basePadding / 2,
                ),
        )
    }
}

@Composable
private fun BackButtonOverlay(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .alpha(if (isVisible) 1f else 0f)
            .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
            .background(AppTheme.colorScheme.surface.copy(alpha = 0.92f))
            .clickable(enabled = isVisible, onClick = onClick)
            .border(
                width = AppTheme.dimensions.thinDivider * 2,
                color = AppTheme.colorScheme.dividerStrong,
                shape = RoundedCornerShape(AppTheme.dimensions.cornerRadius),
            )
            .padding(
                horizontal = AppTheme.dimensions.basePadding * 0.75f,
                vertical = AppTheme.dimensions.basePadding / 2,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppIcon(painter = painterResource(Res.drawable.back_24dp))
            Text(
                text = "Назад",
                style = AppTheme.typography.body.copy(color = AppTheme.colorScheme.text),
            )
        }
    }
}

@Composable
private fun InfoBoardChrome(
    currentTimeLabel: String,
    isCompact: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.padding(AppTheme.dimensions.basePadding)) {
        if (isCompact) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding / 2),
            ) {
                BoardBadge(
                    text = "Trauma Team Canteen Bar",
                    color = AppTheme.colorScheme.accentSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                BoardBadge(
                    text = "ОПЕРАТИВНАЯ ОЧЕРЕДЬ ЗАКАЗОВ",
                    color = AppTheme.colorScheme.warning,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding / 2),
            ) {
                BoardBadge(
                    text = currentTimeLabel,
                    color = AppTheme.colorScheme.accent,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                BoardBadge(
                    text = "ТАБЛО СБОРКИ // ОЧЕРЕДЬ И ВЫДАЧА",
                    color = AppTheme.colorScheme.accentSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BoardBadge(
                    text = "Trauma Team Canteen Bar",
                    color = AppTheme.colorScheme.accentSecondary,
                )
                BoardBadge(
                    text = "ОПЕРАТИВНАЯ ОЧЕРЕДЬ ЗАКАЗОВ",
                    color = AppTheme.colorScheme.warning,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BoardBadge(
                    text = currentTimeLabel,
                    color = AppTheme.colorScheme.accent,
                )
                BoardBadge(
                    text = "ТАБЛО СБОРКИ // ОЧЕРЕДЬ И ВЫДАЧА",
                    color = AppTheme.colorScheme.accentSecondary,
                )
            }
        }
    }
}

@Composable
private fun BoardBadge(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius * 1.6f))
            .background(color.copy(alpha = 0.16f))
            .border(
                width = AppTheme.dimensions.thinDivider * 2,
                color = color.copy(alpha = 0.78f),
                shape = RoundedCornerShape(AppTheme.dimensions.cornerRadius * 1.6f),
            )
            .padding(
                horizontal = AppTheme.dimensions.basePadding,
                vertical = AppTheme.dimensions.basePadding * 0.5f,
            ),
    ) {
        Text(
            text = text,
            color = AppTheme.colorScheme.text,
            textAlign = TextAlign.Center,
            style = AppTheme.typography.body.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
            ),
        )
    }
}

@Composable
private fun rememberInfoBoardTimeLabel(): String {
    var label by remember { mutableStateOf(formatInfoBoardTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            label = formatInfoBoardTime()
            delay(1000)
        }
    }

    return label
}

private fun formatInfoBoardTime(): String {
    return "ЛОКАЛЬНОЕ ВРЕМЯ // ${jsInfoBoardClock()}"
}

private fun jsInfoBoardClock(): String = js("new Date().toLocaleTimeString('ru-RU', { hour12: false })")

@Composable
private fun OrdersBox(
    ordersNames: List<String>,
    title: String,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    OrdersBoxScaffold(
        title = title,
        modifier = modifier,
    ) {
        if (ordersNames.isEmpty()) {
            AppStateMessage(
                title = "Пусто",
                description = "Новых заказов пока нет.",
            )
            return@OrdersBoxScaffold
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
        ) {
            val chunkedOrdersNames = ordersNames.chunked(5)
            val textStyle = if (chunkedOrdersNames.size > 2) AppTheme.typography.display else AppTheme.typography.displayLarge
            chunkedOrdersNames.forEach { ordersNames ->
                OrdersNamesColumn(
                    ordersNames = ordersNames,
                    textStyle = textStyle,
                    textColor = textColor,
                    modifier = Modifier
                        .widthIn(min = 220.dp)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun OrdersBoxScaffold(
    title: String,
    modifier: Modifier = Modifier,
    ordersNamesContent: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(AppTheme.dimensions.basePadding * 2))

        Text(
            text = title,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = AppTheme.typography.big,
        )

        Spacer(Modifier.height(AppTheme.dimensions.basePadding * 4))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            ordersNamesContent()
        }
    }
}

@Composable
private fun OrdersNamesColumn(
    ordersNames: List<String>,
    textStyle: TextStyle,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding * 2),
    ) {
        ordersNames.forEach { orderName ->
            Text(
                text = orderName,
                color = textColor,
                style = textStyle,
            )
        }
    }
}
