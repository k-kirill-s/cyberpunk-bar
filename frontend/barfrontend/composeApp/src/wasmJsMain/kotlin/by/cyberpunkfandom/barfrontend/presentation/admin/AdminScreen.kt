package by.cyberpunkfandom.barfrontend.presentation.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions.CashierTogglePositionsScreen
import by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions.CashierTogglePositionsViewModel
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBigButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminScreen(
    onBackRequest: () -> Unit,
    viewModel: AdminViewModel = koinViewModel(),
    managementViewModel: CashierTogglePositionsViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun showErrorSnackbar(code: ExceptionCodes) {
        scope.launch {
            snackbarHostState.showSnackbar(code.message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onError.collect { showErrorSnackbar(it) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        val isAuthorized = viewModel.isAuthorized.collectAsStateWithLifecycle().value

        if (isAuthorized) {
            CashierTogglePositionsScreen(
                onError = ::showErrorSnackbar,
                onBackRequest = {
                    viewModel.logout()
                    onBackRequest()
                },
                viewModel = managementViewModel,
            )
        } else {
            AdminLoginScreen(
                isAuthorizing = viewModel.isAuthorizing.collectAsStateWithLifecycle().value,
                onBackRequest = onBackRequest,
                onLoginRequest = viewModel::login,
            )
        }
    }
}

@Composable
private fun AdminLoginScreen(
    isAuthorizing: Boolean,
    onBackRequest: () -> Unit,
    onLoginRequest: (String, String) -> Unit,
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Администратор",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackRequest,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppTheme.dimensions.basePadding),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
            ) {
                Text(
                    text = "Вход в управление каталогом и командой.",
                    style = AppTheme.typography.body,
                )

                Text(
                    text = "Используйте логин и пароль из настроек стека (`ADMIN_USERNAME`, `ADMIN_PASSWORD`).",
                    style = AppTheme.typography.body.copy(color = AppTheme.colorScheme.divider),
                )

                AdminFormField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Логин",
                    enabled = !isAuthorizing,
                )

                AdminFormField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Пароль",
                    enabled = !isAuthorizing,
                    visualTransformation = if (isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingActionLabel = if (isPasswordVisible) "Скрыть" else "Показать",
                    onTrailingActionClick = { isPasswordVisible = !isPasswordVisible },
                )

                AppBigButton(
                    title = "Открыть админку",
                    onClick = { onLoginRequest(username, password) },
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.accent,
                    enabled = username.isNotBlank() && password.isNotBlank(),
                    isLoading = isAuthorizing,
                )
            }
        }
    }
}

@Composable
private fun AdminFormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingActionLabel: String? = null,
    onTrailingActionClick: (() -> Unit)? = null,
) {
    val textStyle = AppTheme.typography.body

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
    ) {
        Text(
            text = label,
            style = AppTheme.typography.body,
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            textStyle = textStyle,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(AppTheme.dimensions.cornerRadius),
            trailingIcon = {
                if (trailingActionLabel != null && onTrailingActionClick != null) {
                Text(
                    text = trailingActionLabel,
                    modifier = Modifier
                        .clickable(
                            enabled = enabled,
                            onClick = onTrailingActionClick,
                        ),
                    style = AppTheme.typography.body.copy(
                        color = if (enabled) AppTheme.colorScheme.accent else AppTheme.colorScheme.divider,
                    ),
                )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = AppTheme.colorScheme.text,
                unfocusedTextColor = AppTheme.colorScheme.text,
                disabledTextColor = AppTheme.colorScheme.divider,
                focusedBorderColor = AppTheme.colorScheme.accent,
                unfocusedBorderColor = AppTheme.colorScheme.divider,
                disabledBorderColor = AppTheme.colorScheme.surfaceSelected,
                focusedContainerColor = AppTheme.colorScheme.background,
                unfocusedContainerColor = AppTheme.colorScheme.background,
                disabledContainerColor = AppTheme.colorScheme.background,
                cursorColor = AppTheme.colorScheme.accent,
            ),
        )
    }
}
