package by.cyberpunkfandom.barfrontend.presentation.cashier.addposition

import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionVariant
import kotlinx.coroutines.flow.StateFlow

data class CashierAddPositionState(
    val contentState: StateFlow<ContentState>,
) {

    sealed class ContentState {

        data object Loading : ContentState()

        sealed class ListContent(open val isContinueButtonLoading: Boolean) : ContentState() {

            data class SelectPosition(
                val positions: List<Position>,
                override val isContinueButtonLoading: Boolean = false,
            ) : ListContent(isContinueButtonLoading)

            data class SelectPositionVariant(
                val position: Position,
                val positionVariants: List<PositionVariant>,
                override val isContinueButtonLoading: Boolean = false,
            ) : ListContent(isContinueButtonLoading)
        }
    }
}
