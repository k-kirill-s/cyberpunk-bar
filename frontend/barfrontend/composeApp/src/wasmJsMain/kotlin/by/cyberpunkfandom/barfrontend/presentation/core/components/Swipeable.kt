package by.cyberpunkfandom.barfrontend.presentation.core.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme

@Composable
fun AppSwipeToActionBox(
    swipeEnabled: Boolean,
    onSwiped: () -> Unit,
    swipeIcon: Painter,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) onSwiped()
            false
        },
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> {
                    AppIcon(
                        painter = swipeIcon,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(AppTheme.dimensions.basePadding)
                    )
                }

                else -> {}
            }
        },
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = swipeEnabled,
    ) {
        content()
    }
}
