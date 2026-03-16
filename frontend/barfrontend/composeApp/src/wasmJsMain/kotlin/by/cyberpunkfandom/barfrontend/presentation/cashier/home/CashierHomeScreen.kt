package by.cyberpunkfandom.barfrontend.presentation.cashier.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBigButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CashierHomeScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    onOpenCreateOrderRequest: (orderId: Int) -> Unit,
    onGiveAwayOrderRequest: () -> Unit,
    onCancelOrderRequest: () -> Unit,
    viewModel: CashierHomeViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onOpenCreateOrderRequest.collect { onOpenCreateOrderRequest(it) }
    }

    CashierHomeScreen(
        onBackClick = onBackRequest,
        onCreateOrderClick = viewModel::onCreateOrderClick,
        onGiveAwayOrderClick = onGiveAwayOrderRequest,
        onCancelOrderClick = onCancelOrderRequest,
    )
}

@Composable
private fun CashierHomeScreen(
    onBackClick: () -> Unit,
    onCreateOrderClick: () -> Unit,
    onGiveAwayOrderClick: () -> Unit,
    onCancelOrderClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        val compactSpacing = AppTheme.dimensions.basePadding

        AppTopBar(
            title = "Кассир",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackClick,
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(AppTheme.dimensions.basePadding)
        ) {
            val useGridLayout = maxWidth >= 400.dp

            if (useGridLayout) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(compactSpacing),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(compactSpacing),
                    ) {
                        AppBigButton(
                            title = "Создать заказ",
                            onClick = onCreateOrderClick,
                            modifier = Modifier.weight(1f),
                            color = AppTheme.colorScheme.accent,
                        )

                        AppBigButton(
                            title = "Выдача заказа",
                            onClick = onGiveAwayOrderClick,
                            modifier = Modifier.weight(1f),
                            color = AppTheme.colorScheme.green,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(compactSpacing),
                    ) {
                        AppBigButton(
                            title = "Отменить заказ",
                            onClick = onCancelOrderClick,
                            modifier = Modifier.weight(1f),
                            color = AppTheme.colorScheme.red,
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(compactSpacing)
                ) {
                    AppBigButton(
                        title = "Создать заказ",
                        onClick = onCreateOrderClick,
                        modifier = Modifier.fillMaxWidth(),
                        color = AppTheme.colorScheme.accent,
                    )

                    AppBigButton(
                        title = "Выдача заказа",
                        onClick = onGiveAwayOrderClick,
                        modifier = Modifier.fillMaxWidth(),
                        color = AppTheme.colorScheme.green,
                    )

                    AppBigButton(
                        title = "Отменить заказ",
                        onClick = onCancelOrderClick,
                        modifier = Modifier.fillMaxWidth(),
                        color = AppTheme.colorScheme.red,
                    )
                }
            }
        }
    }
}
