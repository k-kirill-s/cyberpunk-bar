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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import barfrontend.composeapp.generated.resources.check_24dp
import barfrontend.composeapp.generated.resources.remove_24dp
import by.cyberpunkfandom.barfrontend.core.format
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionVariant
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBoxButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppIcon
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppVerticalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import io.github.aakira.napier.Napier
import org.jetbrains.compose.resources.painterResource

@Composable
fun CashierTogglePositionsScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    viewModel: CashierTogglePositionsViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    CashierTogglePositionsScreen(
        onBackClick = onBackRequest,
        positions = viewModel.state.contentState.positions.collectAsStateWithLifecycle().value,
        selectedPositionId = viewModel.state.contentState.selectedPositionId.collectAsStateWithLifecycle().value,
        selectedPositionVariantId = viewModel.state.contentState.selectedPositionVariantId.collectAsStateWithLifecycle().value,
        onPositionClick = viewModel::onPositionClick,
        onPositionVariantsClick = viewModel::onPositionVariantClick,
        isToggleButtonEnabled = viewModel.state.contentState.isToggleButtonEnabled.collectAsStateWithLifecycle().value,
        isToggleButtonLoading = viewModel.state.contentState.isToggleButtonLoading.collectAsStateWithLifecycle().value,
        onToggleButtonClick = viewModel::onToggleButtonClick,
    )
}

@Composable
private fun CashierTogglePositionsScreen(
    onBackClick: () -> Unit,
    positions: Map<Position, List<PositionVariant>>,
    selectedPositionId: String?,
    selectedPositionVariantId: String?,
    onPositionClick: (position: Position) -> Unit,
    onPositionVariantsClick: (positionVariant: PositionVariant) -> Unit,
    isToggleButtonEnabled: Boolean,
    isToggleButtonLoading: Boolean,
    onToggleButtonClick: (isActive: Boolean) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(onBackClick = onBackClick)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            PositionsColumn(
                positionsWithVariants = positions,
                selectedPositionId = selectedPositionId,
                onClick = onPositionClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )

            AppVerticalDivider()

            val detailsModifier = Modifier
                .weight(1f)
                .fillMaxHeight()

            if (selectedPositionId != null) {
                PositionVariantsBlock(
                    positionVariants = positions.entries.firstOrNull { it.key.id == selectedPositionId }?.value.orEmpty(),
                    selectedPositionVariantId = selectedPositionVariantId,
                    onPositionVariantsClick = onPositionVariantsClick,
                    isToggleButtonEnabled = isToggleButtonEnabled,
                    isToggleButtonLoading = isToggleButtonLoading,
                    onToggleButtonClick = onToggleButtonClick,
                    modifier = detailsModifier,
                )
            } else {
                Spacer(detailsModifier)
            }
        }
    }
}

@Composable
private fun TopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = "Включение/выключение позиций"
    AppTopBar(
        modifier = modifier,
        title = title,
        leftIcon = painterResource(Res.drawable.back_24dp),
        onLeftIconClick = onBackClick,
    )
}

@Composable
private fun PositionsColumn(
    positionsWithVariants: Map<Position, List<PositionVariant>>,
    selectedPositionId: String?,
    onClick: (item: Position) -> Unit,
    modifier: Modifier = Modifier,
) {
    val positions = positionsWithVariants.keys.toList()
    LazyColumn(modifier = modifier) {
        items(positions) { position ->
            val isSelected = position.id == selectedPositionId
            val isActive = positionsWithVariants[position].orEmpty().count { it.isActive } > 0
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(AppTheme.dimensions.itemHeight)
                        .background(color = if (isSelected) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.surface)
                        .clickable(onClick = { onClick(position) })
                        .padding(horizontal = AppTheme.dimensions.basePadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = position.name,
                        modifier = Modifier.weight(1f),
                        style = AppTheme.typography.title,
                    )

                    Spacer(Modifier.width(AppTheme.dimensions.basePadding))

                    val (iconRes, iconColor) = if (isActive) {
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
private fun PositionVariantsBlock(
    positionVariants: List<PositionVariant>,
    selectedPositionVariantId: String?,
    onPositionVariantsClick: (item: PositionVariant) -> Unit,
    isToggleButtonEnabled: Boolean,
    isToggleButtonLoading: Boolean,
    onToggleButtonClick: (isActive: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedPositionVariant = selectedPositionVariantId?.let { variantId ->
        positionVariants.firstOrNull { it.id == variantId }
    }

    Column(modifier = modifier) {

        PositionVariantsColumn(
            positionVariants = positionVariants,
            selectedPositionVariantId = selectedPositionVariantId,
            onClick = onPositionVariantsClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
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
                if (selectedPositionVariant != null) {
                    Text(
                        text = selectedPositionVariant.price.format(2),
                        style = AppTheme.typography.big,
                    )
                }
            }

            if (isToggleButtonEnabled && selectedPositionVariant != null) {
                val title = if (selectedPositionVariant.isActive) "Выключить" else "Включить"
                val color = if (selectedPositionVariant.isActive) AppTheme.colorScheme.red else AppTheme.colorScheme.green
                AppBoxButton(
                    title = title,
                    onClick = { onToggleButtonClick(!selectedPositionVariant.isActive) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    color = color,
                    isLoading = isToggleButtonLoading,
                )
            } else {
                Spacer(Modifier.weight(1f).fillMaxHeight())
            }
        }
    }
}

@Composable
private fun PositionVariantsColumn(
    positionVariants: List<PositionVariant>,
    selectedPositionVariantId: String?,
    onClick: (item: PositionVariant) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(positionVariants) { positionVariant ->
            val isSelected = positionVariant.id == selectedPositionVariantId
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(AppTheme.dimensions.itemHeight)
                        .background(color = if (isSelected) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.surface)
                        .clickable(onClick = { onClick(positionVariant) })
                        .padding(horizontal = AppTheme.dimensions.basePadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = positionVariant.name,
                        modifier = Modifier.weight(1f),
                        style = AppTheme.typography.title,
                    )

                    Spacer(Modifier.width(AppTheme.dimensions.basePadding))

                    val (iconRes, iconColor) = if (positionVariant.isActive) {
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
