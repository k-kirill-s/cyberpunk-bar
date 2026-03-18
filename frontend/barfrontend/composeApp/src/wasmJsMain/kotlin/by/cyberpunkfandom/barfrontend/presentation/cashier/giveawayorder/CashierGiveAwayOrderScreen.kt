package by.cyberpunkfandom.barfrontend.presentation.cashier.giveawayorder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.core.format
import by.cyberpunkfandom.barfrontend.domain.Order
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.PositionItem
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBoxButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppDashedHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppVerticalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.DividerType
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CashierGiveAwayOrderScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    viewModel: CashierGiveAwayOrderViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    CashierGiveAwayOrderScreen(
        onBackClick = onBackRequest,
        orders = viewModel.orders.collectAsStateWithLifecycle().value,
        onOrderClick = viewModel::onOrderClick,
        selectedOrderId = viewModel.selectedOrderId.collectAsStateWithLifecycle().value,
        selectedOrder = viewModel.selectedOrder.collectAsStateWithLifecycle().value,
        isGiveAwayLoading = viewModel.isGiveAwayLoading.collectAsStateWithLifecycle().value,
        onGiveAwayClick = viewModel::onGiveAwayClick,
    )
}

@Composable
private fun CashierGiveAwayOrderScreen(
    onBackClick: () -> Unit,
    orders: List<Order>,
    onOrderClick: (orderId: Int) -> Unit,
    selectedOrderId: Int?,
    selectedOrder: OrderFull?,
    isGiveAwayLoading: Boolean,
    onGiveAwayClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        AppTopBar(
            modifier = Modifier.fillMaxWidth(),
            title = "Выдача заказов",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackClick,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            OrdersList(
                orders = orders,
                selectedOrderId = selectedOrderId,
                onOrderClick = onOrderClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
            AppVerticalDivider()
            OrderDetails(
                order = selectedOrder,
                isGiveAwayLoading = isGiveAwayLoading,
                onGiveAwayClick = onGiveAwayClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
        }
    }
}

@Composable
private fun OrdersList(
    orders: List<Order>,
    onOrderClick: (orderId: Int) -> Unit,
    selectedOrderId: Int?,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(orders) { order ->
            val isSelected = order.id == selectedOrderId
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppTheme.dimensions.itemHeight)
                        .background(color = if (isSelected) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.surface)
                        .border(
                            width = AppTheme.dimensions.thinDivider * 2,
                            color = if (isSelected) AppTheme.colorScheme.success else AppTheme.colorScheme.divider,
                            shape = RoundedCornerShape(AppTheme.dimensions.cornerRadius),
                        )
                        .clickable(onClick = { onOrderClick(order.id) })
                        .padding(horizontal = AppTheme.dimensions.basePadding),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = order.name,
                        style = AppTheme.typography.title,
                    )
                }

                AppHorizontalDivider()
            }
        }
    }
}

@Composable
private fun OrderDetails(
    order: OrderFull?,
    isGiveAwayLoading: Boolean,
    onGiveAwayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (order == null) {
        Spacer(modifier)
        return
    }

    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppTheme.dimensions.itemHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Заказ ${order.name}",
                        style = AppTheme.typography.title,
                    )
                }
            }

            item {
                AppHorizontalDivider()
            }

            orderDetailsPositionsItems(order)
        }

        AppHorizontalDivider()

        OrderDetailsBottomBar(
            price = order.price,
            isGiveAwayLoading = isGiveAwayLoading,
            onGiveAwayClick = onGiveAwayClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.dimensions.bottomBarHeight),
        )
    }
}

private fun LazyListScope.orderDetailsPositionsItems(order: OrderFull) {
    order.positionItems.forEachIndexed { positionItemIndex, positionItem ->
        item {
            AppHorizontalDivider(type = DividerType.THIN)
        }
        item {
            PositionItemRow(
                positionItem = positionItem,
                index = positionItemIndex,
            )
        }
    }

    item {
        AppHorizontalDivider(type = DividerType.THIN)
    }
}

@Composable
private fun PositionItemRow(
    positionItem: PositionItem,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.dimensions.itemHeight)
            .padding(horizontal = AppTheme.dimensions.basePadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${index + 1} ${positionItem.position.name} (${positionItem.positionVariant.name})",
            modifier = Modifier.weight(1f),
            style = AppTheme.typography.title,
        )
        Spacer(Modifier.width(AppTheme.dimensions.basePadding))
        Text(
            text = positionItem.price.format(2),
            style = AppTheme.typography.title,
        )
    }
}

@Composable
private fun OrderDetailsBottomBar(
    price: Float,
    isGiveAwayLoading: Boolean,
    onGiveAwayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = price.format(2),
                style = AppTheme.typography.big,
            )
        }

        AppBoxButton(
            title = "Заказ отдан",
            onClick = onGiveAwayClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            color = AppTheme.colorScheme.green,
            isLoading = isGiveAwayLoading,
        )
    }
}
