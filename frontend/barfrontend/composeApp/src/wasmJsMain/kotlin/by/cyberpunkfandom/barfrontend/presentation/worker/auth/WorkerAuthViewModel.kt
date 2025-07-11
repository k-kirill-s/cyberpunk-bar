package by.cyberpunkfandom.barfrontend.presentation.worker.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.WorkersRepository
import by.cyberpunkfandom.barfrontend.domain.Worker
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
    }

    private val _onWorkerSelected: Channel<Int> = Channel(Channel.BUFFERED)
    val onWorkerSelected: Flow<Int> = _onWorkerSelected.receiveAsFlow()

    val workers: MutableStateFlow<List<Worker>> = MutableStateFlow(emptyList())

    init {
        viewModelScope.launch(exceptionHandler) {
            workers.emit(workersRepository.getWorkers())
        }
    }

    fun onWorkerClick(worker: Worker) {
        _onWorkerSelected.trySend(worker.id)
    }
}
