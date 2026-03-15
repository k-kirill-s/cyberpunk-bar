package by.cyberpunkfandom.barfrontend.presentation.worker.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.core.mapState
import by.cyberpunkfandom.barfrontend.core.nextAdaptiveRefreshDelay
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionItemsRepository
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.OrderStatus
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionItem
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.domain.exceptions.GeneralException
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkerOrderViewModel(
    private val orderId: Int,
    private val ordersRepository: OrdersRepository,
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

    private val _onCloseRequest: Channel<Unit> = Channel(Channel.BUFFERED)
    val onCloseRequest: Flow<Unit> = _onCloseRequest.receiveAsFlow()

    private val _onOrderFinished: Channel<Int> = Channel(Channel.BUFFERED)
    val onOrderFinished: Flow<Int> = _onOrderFinished.receiveAsFlow()

    private val _onPositionDetailsRequest: Channel<String> = Channel(Channel.BUFFERED)
    val onPositionDetailsRequest: Flow<String> = _onPositionDetailsRequest.receiveAsFlow()

    val order: MutableStateFlow<OrderFull?> = MutableStateFlow(null)
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val isDoneEnabled = order.mapState { currentOrder ->
        val positionItems = currentOrder?.positionItems.orEmpty()
        positionItems.isNotEmpty() && positionItems.all(PositionItem::isCompleted)
    }

    val isCloseDialogVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val isChangedDialogVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        startOrderRefreshLoop()
    }

    private fun startOrderRefreshLoop() {
        viewModelScope.launch(exceptionHandler) {
            while (true) {
                val changed = refreshOrder()
                isLoading.emit(false)
                val currentOrder = order.value
                if (currentOrder != null && currentOrder.status != OrderStatus.STARTED) {
                    isChangedDialogVisible.emit(true)
                    break
                }
                delay(nextAdaptiveRefreshDelay(changed = changed))
            }
        }
    }

    private suspend fun refreshOrder(): Boolean {
        val previousOrder = order.value
        val updatedOrder = ordersRepository.getOrder(orderId)
        order.emit(updatedOrder)
        return previousOrder != updatedOrder
    }

    fun onCloseClick() {
        isCloseDialogVisible.tryEmit(true)
    }

    fun onDoneClick() {
        _onOrderFinished.trySend(orderId)
    }

    fun onPositionDetailsClick(position: Position) {
        _onPositionDetailsRequest.trySend(position.id)
    }

    fun onPositionItemCompletedSwiped(positionItem: PositionItem) {
        updatePositionItem(positionItem.id, true)
    }

    fun onPositionItemCancelClick(positionItem: PositionItem) {
        updatePositionItem(positionItem.id, false)
    }

    fun onCloseDialogDismissRequest() {
        isCloseDialogVisible.update { false }
    }

    fun onCloseDialogConfirmClick() {
        isCloseDialogVisible.update { false }
        _onCloseRequest.trySend(Unit)
    }

    fun onChangedDialogDismissRequest() {
        isChangedDialogVisible.update { false }
        _onCloseRequest.trySend(Unit)
    }

    private fun updatePositionItem(positionItemId: Int, isCompleted: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            positionItemsRepository.setPositionItemCompleted(positionItemId, isCompleted)
            refreshOrder()
        }
    }
}
