package by.cyberpunkfandom.barfrontend.presentation.worker.orderconfirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.domain.exceptions.GeneralException
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WorkerOrderConfirmationViewModel(
    private val orderId: Int,
    private val ordersRepository: OrdersRepository,
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

    private val _onBackRequest: Channel<Unit> = Channel(Channel.BUFFERED)
    val onBackRequest: Flow<Unit> = _onBackRequest.receiveAsFlow()

    private val _onOrderFinished: Channel<Unit> = Channel(Channel.BUFFERED)
    val onOrderFinished: Flow<Unit> = _onOrderFinished.receiveAsFlow()

    val order: MutableStateFlow<OrderFull?> = MutableStateFlow(null)

    val isConfirming: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch(exceptionHandler) {
            order.emit(ordersRepository.getOrder(orderId))
        }
    }

    fun onConfirmClick() {
        viewModelScope.launch(exceptionHandler) {
            isConfirming.emit(true)
            try {
                ordersRepository.finishOrder(orderId)
                _onOrderFinished.send(Unit)
            } finally {
                isConfirming.emit(false)
            }
        }
    }

    fun onCancelClick() {
        _onBackRequest.trySend(Unit)
    }
}
