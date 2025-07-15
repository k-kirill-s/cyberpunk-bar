package by.cyberpunkfandom.barfrontend.presentation.infoboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import by.cyberpunkfandom.barfrontend.domain.OrderStatus
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class InfoBoardViewModel(
    private val ordersRepository: OrdersRepository,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e("error", throwable)
    }

    val formedOrdersNames: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    val startedOrdersNames: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    val finishedOrdersNames: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    init {
        viewModelScope.launch(exceptionHandler) {
            while (true) {
                val activeOrders = ordersRepository.getActiveOrders().sortedBy { it.updatedAt }
                formedOrdersNames.emit(activeOrders.filter { it.status == OrderStatus.FORMED }.map { it.name })
                startedOrdersNames.emit(activeOrders.filter { it.status == OrderStatus.STARTED }.map { it.name })
                finishedOrdersNames.emit(activeOrders.filter { it.status == OrderStatus.FINISHED }.map { it.name })
                delay(3000L)
            }
        }
    }
}
