package by.cyberpunkfandom.barfrontend.presentation.cashier.addpositionextra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.PositionExtraItemsRepository
import by.cyberpunkfandom.barfrontend.data.repositories.PositionExtraRepository
import by.cyberpunkfandom.barfrontend.domain.PositionExtra
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CashierAddPositionExtraViewModel(
    private val positionItemId: Int,
    private val positionExtraRepository: PositionExtraRepository,
    private val positionExtraItemsRepository: PositionExtraItemsRepository,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e("error", throwable)
    }

    private val _onPositionExtraItemAdded: Channel<Int> = Channel(Channel.BUFFERED)
    val onPositionExtraItemAdded: Flow<Int> = _onPositionExtraItemAdded.receiveAsFlow()

    val positionExtra: MutableStateFlow<List<PositionExtra>> = MutableStateFlow(emptyList())

    val isAddButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch(exceptionHandler) {
            positionExtra.update { positionExtraRepository.getPositionExtra() }
        }
    }

    fun onAddButtonClick(positionExtraId: String) {
        viewModelScope.launch(exceptionHandler) {
            isAddButtonLoading.emit(true)
            try {
                val positionExtraItem = positionExtraItemsRepository.addPositionExtraToPositionItem(
                    positionItemId = positionItemId,
                    positionExtraId = positionExtraId,
                )
                _onPositionExtraItemAdded.send(positionExtraItem.id)
            } finally {
                isAddButtonLoading.emit(false)
            }
        }
    }
}
