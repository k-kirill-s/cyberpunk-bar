package by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.PositionExtraRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionsRepository
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionExtra
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.domain.exceptions.GeneralException
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CashierTogglePositionsViewModel(
    val type: CashierTogglePositionsScreenType,
    private val positionsRepository: PositionsRepository,
    private val positionExtraRepository: PositionExtraRepository,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e("error", throwable)
        if (throwable is GeneralException) {
            _onError.trySend(throwable.code)
        } else {
            _onError.trySend(ExceptionCodes.UNKNOWN)
        }
    }

    private val _onError: Channel<ExceptionCodes> = Channel(Channel.BUFFERED)
    val onError: Flow<ExceptionCodes> = _onError.receiveAsFlow()

    val items: MutableStateFlow<List<ItemData>> = MutableStateFlow(emptyList())

    val isToggleLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        updateData()
    }

    fun onToggleClick(item: ItemData) {
        viewModelScope.launch(exceptionHandler) {
            isToggleLoading.emit(true)
            try {
                when (type) {
                    CashierTogglePositionsScreenType.POSITIONS -> positionsRepository.setPositionIsActive(item.id, !item.isActive)
                    CashierTogglePositionsScreenType.POSITION_EXTRA -> positionExtraRepository.setPositionExtraIsActive(item.id, !item.isActive)
                }
                updateData()
            } finally {
                isToggleLoading.emit(false)
            }
        }
    }

    private fun updateData() {
        viewModelScope.launch(exceptionHandler) {
            when (type) {
                CashierTogglePositionsScreenType.POSITIONS -> {
                    items.update { positionsRepository.getPositions(true).map { it.toItemData() } }
                }

                CashierTogglePositionsScreenType.POSITION_EXTRA -> {
                    items.update { positionExtraRepository.getPositionExtra(true).map { it.toItemData() } }
                }
            }
        }
    }

    private fun Position.toItemData(): ItemData = ItemData(
        id = id,
        name = name,
        isActive = isActive,
        description = description,
        price = price,
    )

    private fun PositionExtra.toItemData(): ItemData = ItemData(
        id = id,
        name = name,
        isActive = isActive,
        description = "",
        price = price,
    )

    data class ItemData(
        val id: String,
        val name: String,
        val isActive: Boolean,
        val description: String,
        val price: Float,
    )
}
