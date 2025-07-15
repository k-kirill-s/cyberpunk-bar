package by.cyberpunkfandom.barfrontend.presentation.cashier.giveawayorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import by.cyberpunkfandom.barfrontend.domain.Order
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.OrderStatus
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CashierGiveAwayOrderViewModel(
    private val ordersRepository: OrdersRepository,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e("error", throwable)
    }

    val orders: MutableStateFlow<List<Order>> = MutableStateFlow(emptyList())

    val selectedOrderId: MutableStateFlow<Int?> = MutableStateFlow(null)

    val selectedOrder: MutableStateFlow<OrderFull?> = MutableStateFlow(null)

    val isGiveAwayLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

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

    fun onGiveAwayClick() {
        val selectedOrderId = selectedOrderId.value ?: return

        viewModelScope.launch(exceptionHandler) {
            isGiveAwayLoading.update { true }
            try {
                ordersRepository.giveAwayOrder(selectedOrderId)
                this@CashierGiveAwayOrderViewModel.selectedOrderId.update { null }
                updateOrders()
            } finally {
                isGiveAwayLoading.update { false }
            }
        }
    }

    private fun updateOrders() {
        viewModelScope.launch(exceptionHandler) {
            orders.update {
                ordersRepository.getActiveOrders().filter { it.status == OrderStatus.FINISHED }
            }
        }
    }
}
