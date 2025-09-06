package by.cyberpunkfandom.barfrontend.presentation.cashier.cancelorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import by.cyberpunkfandom.barfrontend.domain.Order
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.domain.exceptions.GeneralException
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CashierCancelOrderViewModel(
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

    val orders: MutableStateFlow<List<Order>> = MutableStateFlow(emptyList())

    val selectedOrderId: MutableStateFlow<Int?> = MutableStateFlow(null)

    val selectedOrder: MutableStateFlow<OrderFull?> = MutableStateFlow(null)

    val isCancelLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch(exceptionHandler) {
            selectedOrderId.collectLatest { id ->
                selectedOrder.update { id?.let { ordersRepository.getOrder(id) } }
            }
        }

        updateOrders()
    }

    fun onOrderClick(orderId: Int) {
        selectedOrderId.update { orderId }
    }

    fun onCancelClick() {
        val selectedOrderId = selectedOrderId.value ?: return

        viewModelScope.launch(exceptionHandler) {
            isCancelLoading.update { true }
            try {
                ordersRepository.declineOrder(selectedOrderId)
                this@CashierCancelOrderViewModel.selectedOrderId.update { null }
                updateOrders()
            } finally {
                isCancelLoading.update { false }
            }
        }
    }

    private fun updateOrders() {
        viewModelScope.launch(exceptionHandler) {
            orders.update { ordersRepository.getActiveOrders() }
        }
    }
}
