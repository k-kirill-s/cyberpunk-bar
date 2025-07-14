package by.cyberpunkfandom.barfrontend.presentation.worker.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.check_24dp
import barfrontend.composeapp.generated.resources.close_24dp
import barfrontend.composeapp.generated.resources.help_24dp
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionExtraItem
import by.cyberpunkfandom.barfrontend.domain.PositionItem
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBoxButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppDashedHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppIconButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppSwipeToActionBox
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.components.DividerType
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import by.cyberpunkfandom.barfrontend.presentation.worker.order.composables.dialogs.close.WorkerOrderCloseDialog
import org.jetbrains.compose.resources.painterResource

@Composable
fun WorkerOrderScreen(
    onCloseRequest: () -> Unit,
    onOrderFinished: (Int) -> Unit,
    onPositionDetailsRequest: (positionId: String) -> Unit,
    viewModel: WorkerOrderViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onCloseRequest.collect {
            onCloseRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onOrderFinished.collect { orderId ->
            onOrderFinished(orderId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onPositionDetailsRequest.collect { positionId ->
            onPositionDetailsRequest(positionId)
        }
    }

    WorkerOrderScreen(
        onCloseClick = viewModel::onCloseClick,
        order = viewModel.order.collectAsStateWithLifecycle().value,
        completedPositionItems = viewModel.completedPositionItems.collectAsStateWithLifecycle().value,
        onPositionDetailsClick = viewModel::onPositionDetailsClick,
        onPositionItemCompletedSwiped = viewModel::onPositionItemCompletedSwiped,
        onPositionItemCancelClick = viewModel::onPositionItemCancelClick,
        onDoneClick = viewModel::onDoneClick,
        isCloseDialogVisible = viewModel.isCloseDialogVisible.collectAsStateWithLifecycle().value,
        onCloseDialogDismissRequest = viewModel::onCloseDialogDismissRequest,
        onCloseDialogConfirmClick = viewModel::onCloseDialogConfirmClick,
    )
}

@Composable
private fun WorkerOrderScreen(
    onCloseClick: () -> Unit,
    order: OrderFull?,
    completedPositionItems: List<PositionItem>,
    onPositionDetailsClick: (position: Position) -> Unit,
    onPositionItemCompletedSwiped: (positionItem: PositionItem) -> Unit,
    onPositionItemCancelClick: (positionItem: PositionItem) -> Unit,
    onDoneClick: () -> Unit,
    isCloseDialogVisible: Boolean,
    onCloseDialogDismissRequest: () -> Unit,
    onCloseDialogConfirmClick: () -> Unit,
) {
    order ?: run {
        Spacer(Modifier.fillMaxSize())
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Заказ №${order.name}",
            rightIcon = painterResource(Res.drawable.close_24dp),
            onRightIconClick = onCloseClick,
        )

        AppHorizontalDivider()

        OrderContentColumn(
            order = order,
            completedPositionItems = completedPositionItems,
            onPositionDetailsClick = onPositionDetailsClick,
            onPositionItemCompletedSwiped = onPositionItemCompletedSwiped,
            onPositionItemCancelClick = onPositionItemCancelClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        AppBoxButton(
            title = "Готово",
            onClick = onDoneClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.dimensions.bottomBarHeight),
            color = AppTheme.colorScheme.green,
        )
    }

    if (isCloseDialogVisible) {
        WorkerOrderCloseDialog(
            onDismissRequest = onCloseDialogDismissRequest,
            onConfirmClick = onCloseDialogConfirmClick,
        )
    }
}

@Composable
private fun OrderContentColumn(
    order: OrderFull,
    completedPositionItems: List<PositionItem>,
    onPositionDetailsClick: (position: Position) -> Unit,
    onPositionItemCompletedSwiped: (positionItem: PositionItem) -> Unit,
    onPositionItemCancelClick: (positionItem: PositionItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        order.positionItems.forEachIndexed { index, positionItem ->
            item {
                PositionItemRow(
                    positionItem = positionItem,
                    index = index,
                    completed = positionItem in completedPositionItems,
                    onDetailsClick = { onPositionDetailsClick(positionItem.position) },
                    onCompletedSwiped = { onPositionItemCompletedSwiped(positionItem) },
                    onCancelClick = { onPositionItemCancelClick(positionItem) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                AppHorizontalDivider(type = DividerType.THIN)
            }
        }
    }
}

@Composable
private fun PositionItemRow(
    positionItem: PositionItem,
    index: Int,
    completed: Boolean,
    onDetailsClick: () -> Unit,
    onCompletedSwiped: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppSwipeToActionBox(
        swipeEnabled = !completed,
        onSwiped = onCompletedSwiped,
        swipeIcon = painterResource(Res.drawable.check_24dp),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .background(color = if (completed) AppTheme.colorScheme.green else AppTheme.colorScheme.surface)
                .padding(AppTheme.dimensions.basePadding)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${index + 1} ${positionItem.position.name}",
                    style = AppTheme.typography.title,
                )

                if (positionItem.extraItems.isNotEmpty()) {
                    Spacer(Modifier.height(AppTheme.dimensions.basePadding))

                    PositionExtraItemsColumn(
                        positionExtraItems = positionItem.extraItems,
                        positionItemIndex = index,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = AppTheme.dimensions.basePadding * 2)
                    )
                }
            }

            val (iconRes, onClick) = if (completed) {
                Res.drawable.close_24dp to onCancelClick
            } else {
                Res.drawable.help_24dp to onDetailsClick
            }
            AppIconButton(
                painter = painterResource(iconRes),
                onClick = onClick,
            )
        }
    }

}

@Composable
private fun PositionExtraItemsColumn(
    positionExtraItems: List<PositionExtraItem>,
    positionItemIndex: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        positionExtraItems.forEachIndexed { index, item ->
            Text(
                text = "${positionItemIndex + 1}.${index + 1} ${item.positionExtra.name}",
                style = AppTheme.typography.title,
            )

            if (index != positionExtraItems.lastIndex) {
                Spacer(Modifier.height(AppTheme.dimensions.basePadding / 2))
                AppDashedHorizontalDivider(type = DividerType.THIN)
                Spacer(Modifier.height(AppTheme.dimensions.basePadding / 2))
            }
        }
    }
}
