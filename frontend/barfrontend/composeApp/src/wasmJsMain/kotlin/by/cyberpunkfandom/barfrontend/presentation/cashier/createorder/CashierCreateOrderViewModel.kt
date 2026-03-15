package by.cyberpunkfandom.barfrontend.presentation.cashier.createorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.core.mapState
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionItemsRepository
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.PositionItem
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.domain.exceptions.GeneralException
import by.cyberpunkfandom.barfrontend.presentation.cashier.createorder.composable.dialogs.orderformed.CashierCreateOrderOrderFormedDialogState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CashierCreateOrderViewModel(
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

    private val _onAddPositionRequest: Channel<Unit> = Channel(Channel.BUFFERED)
    val onAddPositionRequest: Flow<Unit> = _onAddPositionRequest.receiveAsFlow()

    private val _onOrderFormed: Channel<Unit> = Channel(Channel.BUFFERED)
    val onOrderFormed: Flow<Unit> = _onOrderFormed.receiveAsFlow()

    private val order: MutableStateFlow<OrderFull?> = MutableStateFlow(null)

    val positionItems: StateFlow<List<PositionItem>> = order.mapState { it?.positionItems ?: emptyList() }

    val totalPrice: StateFlow<Float> = order.mapState { it?.price ?: 0f }
    val isCreateOrderButtonEnabled: StateFlow<Boolean> = positionItems.mapState { it.isNotEmpty() }
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val isCreateOrderButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val orderFormedDialogState: MutableStateFlow<CashierCreateOrderOrderFormedDialogState?> = MutableStateFlow(null)

    init {
        viewModelScope.launch(exceptionHandler) {
            order.update { ordersRepository.getOrder(orderId) }
            isLoading.emit(false)
        }
    }

    fun onCloseClick() {
        _onCloseRequest.trySend(Unit)
    }

    fun onPositionItemDeleteClick(positionItemId: Int) {
        viewModelScope.launch(exceptionHandler) {
            positionItemsRepository.deletePositionItem(positionItemId)
            order.update { ordersRepository.getOrder(orderId) }
        }
    }

    fun onAddPositionClick() {
        _onAddPositionRequest.trySend(Unit)
    }

    fun onCreateOrderButtonClick() {
        if (positionItems.value.isEmpty()) {
            _onError.trySend(ExceptionCodes.ORDER_MUST_HAVE_ITEMS)
            return
        }

        viewModelScope.launch(exceptionHandler) {
            isCreateOrderButtonLoading.emit(true)
            try {
                val order = ordersRepository.formOrder(orderId)

                val orderFormedDialogState = CashierCreateOrderOrderFormedDialogState(orderName = order.name)
                this@CashierCreateOrderViewModel.orderFormedDialogState.emit(orderFormedDialogState)
            } finally {
                isCreateOrderButtonLoading.emit(false)
            }
        }
    }

    fun onOrderFormedDialogDismissRequest() {
        orderFormedDialogState.tryEmit(null)
        _onOrderFormed.trySend(Unit)
    }
}
