package by.cyberpunkfandom.barfrontend.presentation.cashier.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
    onTogglePositionsRequest: () -> Unit,
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
        onTogglePositionsClick = onTogglePositionsRequest,
    )
}

@Composable
private fun CashierHomeScreen(
    onBackClick: () -> Unit,
    onCreateOrderClick: () -> Unit,
    onGiveAwayOrderClick: () -> Unit,
    onCancelOrderClick: () -> Unit,
    onTogglePositionsClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        AppTopBar(
            title = "Кассир",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(AppTheme.dimensions.basePadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding)
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

            AppBigButton(
                title = "Каталог и сотрудники",
                onClick = onTogglePositionsClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
