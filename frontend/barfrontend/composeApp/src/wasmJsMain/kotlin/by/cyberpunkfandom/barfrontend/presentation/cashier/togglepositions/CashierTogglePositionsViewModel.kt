package by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.PositionVariantsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.WorkersRepository
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionVariant
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

class CashierTogglePositionsViewModel(
    private val positionsRepository: PositionsRepository,
    private val positionVariantsRepository: PositionVariantsRepository,
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

    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isSaving: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val workers: MutableStateFlow<List<Worker>> = MutableStateFlow(emptyList())
    val positions: MutableStateFlow<List<Position>> = MutableStateFlow(emptyList())
    val positionVariants: MutableStateFlow<List<PositionVariant>> = MutableStateFlow(emptyList())

    val selectedWorkerId: MutableStateFlow<Int?> = MutableStateFlow(null)
    val selectedPositionId: MutableStateFlow<String?> = MutableStateFlow(null)
    val selectedPositionVariantId: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        viewModelScope.launch(exceptionHandler) {
            refreshData()
            isLoading.emit(false)
        }
    }

    fun onWorkerClick(worker: Worker) {
        selectedWorkerId.value = worker.id
    }

    fun onPositionClick(position: Position) {
        selectedPositionId.value = position.id
        selectedPositionVariantId.value = null
        viewModelScope.launch(exceptionHandler) {
            positionVariants.emit(loadPositionVariants(position.id))
        }
    }

    fun onPositionVariantClick(positionVariant: PositionVariant) {
        selectedPositionVariantId.value = positionVariant.id
    }

    fun createWorker(name: String) = mutateCatalog {
        val worker = workersRepository.createWorker(name.trim())
        refreshData(selectWorkerId = worker.id)
    }

    fun updateWorker(
        workerId: Int,
        name: String,
        isOnLine: Boolean,
    ) = mutateCatalog {
        workersRepository.updateWorker(
            workerId = workerId,
            name = name.trim(),
            isOnLine = isOnLine,
        )
        refreshData(selectWorkerId = workerId)
    }

    fun deleteWorker(workerId: Int) = mutateCatalog {
        workersRepository.deleteWorker(workerId)
        refreshData(
            selectWorkerId = null,
        )
    }

    fun createPosition(
        id: String,
        name: String,
        description: String,
    ) = mutateCatalog {
        val position = positionsRepository.createPosition(
            id = id.trim(),
            name = name.trim(),
            description = description.trim(),
        )
        refreshData(selectPositionId = position.id)
    }

    fun updatePosition(
        positionId: String,
        name: String,
        description: String,
    ) = mutateCatalog {
        positionsRepository.updatePosition(
            positionId = positionId,
            name = name.trim(),
            description = description.trim(),
        )
        refreshData(selectPositionId = positionId, selectVariantId = selectedPositionVariantId.value)
    }

    fun deletePosition(positionId: String) = mutateCatalog {
        positionsRepository.deletePosition(positionId)
        refreshData(
            selectPositionId = positions.value.firstOrNull { it.id != positionId }?.id,
            selectVariantId = null,
        )
    }

    fun createPositionVariant(
        positionId: String,
        id: String,
        name: String,
        price: Float,
    ) = mutateCatalog {
        val variant = positionVariantsRepository.createPositionVariant(
            positionId = positionId,
            id = id.trim(),
            name = name.trim(),
            price = price,
        )
        refreshData(
            selectPositionId = positionId,
            selectVariantId = variant.id,
        )
    }

    fun updatePositionVariant(
        positionVariantId: String,
        name: String,
        price: Float,
        isActive: Boolean,
    ) = mutateCatalog {
        positionVariantsRepository.updatePositionVariant(
            positionVariantId = positionVariantId,
            name = name.trim(),
            price = price,
            isActive = isActive,
        )
        refreshData(
            selectPositionId = selectedPositionId.value,
            selectVariantId = positionVariantId,
        )
    }

    fun deletePositionVariant(positionVariantId: String) = mutateCatalog {
        positionVariantsRepository.deletePositionVariant(positionVariantId)
        refreshData(
            selectPositionId = selectedPositionId.value,
            selectVariantId = positionVariants.value.firstOrNull { it.id != positionVariantId }?.id,
        )
    }

    private fun mutateCatalog(block: suspend () -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            isSaving.emit(true)
            try {
                block()
            } finally {
                isSaving.emit(false)
                isLoading.emit(false)
            }
        }
    }

    private suspend fun refreshData(
        selectWorkerId: Int? = selectedWorkerId.value,
        selectPositionId: String? = selectedPositionId.value,
        selectVariantId: String? = selectedPositionVariantId.value,
    ) {
        val updatedWorkers = workersRepository.getWorkers().sortedBy { it.name.lowercase() }
        val updatedPositions = positionsRepository.getPositions().sortedBy { it.name.lowercase() }

        val resolvedPositionId = when {
            updatedPositions.isEmpty() -> null
            selectPositionId != null && updatedPositions.any { it.id == selectPositionId } -> selectPositionId
            else -> updatedPositions.first().id
        }

        val updatedVariants = resolvedPositionId?.let { loadPositionVariants(it) }.orEmpty()
        val resolvedVariantId = selectVariantId?.takeIf { candidateId ->
            updatedVariants.any { it.id == candidateId }
        }

        workers.emit(updatedWorkers)
        positions.emit(updatedPositions)
        positionVariants.emit(updatedVariants)

        selectedWorkerId.emit(selectWorkerId?.takeIf { candidateId ->
            updatedWorkers.any { it.id == candidateId }
        })
        selectedPositionId.emit(resolvedPositionId)
        selectedPositionVariantId.emit(resolvedVariantId)
    }

    private suspend fun loadPositionVariants(positionId: String): List<PositionVariant> {
        return positionVariantsRepository
            .getPositionVariants(positionId, withNotActive = true)
            .sortedBy { it.name.lowercase() }
    }
}
