package by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions

import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionVariant
import kotlinx.coroutines.flow.StateFlow

data class CashierTogglePositionsState(
    val contentState: ContentState,
) {

    data class ContentState(
        val positions: StateFlow<Map<Position, List<PositionVariant>>>,
        val selectedPositionId: StateFlow<String?>,
        val selectedPositionVariantId: StateFlow<String?>,
        val isToggleButtonEnabled: StateFlow<Boolean>,
        val isToggleButtonLoading: StateFlow<Boolean>,
    )
}
