package by.cyberpunkfandom.barfrontend.presentation.infoboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppVerticalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun InfoBoardScreen(viewModel: InfoBoardViewModel) {
    InfoBoardScreen(
        formedOrdersNames = viewModel.formedOrdersNames.collectAsStateWithLifecycle().value,
        startedOrdersNames = viewModel.startedOrdersNames.collectAsStateWithLifecycle().value,
        finishedOrdersNames = viewModel.finishedOrdersNames.collectAsStateWithLifecycle().value,
    )
}

@Composable
private fun InfoBoardScreen(
    formedOrdersNames: List<String>,
    startedOrdersNames: List<String>,
    finishedOrdersNames: List<String>,
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {

        OrdersBox(
            ordersNames = formedOrdersNames,
            title = "В очереди",
            textColor = AppTheme.colorScheme.text,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )

        AppVerticalDivider()

        OrdersBox(
            ordersNames = startedOrdersNames,
            title = "В процессе",
            textColor = AppTheme.colorScheme.text,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )

        AppVerticalDivider()

        OrdersBox(
            ordersNames = finishedOrdersNames,
            title = "Готовы",
            textColor = AppTheme.colorScheme.green,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )
    }
}

@Composable
private fun OrdersBox(
    ordersNames: List<String>,
    title: String,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    OrdersBoxScaffold(
        title = title,
        modifier = modifier,
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            val chunkedOrdersNames = ordersNames.chunked(5)
            val textStyle = if (chunkedOrdersNames.size > 2) AppTheme.typography.display else AppTheme.typography.displayLarge
            chunkedOrdersNames.take(3).forEach { ordersNames ->
                OrdersNamesColumn(
                    ordersNames = ordersNames,
                    textStyle = textStyle,
                    textColor = textColor,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun OrdersBoxScaffold(
    title: String,
    modifier: Modifier = Modifier,
    ordersNamesContent: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(AppTheme.dimensions.basePadding * 2))

        Text(
            text = title,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = AppTheme.typography.big,
        )

        Spacer(Modifier.height(AppTheme.dimensions.basePadding * 4))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            ordersNamesContent()
        }
    }
}

@Composable
private fun OrdersNamesColumn(
    ordersNames: List<String>,
    textStyle: TextStyle,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding * 2),
    ) {
        ordersNames.forEach { orderName ->
            Text(
                text = orderName,
                color = textColor,
                style = textStyle,
            )
        }
    }
}
