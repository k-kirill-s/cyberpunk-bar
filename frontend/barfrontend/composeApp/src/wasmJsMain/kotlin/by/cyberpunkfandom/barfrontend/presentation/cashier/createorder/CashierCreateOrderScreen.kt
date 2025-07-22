package by.cyberpunkfandom.barfrontend.presentation.cashier.createorder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.close_24dp
import by.cyberpunkfandom.barfrontend.core.format
import by.cyberpunkfandom.barfrontend.domain.PositionItem
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.composable.CashierCreateOrderPositionExtraItemRow
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.composable.CashierCreateOrderPositionItemRow
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.composable.dialogs.orderformed.CashierCreateOrderOrderFormedDialog
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.composable.dialogs.orderformed.CashierCreateOrderOrderFormedDialogState
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBoxButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CashierCreateOrderScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onCloseRequest: () -> Unit,
    onAddPositionRequest: () -> Unit,
    onAddPositionExtraRequest: (positionItemId: Int) -> Unit,
    onOrderFormed: () -> Unit,
    viewModel: CashierCreateOrderViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onCloseRequest.collect {
            onCloseRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onAddPositionRequest.collect {
            onAddPositionRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onAddPositionExtraRequest.collect { positionItemId ->
            onAddPositionExtraRequest(positionItemId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onOrderFormed.collect {
            onOrderFormed()
        }
    }

    CashierCreateOrderScreen(
        onCloseClick = viewModel::onCloseClick,
        positionItems = viewModel.positionItems.collectAsStateWithLifecycle().value,
        onPositionItemAddExtraClick = viewModel::onPositionItemAddExtraClick,
        onPositionItemDeleteClick = viewModel::onPositionItemDeleteClick,
        onPositionExtraItemDeleteClick = viewModel::onPositionExtraItemDeleteClick,
        totalPrice = viewModel.totalPrice.collectAsStateWithLifecycle().value,
        onAddPositionClick = viewModel::onAddPositionClick,
        isCreateOrderButtonLoading = viewModel.isCreateOrderButtonLoading.collectAsStateWithLifecycle().value,
        onCreateOrderButtonClick = viewModel::onCreateOrderButtonClick,
        orderFormedDialogState = viewModel.orderFormedDialogState.collectAsStateWithLifecycle().value,
        onOrderFormedDialogDismissRequest = viewModel::onOrderFormedDialogDismissRequest,
    )
}

@Composable
private fun CashierCreateOrderScreen(
    onCloseClick: () -> Unit,
    positionItems: List<PositionItem>,
    onPositionItemAddExtraClick: (positionItemId: Int) -> Unit,
    onPositionItemDeleteClick: (positionItemId: Int) -> Unit,
    onPositionExtraItemDeleteClick: (positionExtraItemId: Int) -> Unit,
    totalPrice: Float,
    onAddPositionClick: () -> Unit,
    isCreateOrderButtonLoading: Boolean,
    onCreateOrderButtonClick: () -> Unit,
    orderFormedDialogState: CashierCreateOrderOrderFormedDialogState?,
    onOrderFormedDialogDismissRequest: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopBar(
            onCloseClick = onCloseClick,
        )

        ItemsList(
            positionItems = positionItems,
            onPositionItemAddExtraClick = onPositionItemAddExtraClick,
            onPositionItemDeleteClick = onPositionItemDeleteClick,
            onPositionExtraItemDeleteClick = onPositionExtraItemDeleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

            )

        BottomBar(
            price = totalPrice,
            onAddPositionClick = onAddPositionClick,
            isCreateOrderButtonLoading = isCreateOrderButtonLoading,
            onCreateOrderButtonClick = onCreateOrderButtonClick,
        )
    }

    if (orderFormedDialogState != null) {
        CashierCreateOrderOrderFormedDialog(
            state = orderFormedDialogState,
            onDismissRequest = onOrderFormedDialogDismissRequest,
        )
    }
}

@Composable
private fun TopBar(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppTopBar(
        modifier = modifier,
        title = "Создание заказа",
        rightIcon = painterResource(Res.drawable.close_24dp),
        onRightIconClick = onCloseClick,
    )
}

@Composable
private fun ItemsList(
    positionItems: List<PositionItem>,
    onPositionItemAddExtraClick: (positionItemId: Int) -> Unit,
    onPositionItemDeleteClick: (positionItemId: Int) -> Unit,
    onPositionExtraItemDeleteClick: (positionExtraItemId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        positionItems.forEachIndexed { positionIndex, positionItem ->
            item {
                CashierCreateOrderPositionItemRow(
                    number = (positionIndex + 1).toString(),
                    name = positionItem.position.name,
                    price = positionItem.position.price,
                    onAddExtraClick = { onPositionItemAddExtraClick(positionItem.id) },
                    onDeleteClick = { onPositionItemDeleteClick(positionItem.id) },
                )
            }

            positionItem.extraItems.forEachIndexed { extraIndex, positionExtraItem ->
                item {
                    CashierCreateOrderPositionExtraItemRow(
                        number = "${positionIndex + 1}.${extraIndex + 1}",
                        name = positionExtraItem.positionExtra.name,
                        price = positionExtraItem.positionExtra.price,
                        onDeleteClick = { onPositionExtraItemDeleteClick(positionExtraItem.id) },
                    )
                }
            }
        }

        item {
            AppHorizontalDivider()
        }
    }
}

@Composable
private fun BottomBar(
    price: Float,
    onAddPositionClick: () -> Unit,
    isCreateOrderButtonLoading: Boolean,
    onCreateOrderButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .height(AppTheme.dimensions.bottomBarHeight)
    ) {
        AppHorizontalDivider(Modifier.fillMaxWidth())

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ) {
            PriceBox(
                price = price,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
            AppBoxButton(
                title = "Добавить позицию",
                onClick = onAddPositionClick,
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
            )
            AppBoxButton(
                title = "Создать заказ",
                onClick = onCreateOrderButtonClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                color = AppTheme.colorScheme.green,
                isLoading = isCreateOrderButtonLoading,
            )
        }
    }
}

@Composable
private fun PriceBox(
    price: Float,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.align(Alignment.Center)) {
            Text(
                text = "Итого: ",
                style = AppTheme.typography.title,
            )
            Text(
                text = price.format(2),
                style = AppTheme.typography.big,
            )
        }
    }
}
