package by.cyberpunkfandom.barfrontend.presentation.admin.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBigButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdminLoginScreen(
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
                    style = AppTheme.typography.title,
                )

                Text(
                    text = "Используйте логин и пароль из настроек стека (`ADMIN_USERNAME`, `ADMIN_PASSWORD`).",
                    style = AppTheme.typography.body.copy(color = AppTheme.colorScheme.textSecondary),
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
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val borderColor = when {
        !enabled -> AppTheme.colorScheme.divider
        isFocused -> AppTheme.colorScheme.accent
        else -> AppTheme.colorScheme.dividerStrong
    }
    val textStyle = if (enabled) {
        MaterialTheme.typography.bodyLarge.copy(color = AppTheme.colorScheme.text)
    } else {
        MaterialTheme.typography.bodyLarge.copy(color = AppTheme.colorScheme.textSecondary)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(color = AppTheme.colorScheme.textSecondary),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colorScheme.surfaceMuted, RoundedCornerShape(AppTheme.dimensions.cornerRadius))
                .border(
                    width = AppTheme.dimensions.thinDivider * 2,
                    color = borderColor,
                    shape = RoundedCornerShape(AppTheme.dimensions.cornerRadius),
                )
                .padding(AppTheme.dimensions.basePadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused }
                    .heightIn(min = 24.dp),
                enabled = enabled,
                singleLine = true,
                textStyle = textStyle,
                visualTransformation = visualTransformation,
                cursorBrush = SolidColor(AppTheme.colorScheme.accent),
            )

            if (trailingActionLabel != null && onTrailingActionClick != null) {
                Text(
                    text = trailingActionLabel,
                    modifier = Modifier.clickable(enabled = enabled, onClick = onTrailingActionClick),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = if (enabled) AppTheme.colorScheme.accent else AppTheme.colorScheme.textSecondary,
                    ),
                )
            }
        }
    }
}
