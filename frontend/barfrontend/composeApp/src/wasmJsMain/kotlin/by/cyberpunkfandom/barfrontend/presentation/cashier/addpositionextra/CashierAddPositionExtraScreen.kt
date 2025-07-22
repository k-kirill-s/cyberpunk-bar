package by.cyberpunkfandom.barfrontend.presentation.cashier.addpositionextra

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
import by.cyberpunkfandom.barfrontend.domain.PositionExtra
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBoxButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppVerticalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CashierAddPositionExtraScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onPositionExtraItemAdded: (positionExtraItemId: Int) -> Unit,
    viewModel: CashierAddPositionExtraViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onPositionExtraItemAdded.collect { positionExtraItemId ->
            onPositionExtraItemAdded(positionExtraItemId)
        }
    }

    CashierAddPositionExtraScreen(
        onBackClick = onBackRequest,
        positionExtra = viewModel.positionExtra.collectAsStateWithLifecycle().value,
        isAddButtonLoading = viewModel.isAddButtonLoading.collectAsStateWithLifecycle().value,
        onAddButtonClick = viewModel::onAddButtonClick,
    )
}

@Composable
private fun CashierAddPositionExtraScreen(
    onBackClick: () -> Unit,
    positionExtra: List<PositionExtra>,
    isAddButtonLoading: Boolean,
    onAddButtonClick: (positionExtraId: String) -> Unit,
) {
    var selectedPositionExtra by remember { mutableStateOf<PositionExtra?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(onBackClick = onBackClick)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            ItemsColumn(
                positionExtra = positionExtra,
                selectedPositionExtra = selectedPositionExtra,
                onClick = { selectedPositionExtra = it },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )

            AppVerticalDivider()

            ItemDetails(
                positionExtra = selectedPositionExtra,
                isAddButtonLoading = isAddButtonLoading,
                onAddButtonClick = { selectedPositionExtra?.let { onAddButtonClick(it.id) } },
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
        title = "Добавление экстра",
        leftIcon = painterResource(Res.drawable.back_24dp),
        onLeftIconClick = onBackClick,
    )
}

@Composable
private fun ItemsColumn(
    positionExtra: List<PositionExtra>,
    selectedPositionExtra: PositionExtra?,
    onClick: (positionExtra: PositionExtra) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(positionExtra) { positionExtra ->
            val isSelected = positionExtra.id == selectedPositionExtra?.id
            Column {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(AppTheme.dimensions.itemHeight)
                        .background(color = if (isSelected) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.surface)
                        .clickable(onClick = { onClick(positionExtra) })
                        .padding(horizontal = AppTheme.dimensions.basePadding),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = positionExtra.name,
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
    positionExtra: PositionExtra?,
    isAddButtonLoading: Boolean,
    onAddButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (positionExtra == null) {
        Spacer(modifier)
    } else {
        Column(modifier = modifier) {
            Spacer(Modifier.height(AppTheme.dimensions.basePadding))

            // title
            Text(
                text = positionExtra.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimensions.basePadding),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.title,
            )

            Spacer(Modifier.weight(1f))

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
                        text = positionExtra.price.format(2),
                        style = AppTheme.typography.big,
                    )
                }

                AppBoxButton(
                    title = "Добавить",
                    onClick = onAddButtonClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    isLoading = isAddButtonLoading,
                )
            }
        }
    }
}
