package by.cyberpunkfandom.barfrontend.presentation.cashier.addposition

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.core.format
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionVariant
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBoxButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppVerticalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CashierAddPositionScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onPositionItemAdded: (positionItemId: Int) -> Unit,
    viewModel: CashierAddPositionViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onPositionItemAdded.collect { positionItemId ->
            onPositionItemAdded(positionItemId)
        }
    }

    CashierAddPositionScreen(
        state = viewModel.state,
        onBackClick = onBackRequest,
        onAddPositionClick = viewModel::onAddPositionClick,
        onAddPositionVariantClick = viewModel::onAddPositionVariantClick,
    )
}

@Composable
private fun CashierAddPositionScreen(
    state: CashierAddPositionState,
    onBackClick: () -> Unit,
    onAddPositionClick: (positionId: String) -> Unit,
    onAddPositionVariantClick: (positionVariantId: String) -> Unit,
) {
    val contentState = state.contentState.collectAsStateWithLifecycle().value
    Column(modifier = Modifier.fillMaxSize()) {
        val title = (contentState as? CashierAddPositionState.ContentState.ListContent.SelectPositionVariant)?.position?.name
        TopBar(
            title = title,
            onBackClick = onBackClick,
        )

        when (contentState) {

            is CashierAddPositionState.ContentState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().weight(1f))
            }

            is CashierAddPositionState.ContentState.ListContent.SelectPosition -> {
                ListContentComponent(
                    items = contentState.positions,
                    isAddItemButtonLoading = contentState.isContinueButtonLoading,
                    onAddItemClick = onAddPositionClick,
                    itemId = Position::id,
                    itemName = Position::name,
                    itemDescription = Position::description,
                    itemPrice = { null },
                    modifier = Modifier.fillMaxWidth().weight(1f),
                )
            }

            is CashierAddPositionState.ContentState.ListContent.SelectPositionVariant -> {
                ListContentComponent(
                    items = contentState.positionVariants,
                    isAddItemButtonLoading = contentState.isContinueButtonLoading,
                    onAddItemClick = onAddPositionVariantClick,
                    itemId = PositionVariant::id,
                    itemName = PositionVariant::name,
                    itemDescription = { " " },
                    itemPrice = PositionVariant::price,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    title: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppTopBar(
        modifier = modifier,
        title = title ?: "Добавление позиции",
        leftIcon = painterResource(Res.drawable.back_24dp),
        onLeftIconClick = onBackClick,
    )
}

@Composable
private fun <T> ListContentComponent(
    items: List<T>,
    isAddItemButtonLoading: Boolean,
    onAddItemClick: (id: String) -> Unit,
    itemId: T.() -> String,
    itemName: T.() -> String,
    itemDescription: T.() -> String,
    itemPrice: T.() -> Float?,
    modifier: Modifier = Modifier,
) {
    var selectedItem by remember { mutableStateOf<T?>(null) }

    Row(
        modifier = modifier,
    ) {
        ItemsColumn(
            items = items,
            selectedItem = selectedItem,
            onClick = { selectedItem = it },
            itemId = itemId,
            itemName = itemName,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )

        AppVerticalDivider()

        ItemDetails(
            item = selectedItem,
            isAddItemButtonLoading = isAddItemButtonLoading,
            onAddItemButtonClick = { selectedItem?.let { onAddItemClick(it.itemId()) } },
            itemName = itemName,
            itemDescription = itemDescription,
            itemPrice = itemPrice,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )
    }
}

@Composable
private fun <T> ItemsColumn(
    items: List<T>,
    selectedItem: T?,
    onClick: (item: T) -> Unit,
    itemId: T.() -> String,
    itemName: T.() -> String,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(items) { item ->
            val isSelected = item.itemId() == selectedItem?.itemId()
            Column {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(AppTheme.dimensions.itemHeight)
                        .background(color = if (isSelected) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.surface)
                        .clickable(onClick = { onClick(item) })
                        .padding(horizontal = AppTheme.dimensions.basePadding),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = item.itemName(),
                        modifier = Modifier,
                        style = AppTheme.typography.title,
                    )
                }

                AppHorizontalDivider()
            }
        }
    }
}

@Composable
private fun <T> ItemDetails(
    item: T?,
    isAddItemButtonLoading: Boolean,
    onAddItemButtonClick: () -> Unit,
    itemName: T.() -> String,
    itemDescription: T.() -> String,
    itemPrice: T.() -> Float?,
    modifier: Modifier = Modifier,
) {
    if (item == null) {
        Spacer(modifier)
    } else {
        Column(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(AppTheme.dimensions.basePadding))

                // title
                Text(
                    text = item.itemName(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimensions.basePadding),
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.title,
                )

                Spacer(Modifier.height(AppTheme.dimensions.basePadding))

                // description
                Text(
                    text = item.itemDescription(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimensions.basePadding),
                    textAlign = TextAlign.Justify,
                    style = AppTheme.typography.body,
                )

                Spacer(Modifier.height(AppTheme.dimensions.basePadding))
            }

            AppHorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.dimensions.bottomBarHeight)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    item.itemPrice()?.let { price ->
                        Text(
                            text = price.format(2),
                            style = AppTheme.typography.big,
                        )
                    }

                }

                AppBoxButton(
                    title = "Добавить",
                    onClick = onAddItemButtonClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    isLoading = isAddItemButtonLoading,
                )
            }
        }
    }
}
