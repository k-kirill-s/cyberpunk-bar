package by.cyberpunkfandom.barfrontend.presentation.cashier.addposition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.PositionItemsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionVariantsRepository
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
import kotlinx.coroutines.launch

class CashierAddPositionViewModel(
    private val orderId: Int,
    private val positionsRepository: PositionsRepository,
    private val positionVariantsRepository: PositionVariantsRepository,
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

    private val positions: MutableStateFlow<List<Position>> = MutableStateFlow(emptyList())

    private val contentState: MutableStateFlow<CashierAddPositionState.ContentState> = MutableStateFlow(CashierAddPositionState.ContentState.Loading)

    val state: CashierAddPositionState = CashierAddPositionState(
        contentState = contentState,
    )

    private var selectedPositionId: String? = null

    init {
        viewModelScope.launch(exceptionHandler) {
            positions.emit(positionsRepository.getActivePositions())
            contentState.emit(CashierAddPositionState.ContentState.ListContent.SelectPosition(positions.value))
        }
    }

    fun onAddPositionClick(positionId: String) {
        selectedPositionId = positionId
        viewModelScope.launch(exceptionHandler) {
            val selectPositionState = contentState.value as CashierAddPositionState.ContentState.ListContent.SelectPosition
            contentState.emit(selectPositionState.copy(isContinueButtonLoading = true))
            try {
                val position = positions.value.first { it.id == positionId }
                val positionVariants = positionVariantsRepository.getPositionVariants(positionId)
                contentState.emit(CashierAddPositionState.ContentState.ListContent.SelectPositionVariant(position, positionVariants))
            } catch (_: Exception) {
                contentState.emit(selectPositionState)
            }
        }
    }

    fun onAddPositionVariantClick(positionVariantId: String) {
        viewModelScope.launch(exceptionHandler) {
            val selectPositionVariantState = contentState.value as CashierAddPositionState.ContentState.ListContent.SelectPositionVariant
            contentState.emit(selectPositionVariantState.copy(isContinueButtonLoading = true))
            try {
                val positionItem = positionItemsRepository.addPositionToOrder(
                    orderId = orderId,
                    positionId = selectedPositionId!!,
                    positionVariantId = positionVariantId,
                )
                _onPositionItemAdded.send(positionItem.id)
            } finally {
                contentState.emit(selectPositionVariantState)
            }
        }
    }
}
