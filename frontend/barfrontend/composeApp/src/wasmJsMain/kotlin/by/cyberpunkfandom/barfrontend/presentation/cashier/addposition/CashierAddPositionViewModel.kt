package by.cyberpunkfandom.barfrontend.presentation.cashier.addposition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.PositionItemsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionsRepository
import by.cyberpunkfandom.barfrontend.domain.Position
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

class CashierAddPositionViewModel(
    private val orderId: Int,
    private val positionsRepository: PositionsRepository,
    private val positionItemsRepository: PositionItemsRepository,
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

    private val _onPositionItemAdded: Channel<Int> = Channel(Channel.BUFFERED)
    val onPositionItemAdded: Flow<Int> = _onPositionItemAdded.receiveAsFlow()

    val positions: MutableStateFlow<List<Position>> = MutableStateFlow(emptyList())

    val isAddPositionButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch(exceptionHandler) {
            positions.update { positionsRepository.getPositions() }
        }
    }

    fun onAddPositionClick(positionId: String) {
        viewModelScope.launch(exceptionHandler) {
            isAddPositionButtonLoading.emit(true)
            try {
                val positionItem = positionItemsRepository.addPositionToOrder(
                    orderId = orderId,
                    positionId = positionId,
                )
                _onPositionItemAdded.send(positionItem.id)
            } finally {
                isAddPositionButtonLoading.emit(false)
            }
        }
    }
}
