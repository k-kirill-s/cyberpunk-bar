package by.cyberpunkfandom.barfrontend.presentation.cashier.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

class CashierHomeViewModel(
    private val ordersRepository: OrdersRepository,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e("error", throwable)
    }

    private val _onOpenCreateOrderRequest: Channel<Int> = Channel(Channel.BUFFERED)
    val onOpenCreateOrderRequest: Flow<Int> = _onOpenCreateOrderRequest.receiveAsFlow()

    private var lastTimeCreateOrderClick: Instant = Instant.fromEpochMilliseconds(0)

    fun onCreateOrderClick() {
        if (Clock.System.now() - lastTimeCreateOrderClick < 3000.milliseconds) return
        lastTimeCreateOrderClick = Clock.System.now()

        viewModelScope.launch(exceptionHandler) {
            val orderId = ordersRepository.createOrder().id
            _onOpenCreateOrderRequest.trySend(orderId)
        }
    }
}
