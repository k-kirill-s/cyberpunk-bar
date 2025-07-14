package by.cyberpunkfandom.barfrontend.presentation.worker.positiondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.PositionsRepository
import by.cyberpunkfandom.barfrontend.domain.Position
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WorkerPositionDetailsViewModel(
    private val positionId: String,
    private val positionsRepository: PositionsRepository,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e("error", throwable)
    }

    val position: MutableStateFlow<Position?> = MutableStateFlow(null)

    init {
        viewModelScope.launch(exceptionHandler) {
            position.emit(positionsRepository.getPositions(true).first { it.id == positionId })
        }
    }
}
