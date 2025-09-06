package by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.PositionVariantsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionsRepository
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionVariant
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

    private val positions: MutableStateFlow<Map<Position, List<PositionVariant>>> = MutableStateFlow(emptyMap())

    private val selectedPositionId: MutableStateFlow<String?> = MutableStateFlow(null)

    private val selectedPositionVariantId: MutableStateFlow<String?> = MutableStateFlow(null)

    private val isToggleButtonEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val isToggleButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val contentState: CashierTogglePositionsState.ContentState = CashierTogglePositionsState.ContentState(
        positions = positions,
        selectedPositionId = selectedPositionId,
        selectedPositionVariantId = selectedPositionVariantId,
        isToggleButtonEnabled = isToggleButtonEnabled,
        isToggleButtonLoading = isToggleButtonLoading,
    )

    val state = CashierTogglePositionsState(
        contentState = contentState,
    )

    init {
        updateData()
    }

    fun onPositionClick(position: Position) {
        selectedPositionId.value = position.id
        selectedPositionVariantId.value = null
        isToggleButtonEnabled.value = false
    }

    fun onPositionVariantClick(positionVariant: PositionVariant) {
        selectedPositionVariantId.value = positionVariant.id
        isToggleButtonEnabled.value = true
    }

    fun onToggleButtonClick(isActive: Boolean) {
        isToggleButtonLoading.value = true
        viewModelScope.launch(exceptionHandler) {
            try {
                val positionVariantId = selectedPositionVariantId.value ?: return@launch
                positionVariantsRepository.setPositionVariantIsActive(
                    positionVariantId = positionVariantId,
                    isActive = isActive,
                )
                updateData()
            } finally {
                isToggleButtonLoading.value = false
            }
        }
    }

    private fun updateData() {
        viewModelScope.launch(exceptionHandler) {
            positions.emit(
                positionsRepository
                    .getPositions()
                    .associateWith { position ->
                        positionVariantsRepository.getPositionVariants(position.id, withNotActive = true)
                    }
            )
        }
    }
}
