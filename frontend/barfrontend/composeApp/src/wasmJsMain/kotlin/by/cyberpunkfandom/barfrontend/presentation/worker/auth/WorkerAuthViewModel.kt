package by.cyberpunkfandom.barfrontend.presentation.worker.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.WorkersRepository
import by.cyberpunkfandom.barfrontend.domain.Worker
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.domain.exceptions.GeneralException
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WorkerAuthViewModel(
    private val workersRepository: WorkersRepository,
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

    private val _onWorkerSelected: Channel<Int> = Channel(Channel.BUFFERED)
    val onWorkerSelected: Flow<Int> = _onWorkerSelected.receiveAsFlow()

    val workers: MutableStateFlow<List<Worker>> = MutableStateFlow(emptyList())
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)

    init {
        viewModelScope.launch(exceptionHandler) {
            workers.emit(getSortedBartenders())
            isLoading.emit(false)
        }
    }

    fun onWorkerClick(worker: Worker) {
        viewModelScope.launch(exceptionHandler) {
            workersRepository.setWorkerIsOnLine(worker.id, true)
            workers.emit(getSortedBartenders())
            _onWorkerSelected.send(worker.id)
        }
    }

    private suspend fun getSortedBartenders(): List<Worker> {
        return workersRepository
            .getWorkers()
            .filter(Worker::canBeBartender)
            .sortedWith(compareByDescending<Worker> { it.isOnLine }.thenBy { it.name })
    }
}
