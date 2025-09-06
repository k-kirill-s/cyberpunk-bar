package by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.add_circle_24dp
import barfrontend.composeapp.generated.resources.delete_24dp
import by.cyberpunkfandom.barfrontend.core.format
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppDashedHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppIconButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppVerticalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CashierCreateOrderPositionItemRow(
    number: String,
    name: String,
    subname: String,
    price: Float,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ItemRow(
        price = price,
        onDeleteClick = onDeleteClick,
        title = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppTheme.dimensions.basePadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "$number $name ($subname)",
                    modifier = Modifier.weight(1f),
                    style = AppTheme.typography.title,
                )
            }
        },
        topDivider = { AppHorizontalDivider() },
        modifier = modifier,
    )
}

@Composable
private fun ItemRow(
    price: Float,
    onDeleteClick: () -> Unit,
    title: @Composable () -> Unit,
    topDivider: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(AppTheme.dimensions.itemHeight)
            .background(AppTheme.colorScheme.surface),
    ) {
        topDivider()

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                content = { title() },
            )

            AppVerticalDivider()

            Text(
                text = price.format(2),
                modifier = Modifier.width(200.dp),
                textAlign = TextAlign.Center,
                style = AppTheme.typography.title,
            )

            AppVerticalDivider()

            AppIconButton(
                painter = painterResource(Res.drawable.delete_24dp),
                onClick = onDeleteClick,
                modifier = Modifier.padding(horizontal = AppTheme.dimensions.basePadding),
            )
        }
    }
}
