package by.cyberpunkfandom.barfrontend.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.cyberpunkfandom.barfrontend.data.repositories.AdminRepository
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.domain.exceptions.GeneralException
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val adminRepository: AdminRepository,
) : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Napier.e("admin auth error", throwable)
        if (throwable is GeneralException) {
            _onError.trySend(throwable.code)
        } else {
            _onError.trySend(ExceptionCodes.UNKNOWN)
        }
    }

    private val _onError = Channel<ExceptionCodes>(Channel.BUFFERED)
    val onError: Flow<ExceptionCodes> = _onError.receiveAsFlow()

    val isAuthorizing = MutableStateFlow(false)
    val isAuthorized = MutableStateFlow(adminRepository.isAuthorized())

    fun login(
        username: String,
        password: String,
    ) {
        viewModelScope.launch(exceptionHandler) {
            isAuthorizing.emit(true)
            try {
                adminRepository.login(
                    username = username.trim(),
                    password = password,
                )
                isAuthorized.emit(true)
            } finally {
                isAuthorizing.emit(false)
            }
        }
    }

    fun logout() {
        adminRepository.logout()
        isAuthorized.value = false
    }
}
