package by.cyberpunkfandom.barfrontend.presentation.worker.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionItem
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkerOrderViewModel(
    private val orderId: Int,
    private val ordersRepository: OrdersRepository,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e("error", throwable)
    }
    private val _onCloseRequest: Channel<Unit> = Channel(Channel.BUFFERED)
    val onCloseRequest: Flow<Unit> = _onCloseRequest.receiveAsFlow()

    private val _onOrderFinished: Channel<Int> = Channel(Channel.BUFFERED)
    val onOrderFinished: Flow<Int> = _onOrderFinished.receiveAsFlow()

    private val _onPositionDetailsRequest: Channel<String> = Channel(Channel.BUFFERED)
    val onPositionDetailsRequest: Flow<String> = _onPositionDetailsRequest.receiveAsFlow()

    val order: MutableStateFlow<OrderFull?> = MutableStateFlow(null)

    val completedPositionItems: MutableStateFlow<List<PositionItem>> = MutableStateFlow(emptyList())

    val isCloseDialogVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch(exceptionHandler) {
            order.emit(ordersRepository.getOrder(orderId))
        }
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
        completedPositionItems.update { it + positionItem }
    }

    fun onPositionItemCancelClick(positionItem: PositionItem) {
        completedPositionItems.update { it - positionItem }
    }

    fun onCloseDialogDismissRequest() {
        isCloseDialogVisible.update { false }
    }

    fun onCloseDialogConfirmClick() {
        isCloseDialogVisible.update { false }
        _onCloseRequest.trySend(Unit)
    }
}
