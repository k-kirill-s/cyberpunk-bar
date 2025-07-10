package by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import barfrontend.composeapp.generated.resources.check_24dp
import barfrontend.composeapp.generated.resources.remove_24dp
import by.cyberpunkfandom.barfrontend.core.format
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBoxButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppIcon
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppVerticalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CashierTogglePositionsScreen(
    onBackRequest: () -> Unit,
    viewModel: CashierTogglePositionsViewModel,
) {
    CashierTogglePositionsScreen(
        type = viewModel.type,
        onBackClick = onBackRequest,
        items = viewModel.items.collectAsStateWithLifecycle().value,
        isToggleLoading = viewModel.isToggleLoading.collectAsStateWithLifecycle().value,
        onToggleClick = viewModel::onToggleClick,
    )
}

@Composable
private fun CashierTogglePositionsScreen(
    type: CashierTogglePositionsScreenType,
    onBackClick: () -> Unit,
    items: List<CashierTogglePositionsViewModel.ItemData>,
    isToggleLoading: Boolean,
    onToggleClick: (item: CashierTogglePositionsViewModel.ItemData) -> Unit,
) {
    var selectedItemId by remember { mutableStateOf<String?>(null) }
    val selectedItem = items.firstOrNull { it.id == selectedItemId }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            type = type,
            onBackClick = onBackClick,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            ItemsColumn(
                items = items,
                selectedItem = selectedItem,
                onClick = { selectedItemId = it.id },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )

            AppVerticalDivider()

            val detailsModifier = Modifier
                .weight(1f)
                .fillMaxHeight()
            selectedItem?.let { selectedItem ->
                ItemDetails(
                    item = selectedItem,
                    isToggleLoading = isToggleLoading,
                    onToggleClick = { onToggleClick(selectedItem) },
                    modifier = detailsModifier,
                )
            } ?: Spacer(detailsModifier)

        }
    }
}

@Composable
private fun TopBar(
    type: CashierTogglePositionsScreenType,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = when (type) {
        CashierTogglePositionsScreenType.POSITIONS -> "Включение/выключение позиций"
        CashierTogglePositionsScreenType.POSITION_EXTRA -> "Включение/выключение экстра"
    }
    AppTopBar(
        modifier = modifier,
        title = title,
        leftIcon = painterResource(Res.drawable.back_24dp),
        onLeftIconClick = onBackClick,
    )
}

@Composable
private fun ItemsColumn(
    items: List<CashierTogglePositionsViewModel.ItemData>,
    selectedItem: CashierTogglePositionsViewModel.ItemData?,
    onClick: (item: CashierTogglePositionsViewModel.ItemData) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(items) { item ->
            val isSelected = item == selectedItem
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(AppTheme.dimensions.itemHeight)
                        .background(color = if (isSelected) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.surface)
                        .clickable(onClick = { onClick(item) })
                        .padding(horizontal = AppTheme.dimensions.basePadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = item.name,
                        modifier = Modifier.weight(1f),
                        style = AppTheme.typography.title,
                    )

                    Spacer(Modifier.width(AppTheme.dimensions.basePadding))

                    val (iconRes, iconColor) = if (item.isActive) {
                        Res.drawable.check_24dp to AppTheme.colorScheme.green
                    } else {
                        Res.drawable.remove_24dp to AppTheme.colorScheme.red
                    }
                    AppIcon(
                        painter = painterResource(iconRes),
                        tint = iconColor,
                    )
                }

                AppHorizontalDivider()
            }
        }
    }
}

@Composable
private fun ItemDetails(
    item: CashierTogglePositionsViewModel.ItemData,
    isToggleLoading: Boolean,
    onToggleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(AppTheme.dimensions.basePadding))

        // title
        Text(
            text = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimensions.basePadding),
            textAlign = TextAlign.Center,
            style = AppTheme.typography.title,
        )

        Spacer(Modifier.height(AppTheme.dimensions.basePadding))

        // description
        Text(
            text = item.description,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = AppTheme.dimensions.basePadding),
            textAlign = TextAlign.Justify,
            style = AppTheme.typography.body,
        )

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
                Text(
                    text = item.price.format(2),
                    style = AppTheme.typography.big,
                )
            }

            val title = if (item.isActive) "Выключить" else "Включить"
            val color = if (item.isActive) AppTheme.colorScheme.red else AppTheme.colorScheme.green
            AppBoxButton(
                title = title,
                onClick = onToggleClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                color = color,
                isLoading = isToggleLoading,
            )
        }
    }
}
