package by.cyberpunkfandom.barfrontend.presentation.worker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.WorkersRepository
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.domain.exceptions.GeneralException
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WorkerViewModel(
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

    private val _onBackAllowed: Channel<Unit> = Channel(Channel.BUFFERED)
    val onBackAllowed: Flow<Unit> = _onBackAllowed.receiveAsFlow()

    private var activeWorkerId: Int? = null

    fun onWorkerSelected(workerId: Int) {
        activeWorkerId = workerId
    }

    fun onBackClick() {
        viewModelScope.launch(exceptionHandler) {
            activeWorkerId?.let { workersRepository.setWorkerIsOnLine(it, false) }
            activeWorkerId = null
            _onBackAllowed.send(Unit)
        }
    }
}
