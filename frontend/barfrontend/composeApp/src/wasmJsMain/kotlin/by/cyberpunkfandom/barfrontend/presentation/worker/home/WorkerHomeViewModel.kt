package by.cyberpunkfandom.barfrontend.presentation.worker.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.OrdersRepository
import by.cyberpunkfandom.barfrontend.data.repositories.WorkersRepository
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.OrderStatus
import by.cyberpunkfandom.barfrontend.domain.Worker
import by.cyberpunkfandom.barfrontend.domain.exceptions.OrderAlreadyStartedException
import by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderfinished.WorkerHomeOrderFinishedDialogState
import by.cyberpunkfandom.barfrontend.presentation.worker.home.composable.dialogs.orderstarted.WorkerHomeOrderStartedDialogState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WorkerHomeViewModel(
    private val workerId: Int,
    private val ordersRepository: OrdersRepository,
    private val workersRepository: WorkersRepository,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e("error", throwable)
    }

    private val _onOrderStarted: Channel<Int> = Channel(Channel.BUFFERED)
    val onOrderStarted: Flow<Int> = _onOrderStarted.receiveAsFlow()

    val worker: MutableStateFlow<Worker?> = MutableStateFlow(null)

    val orderToCollect: MutableStateFlow<OrderFull?> = MutableStateFlow(null)

    val isStartOrderLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val orderFinishedDialogState: MutableStateFlow<WorkerHomeOrderFinishedDialogState?> = MutableStateFlow(null)

    val orderStartedDialogState: MutableStateFlow<WorkerHomeOrderStartedDialogState?> = MutableStateFlow(null)

    init {
        clearAndStartObservingOrderToCollect()

        viewModelScope.launch(exceptionHandler) {
            worker.emit(workersRepository.getWorkers().first { it.id == workerId })
        }
    }

    private fun clearAndStartObservingOrderToCollect() {
        viewModelScope.launch(exceptionHandler) {
            orderToCollect.emit(null)
            while (true) {
                loadOrderToCollect()
                if (orderToCollect.value != null) break
                delay(3000L)
            }
        }
    }

    private suspend fun loadOrderToCollect() {
        val inProgressOrder = ordersRepository.getInProgressOrderByWorker(workerId)
        if (inProgressOrder != null) {
            orderToCollect.emit(inProgressOrder)
        } else {
            val nextOrderToCollect = ordersRepository.getNextOrderToCollect()
            if (nextOrderToCollect != null) {
                orderToCollect.emit(nextOrderToCollect)
            }
        }
    }

    fun onStartOrderClick() {
        val orderToCollect = orderToCollect.value ?: return

        viewModelScope.launch(exceptionHandler) {
            isStartOrderLoading.emit(true)
            try {
                if (orderToCollect.status == OrderStatus.STARTED) {
                    if (ordersRepository.getOrder(orderToCollect.id).status != OrderStatus.STARTED) {
                        orderFinishedDialogState.emit(WorkerHomeOrderFinishedDialogState(orderToCollect.name))
                        clearAndStartObservingOrderToCollect()
                    } else {
                        _onOrderStarted.send(orderToCollect.id)
                    }
                } else {
                    try {
                        ordersRepository.startOrder(
                            orderId = orderToCollect.id,
                            workerId = workerId,
                        )
                        _onOrderStarted.send(orderToCollect.id)
                    } catch (e: OrderAlreadyStartedException) {
                        orderStartedDialogState.emit(WorkerHomeOrderStartedDialogState(orderToCollect.name))
                        clearAndStartObservingOrderToCollect()
                    }
                }
            } finally {
                isStartOrderLoading.emit(false)
            }
        }
    }

    fun onOrderFinishedDialogDismissRequest() {
        orderFinishedDialogState.tryEmit(null)
    }

    fun onOrderStartedDialogDismissRequest() {
        orderStartedDialogState.tryEmit(null)
    }
}
