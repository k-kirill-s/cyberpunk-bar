package by.cyberpunkfandom.barfrontend.presentation.worker.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.OrderStatus
import by.cyberpunkfandom.barfrontend.domain.Worker
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBigButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppStateMessage
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderfinished.WorkerHomeOrderFinishedDialog
import by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderfinished.WorkerHomeOrderFinishedDialogState
import by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderstarted.WorkerHomeOrderStartedOrCancelledDialog
import by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderstarted.WorkerHomeOrderStartedOrCancelledDialogState
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkerHomeScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onOrderStarted: (orderId: Int) -> Unit,
    viewModel: WorkerHomeViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onOrderStarted.collect { workerId ->
            onOrderStarted(workerId)
        }
    }

    WorkerHomeContent(
        onBackClick = onBackRequest,
        worker = viewModel.worker.collectAsStateWithLifecycle().value,
        orderToCollect = viewModel.orderToCollect.collectAsStateWithLifecycle().value,
        isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value,
        isStartOrderLoading = viewModel.isStartOrderLoading.collectAsStateWithLifecycle().value,
        onStartOrderClick = viewModel::onStartOrderClick,
        orderFinishedDialogState = viewModel.orderFinishedDialogState.collectAsStateWithLifecycle().value,
        onOrderFinishedDialogDismissRequest = viewModel::onOrderFinishedDialogDismissRequest,
        orderStartedOrCancelledDialogState = viewModel.orderStartedOrCancelledDialogState.collectAsStateWithLifecycle().value,
        onOrderStartedDialogDismissRequest = viewModel::onOrderStartedDialogDismissRequest,
    )
}

@Composable
private fun WorkerHomeContent(
    onBackClick: () -> Unit,
    worker: Worker?,
    orderToCollect: OrderFull?,
    isLoading: Boolean,
    isStartOrderLoading: Boolean,
    onStartOrderClick: () -> Unit,
    orderFinishedDialogState: WorkerHomeOrderFinishedDialogState?,
    onOrderFinishedDialogDismissRequest: () -> Unit,
    orderStartedOrCancelledDialogState: WorkerHomeOrderStartedOrCancelledDialogState?,
    onOrderStartedDialogDismissRequest: () -> Unit,
) {
    val statusLabel = when {
        isLoading -> "СКАНИРОВАНИЕ"
        orderToCollect == null -> "ОЖИДАНИЕ"
        orderToCollect.status == OrderStatus.FORMED -> "НОВЫЙ ЗАКАЗ"
        else -> "В РАБОТЕ"
    }
    val statusColor = when {
        isLoading -> AppTheme.colorScheme.warning
        orderToCollect == null -> AppTheme.colorScheme.textSecondary
        orderToCollect.status == OrderStatus.FORMED -> AppTheme.colorScheme.accent
        else -> AppTheme.colorScheme.accentSecondary
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = worker?.name.orEmpty(),
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackClick,
        )

        AppHorizontalDivider()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(AppTheme.dimensions.basePadding),
        ) {
            val (title, color, enabled) = when {
                isLoading -> Triple("Загружаем очередь", AppTheme.colorScheme.surface, false)
                orderToCollect == null -> Triple("Нет доступных заказов", AppTheme.colorScheme.surface, false)
                orderToCollect.status == OrderStatus.FORMED -> Triple("Взять заказ №${orderToCollect.name}", AppTheme.colorScheme.accent, true)
                else -> Triple("Продолжить заказ №${orderToCollect.name}", AppTheme.colorScheme.accent, true)
            }
            val panelShape = RoundedCornerShape(AppTheme.dimensions.cornerRadius * 3)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(panelShape)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF171026),
                                AppTheme.colorScheme.background,
                            ),
                        ),
                    )
                    .border(
                        width = AppTheme.dimensions.thinDivider * 2,
                        color = AppTheme.colorScheme.accentSecondary.copy(alpha = 0.72f),
                        shape = panelShape,
                    ),
            ) {
                WorkerHomeBackdrop(modifier = Modifier.fillMaxSize())

                WorkerHomeDeckLabels(
                    workerName = worker?.name,
                    statusLabel = statusLabel,
                    statusColor = statusColor,
                    modifier = Modifier.fillMaxSize(),
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = AppTheme.dimensions.basePadding * 2,
                            vertical = AppTheme.dimensions.basePadding,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isLoading && orderToCollect == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(AppTheme.dimensions.bigButtonHeight * 1.6f)
                                .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius * 2))
                                .background(AppTheme.colorScheme.surfaceMuted.copy(alpha = 0.78f))
                                .border(
                                    width = AppTheme.dimensions.thinDivider * 2,
                                    color = AppTheme.colorScheme.dividerStrong.copy(alpha = 0.75f),
                                    shape = RoundedCornerShape(AppTheme.dimensions.cornerRadius * 2),
                                ),
                        ) {
                            AppStateMessage(
                                title = title,
                                modifier = Modifier.fillMaxWidth(),
                                isLoading = true,
                            )
                        }
                    } else {
                        AppBigButton(
                            title = title,
                            onClick = onStartOrderClick,
                            modifier = Modifier.fillMaxWidth(),
                            color = color,
                            enabled = enabled,
                            isLoading = isStartOrderLoading,
                        )
                    }
                }
            }
        }
    }

    orderFinishedDialogState?.let { state ->
        WorkerHomeOrderFinishedDialog(
            state = state,
            onDismissRequest = onOrderFinishedDialogDismissRequest,
        )
    }

    orderStartedOrCancelledDialogState?.let { state ->
        WorkerHomeOrderStartedOrCancelledDialog(
            state = state,
            onDismissRequest = onOrderStartedDialogDismissRequest,
        )
    }
}

@Composable
private fun WorkerHomeBackdrop(modifier: Modifier = Modifier) {
    val accent = AppTheme.colorScheme.accent
    val accentSecondary = AppTheme.colorScheme.accentSecondary

    Canvas(modifier = modifier) {
        val outerMargin = size.minDimension * 0.055f
        val innerMargin = outerMargin + size.minDimension * 0.022f

        drawCircle(
            color = accent.copy(alpha = 0.08f),
            radius = size.minDimension * 0.34f,
            center = Offset(size.width * 0.20f, size.height * 0.18f),
        )
        drawCircle(
            color = accentSecondary.copy(alpha = 0.09f),
            radius = size.minDimension * 0.26f,
            center = Offset(size.width * 0.76f, size.height * 0.24f),
        )

        drawHexGrid(
            color = accent,
            step = size.minDimension * 0.18f,
        )

        drawTelemetryCross(
            color = accentSecondary,
            center = Offset(size.width * 0.78f, size.height * 0.22f),
            size = size.minDimension * 0.16f,
        )

        drawRoundRect(
            color = accent.copy(alpha = 0.68f),
            topLeft = Offset(innerMargin, innerMargin),
            size = Size(size.width - innerMargin * 2, size.height - innerMargin * 2),
            cornerRadius = CornerRadius(size.minDimension * 0.03f),
            style = Stroke(width = size.minDimension * 0.0035f),
        )
        drawRoundRect(
            color = accentSecondary.copy(alpha = 0.54f),
            topLeft = Offset(outerMargin, outerMargin),
            size = Size(size.width - outerMargin * 2, size.height - outerMargin * 2),
            cornerRadius = CornerRadius(size.minDimension * 0.045f),
            style = Stroke(width = size.minDimension * 0.0045f),
        )

        drawLine(
            color = accent.copy(alpha = 0.55f),
            start = Offset(innerMargin, innerMargin + size.height * 0.08f),
            end = Offset(size.width - innerMargin, innerMargin + size.height * 0.08f),
            strokeWidth = size.minDimension * 0.003f,
        )
        drawLine(
            color = accent.copy(alpha = 0.55f),
            start = Offset(innerMargin, size.height - innerMargin - size.height * 0.12f),
            end = Offset(size.width - innerMargin, size.height - innerMargin - size.height * 0.12f),
            strokeWidth = size.minDimension * 0.003f,
        )

        drawCornerBrackets(
            color = accent,
            pad = outerMargin * 0.45f,
            length = size.minDimension * 0.07f,
            strokeWidth = size.minDimension * 0.004f,
        )
    }
}

@Composable
private fun WorkerHomeDeckLabels(
    workerName: String?,
    statusLabel: String,
    statusColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(AppTheme.dimensions.basePadding),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            WorkerHomePanelLabel(
                text = "ОЧЕРЕДЬ ЗАКАЗОВ",
                color = AppTheme.colorScheme.accent,
                modifier = Modifier.weight(1f),
            )

            WorkerHomePanelLabel(
                text = workerName?.uppercase().orEmpty().ifBlank { "РАБОЧАЯ СТАНЦИЯ" },
                color = AppTheme.colorScheme.accentSecondary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
            )
        }

        Box(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth()) {
            WorkerHomePanelLabel(
                text = "АВТООБНОВЛЕНИЕ",
                color = AppTheme.colorScheme.textSecondary,
                modifier = Modifier.weight(1f),
            )

            WorkerHomePanelLabel(
                text = statusLabel,
                color = statusColor,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
private fun WorkerHomePanelLabel(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        style = AppTheme.typography.body.copy(
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.2.sp,
        ),
    )
}

private fun DrawScope.drawTelemetryCross(
    color: Color,
    center: Offset,
    size: Float,
) {
    drawRoundRect(
        color = color.copy(alpha = 0.12f),
        topLeft = Offset(center.x - size * 0.14f, center.y - size * 0.5f),
        size = Size(size * 0.28f, size),
        cornerRadius = CornerRadius(size * 0.08f),
    )
    drawRoundRect(
        color = color.copy(alpha = 0.12f),
        topLeft = Offset(center.x - size * 0.5f, center.y - size * 0.14f),
        size = Size(size, size * 0.28f),
        cornerRadius = CornerRadius(size * 0.08f),
    )
}

private fun DrawScope.drawCornerBrackets(
    color: Color,
    pad: Float,
    length: Float,
    strokeWidth: Float,
) {
    drawLine(
        color = color,
        start = Offset(pad, pad),
        end = Offset(pad + length, pad),
        strokeWidth = strokeWidth,
    )
    drawLine(
        color = color,
        start = Offset(pad, pad),
        end = Offset(pad, pad + length),
        strokeWidth = strokeWidth,
    )
    drawLine(
        color = color,
        start = Offset(size.width - pad, pad),
        end = Offset(size.width - pad - length, pad),
        strokeWidth = strokeWidth,
    )
    drawLine(
        color = color,
        start = Offset(size.width - pad, pad),
        end = Offset(size.width - pad, pad + length),
        strokeWidth = strokeWidth,
    )
    drawLine(
        color = color,
        start = Offset(pad, size.height - pad),
        end = Offset(pad + length, size.height - pad),
        strokeWidth = strokeWidth,
    )
    drawLine(
        color = color,
        start = Offset(pad, size.height - pad),
        end = Offset(pad, size.height - pad - length),
        strokeWidth = strokeWidth,
    )
    drawLine(
        color = color,
        start = Offset(size.width - pad, size.height - pad),
        end = Offset(size.width - pad - length, size.height - pad),
        strokeWidth = strokeWidth,
    )
    drawLine(
        color = color,
        start = Offset(size.width - pad, size.height - pad),
        end = Offset(size.width - pad, size.height - pad - length),
        strokeWidth = strokeWidth,
    )
}

private fun DrawScope.drawHexGrid(
    color: Color,
    step: Float,
) {
    val radius = step / 2f
    val rowHeight = step * 0.86f
    var rowIndex = 0
    var y = -radius

    while (y < size.height + radius) {
        val rowOffset = if (rowIndex % 2 == 0) 0f else radius
        var x = -radius

        while (x < size.width + radius) {
            val centerX = x + rowOffset
            val path = Path().apply {
                for (pointIndex in 0 until 6) {
                    val angle = kotlin.math.PI / 3.0 * pointIndex + kotlin.math.PI / 6.0
                    val pointX = centerX + radius * kotlin.math.cos(angle).toFloat()
                    val pointY = y + radius * kotlin.math.sin(angle).toFloat()
                    if (pointIndex == 0) {
                        moveTo(pointX, pointY)
                    } else {
                        lineTo(pointX, pointY)
                    }
                }
                close()
            }

            drawPath(
                path = path,
                color = color.copy(alpha = 0.08f),
                style = Stroke(width = size.minDimension * 0.0018f),
            )

            x += step
        }

        y += rowHeight
        rowIndex += 1
    }
}
