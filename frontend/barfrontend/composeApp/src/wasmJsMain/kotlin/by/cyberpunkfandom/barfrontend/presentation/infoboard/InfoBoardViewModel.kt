package by.cyberpunkfandom.barfrontend.presentation.infoboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.core.nextAdaptiveRefreshDelay
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
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        startRefreshLoop()
    }

    private fun startRefreshLoop() {
        viewModelScope.launch(exceptionHandler) {
            while (true) {
                val changed = refreshOrders()
                isLoading.emit(false)
                delay(nextAdaptiveRefreshDelay(changed = changed, hiddenMs = 30_000L))
            }
        }
    }

    private suspend fun refreshOrders(): Boolean {
        return runCatching {
            val previousSnapshot = Triple(formedOrdersNames.value, startedOrdersNames.value, finishedOrdersNames.value)
            val activeOrders = ordersRepository.getActiveOrders().sortedBy { it.updatedAt }
            val formed = activeOrders.filter { it.status == OrderStatus.FORMED }.map { it.name }
            val started = activeOrders.filter { it.status == OrderStatus.STARTED }.map { it.name }
            val finished = activeOrders.filter { it.status == OrderStatus.FINISHED }.map { it.name }

            formedOrdersNames.emit(formed)
            startedOrdersNames.emit(started)
            finishedOrdersNames.emit(finished)
            errorMessage.emit(null)

            previousSnapshot != Triple(formed, started, finished)
        }.getOrElse { throwable ->
            Napier.e("error", throwable)
            errorMessage.emit("Не удалось обновить табло")
            false
        }
    }
}
