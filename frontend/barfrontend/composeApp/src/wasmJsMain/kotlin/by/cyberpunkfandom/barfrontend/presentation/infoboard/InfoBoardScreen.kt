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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppIcon
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppVerticalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppStateMessage
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
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

        BackButtonOverlay(
            isVisible = showBackButton,
            onClick = onBackRequest,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(AppTheme.dimensions.basePadding),
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
