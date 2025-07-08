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
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBoxButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppVerticalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CashierAddPositionScreen(
    onBackRequest: () -> Unit,
    onPositionItemAdded: (positionItemId: Int) -> Unit,
    viewModel: CashierAddPositionViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onPositionItemAdded.collect { positionItemId ->
            onPositionItemAdded(positionItemId)
        }
    }

    CashierAddPositionScreen(
        onBackClick = onBackRequest,
        positions = viewModel.positions.collectAsStateWithLifecycle().value,
        isAddPositionButtonLoading = viewModel.isAddPositionButtonLoading.collectAsStateWithLifecycle().value,
        onAddPositionClick = viewModel::onAddPositionClick,
    )
}

@Composable
private fun CashierAddPositionScreen(
    onBackClick: () -> Unit,
    positions: List<Position>,
    isAddPositionButtonLoading: Boolean,
    onAddPositionClick: (positionId: String) -> Unit,
) {
    var selectedPosition by remember { mutableStateOf<Position?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(onBackClick = onBackClick)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            ItemsColumn(
                positions = positions,
                selectedPosition = selectedPosition,
                onClick = { selectedPosition = it },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )

            AppVerticalDivider()

            ItemDetails(
                position = selectedPosition,
                isAddPositionButtonLoading = isAddPositionButtonLoading,
                onAddPositionButtonClick = { selectedPosition?.let { onAddPositionClick(it.id) } },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
        }
    }
}

@Composable
private fun TopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppTopBar(
        modifier = modifier,
        title = "Добавление позиции",
        leftIcon = painterResource(Res.drawable.back_24dp),
        onLeftIconClick = onBackClick,
    )
}

@Composable
private fun ItemsColumn(
    positions: List<Position>,
    selectedPosition: Position?,
    onClick: (position: Position) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(positions) { position ->
            val isSelected = position.id == selectedPosition?.id
            Column {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(AppTheme.dimensions.itemHeight)
                        .background(color = if (isSelected) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.surface)
                        .clickable(onClick = { onClick(position) })
                        .padding(horizontal = AppTheme.dimensions.basePadding),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = position.name,
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
private fun ItemDetails(
    position: Position?,
    isAddPositionButtonLoading: Boolean,
    onAddPositionButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (position == null) {
        Spacer(modifier)
    } else {
        Column(modifier = modifier) {
            Spacer(Modifier.height(AppTheme.dimensions.basePadding))

            // title
            Text(
                text = position.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimensions.basePadding),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.title,
            )

            Spacer(Modifier.height(AppTheme.dimensions.basePadding))

            // description
            Text(
                text = position.description,
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
                        text = position.price.format(2),
                        style = AppTheme.typography.big,
                    )
                }

                AppBoxButton(
                    title = "Добавить",
                    onClick = onAddPositionButtonClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    isLoading = isAddPositionButtonLoading,
                )
            }
        }
    }
}
