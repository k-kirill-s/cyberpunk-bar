package by.cyberpunkfandom.barfrontend.presentation.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    leftIcon: Painter? = null,
    onLeftIconClick: () -> Unit = {},
    rightIcon: Painter? = null,
    onRightIconClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .height(AppTheme.dimensions.topBarHeight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            leftIcon?.let {
                AppIconButton(
                    painter = leftIcon,
                    onClick = onLeftIconClick,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = AppTheme.dimensions.basePadding),
                )
            }

            title?.let {
                Text(
                    text = title,
                    modifier = Modifier
                        .align(Alignment.Center),
                    style = AppTheme.typography.title,
                )
            }

            rightIcon?.let {
                AppIconButton(
                    painter = rightIcon,
                    onClick = onRightIconClick,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = AppTheme.dimensions.basePadding),
                )
            }
        }

        AppHorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
