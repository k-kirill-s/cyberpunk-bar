package by.cyberpunkfandom.barfrontend.presentation.admin.variants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.presentation.admin.AdminCatalogViewModel
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminContentContainer
import by.cyberpunkfandom.barfrontend.presentation.admin.components.AdminLoadingState
import by.cyberpunkfandom.barfrontend.presentation.admin.components.VariantEditorContent
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import org.jetbrains.compose.resources.painterResource

@Composable
fun AdminVariantEditorScreen(
    viewModel: AdminCatalogViewModel,
    variantId: String?,
    onBackRequest: () -> Unit,
    onSaved: () -> Unit,
) {
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val isSaving = viewModel.isSaving.collectAsStateWithLifecycle().value
    val positionVariants = viewModel.positionVariants.collectAsStateWithLifecycle().value
    val variant = variantId?.let { currentVariantId ->
        positionVariants.firstOrNull { it.id == currentVariantId }
    }

    LaunchedEffect(variantId) {
        viewModel.selectPositionVariantById(variantId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = if (variantId == null) "Новый товар" else "Товар",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackRequest,
        )

        AdminContentContainer {
            if (variantId != null && variant == null && isLoading) {
                AdminLoadingState(title = "Загружаем товар")
            } else {
                VariantEditorContent(
                    variant = variant,
                    isSaving = isSaving,
                    onSave = { name, price, isActive, onSuccess ->
                        if (variantId == null) {
                            viewModel.createPositionVariant(name, price) {
                                onSuccess()
                                onSaved()
                            }
                        } else {
                            viewModel.updatePositionVariant(variantId, name, price, isActive) {
                                onSuccess()
                                onSaved()
                            }
                        }
                    },
                    onDelete = if (variantId == null) {
                        null
                    } else {
                        {
                            variant?.let {
                                viewModel.deletePositionVariant(it.id) {
                                    onSaved()
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}
