package by.cyberpunkfandom.barfrontend.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.AdminRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionVariantsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.WorkersRepository
import by.cyberpunkfandom.barfrontend.domain.AdminAnalytics
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

class AdminCatalogViewModel(
    private val adminRepository: AdminRepository,
    private val positionsRepository: PositionsRepository,
    private val positionVariantsRepository: PositionVariantsRepository,
    private val workersRepository: WorkersRepository,
) : ViewModel() {
    private var hasLoaded = false

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e("admin catalog error", throwable)
        if (throwable is GeneralException) {
            _onError.trySend(throwable.code)
        } else {
            _onError.trySend(ExceptionCodes.UNKNOWN)
        }
    }

    private val _onError: Channel<ExceptionCodes> = Channel(Channel.BUFFERED)
    val onError: Flow<ExceptionCodes> = _onError.receiveAsFlow()

    val isLoading = MutableStateFlow(true)
    val isSaving = MutableStateFlow(false)
    val isAnalyticsRefreshing = MutableStateFlow(false)

    val workers = MutableStateFlow<List<Worker>>(emptyList())
    val positions = MutableStateFlow<List<Position>>(emptyList())
    val positionVariants = MutableStateFlow<List<PositionVariant>>(emptyList())
    val selectedPositionVariantIds = MutableStateFlow<Set<String>>(emptySet())
    val analytics = MutableStateFlow<AdminAnalytics?>(null)

    val selectedWorkerId = MutableStateFlow<Int?>(null)
    val selectedPositionId = MutableStateFlow<String?>(null)
    val selectedPositionVariantId = MutableStateFlow<String?>(null)

    fun ensureLoaded() {
        if (hasLoaded) return

        viewModelScope.launch(exceptionHandler) {
            isLoading.emit(true)
            try {
                refreshData()
                hasLoaded = true
            } finally {
                isLoading.emit(false)
            }
        }
    }

    fun selectWorker(worker: Worker) {
        selectWorkerById(worker.id)
    }

    fun selectWorkerById(workerId: Int?) {
        selectedWorkerId.value = workerId?.takeIf { candidateId ->
            workers.value.any { it.id == candidateId }
        }
    }

    fun selectPosition(position: Position) {
        selectPositionById(position.id)
    }

    fun selectPositionById(positionId: String?) {
        viewModelScope.launch(exceptionHandler) {
            refreshData(
                selectPositionId = positionId,
                selectVariantId = selectedPositionVariantId.value,
            )
        }
    }

    fun selectPositionVariant(positionVariant: PositionVariant) {
        selectPositionVariantById(positionVariant.id)
    }

    fun selectPositionVariantById(positionVariantId: String?) {
        selectedPositionVariantId.value = positionVariantId?.takeIf { candidateId ->
            positionVariants.value.any { it.id == candidateId }
        }
    }

    fun onAnalyticsRefreshClick() {
        viewModelScope.launch(exceptionHandler) {
            if (!adminRepository.isAuthorized()) {
                analytics.emit(null)
                return@launch
            }

            isAnalyticsRefreshing.emit(true)
            try {
                analytics.emit(adminRepository.getAnalytics())
            } finally {
                isAnalyticsRefreshing.emit(false)
            }
        }
    }

    fun createWorker(
        name: String,
        canBeCashier: Boolean,
        canBeBartender: Boolean,
        onSuccess: () -> Unit = {},
    ) = mutateCatalog(onSuccess) {
        val worker = workersRepository.createWorker(
            name = name.trim(),
            canBeCashier = canBeCashier,
            canBeBartender = canBeBartender,
        )
        refreshData(selectWorkerId = worker.id)
    }

    fun updateWorker(
        workerId: Int,
        name: String,
        isOnLine: Boolean,
        canBeCashier: Boolean,
        canBeBartender: Boolean,
        onSuccess: () -> Unit = {},
    ) = mutateCatalog(onSuccess) {
        workersRepository.updateWorker(
            workerId = workerId,
            name = name.trim(),
            isOnLine = isOnLine,
            canBeCashier = canBeCashier,
            canBeBartender = canBeBartender,
        )
        refreshData(selectWorkerId = workerId)
    }

    fun deleteWorker(
        workerId: Int,
        onSuccess: () -> Unit = {},
    ) = mutateCatalog(onSuccess) {
        workersRepository.deleteWorker(workerId)
        refreshData(selectWorkerId = null)
    }

    fun createPosition(
        name: String,
        description: String,
        positionVariantIds: List<String>,
        onSuccess: () -> Unit = {},
    ) = mutateCatalog(onSuccess) {
        val position = positionsRepository.createPosition(
            name = name.trim(),
            description = description.trim(),
            positionVariantIds = positionVariantIds,
        )
        refreshData(selectPositionId = position.id)
    }

    fun updatePosition(
        positionId: String,
        name: String,
        description: String,
        positionVariantIds: List<String>,
        onSuccess: () -> Unit = {},
    ) = mutateCatalog(onSuccess) {
        positionsRepository.updatePosition(
            positionId = positionId,
            name = name.trim(),
            description = description.trim(),
            positionVariantIds = positionVariantIds,
        )
        refreshData(selectPositionId = positionId, selectVariantId = selectedPositionVariantId.value)
    }

    fun deletePosition(
        positionId: String,
        onSuccess: () -> Unit = {},
    ) = mutateCatalog(onSuccess) {
        positionsRepository.deletePosition(positionId)
        refreshData(
            selectPositionId = positions.value.firstOrNull { it.id != positionId }?.id,
            selectVariantId = null,
        )
    }

    fun createPositionVariant(
        name: String,
        price: Float,
        onSuccess: () -> Unit = {},
    ) = mutateCatalog(onSuccess) {
        val variant = positionVariantsRepository.createPositionVariant(
            name = name.trim(),
            price = price,
        )
        refreshData(
            selectPositionId = selectedPositionId.value,
            selectVariantId = variant.id,
        )
    }

    fun updatePositionVariant(
        positionVariantId: String,
        name: String,
        price: Float,
        isActive: Boolean,
        onSuccess: () -> Unit = {},
    ) = mutateCatalog(onSuccess) {
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

    fun deletePositionVariant(
        positionVariantId: String,
        onSuccess: () -> Unit = {},
    ) = mutateCatalog(onSuccess) {
        positionVariantsRepository.deletePositionVariant(positionVariantId)
        refreshData(
            selectPositionId = selectedPositionId.value,
            selectVariantId = positionVariants.value.firstOrNull { it.id != positionVariantId }?.id,
        )
    }

    private fun mutateCatalog(
        onSuccess: () -> Unit = {},
        block: suspend () -> Unit,
    ) {
        viewModelScope.launch(exceptionHandler) {
            isSaving.emit(true)
            try {
                block()
                onSuccess()
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
        val updatedPositionVariants = positionVariantsRepository
            .getPositionVariants(withNotActive = true)
            .sortedBy { it.name.lowercase() }
        val updatedAnalytics = if (adminRepository.isAuthorized()) {
            adminRepository.getAnalytics()
        } else {
            null
        }

        val resolvedPositionId = when {
            updatedPositions.isEmpty() -> null
            selectPositionId != null && updatedPositions.any { it.id == selectPositionId } -> selectPositionId
            else -> updatedPositions.first().id
        }

        val linkedVariants = resolvedPositionId?.let { loadPositionVariants(it) }.orEmpty()
        val resolvedVariantId = selectVariantId?.takeIf { candidateId ->
            updatedPositionVariants.any { it.id == candidateId }
        }

        workers.emit(updatedWorkers)
        positions.emit(updatedPositions)
        positionVariants.emit(updatedPositionVariants)
        selectedPositionVariantIds.emit(linkedVariants.mapTo(mutableSetOf()) { it.id })
        analytics.emit(updatedAnalytics)

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
