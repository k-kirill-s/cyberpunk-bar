package by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import barfrontend.composeapp.generated.resources.Res
import barfrontend.composeapp.generated.resources.back_24dp
import by.cyberpunkfandom.barfrontend.core.format
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionVariant
import by.cyberpunkfandom.barfrontend.domain.Worker
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppBoxButton
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppHorizontalDivider
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppStateMessage
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun CashierTogglePositionsScreen(
    onError: (code: ExceptionCodes) -> Unit,
    onBackRequest: () -> Unit,
    viewModel: CashierTogglePositionsViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onError.collect { code ->
            onError(code)
        }
    }

    CashierTogglePositionsScreen(
        onBackClick = onBackRequest,
        isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value,
        isSaving = viewModel.isSaving.collectAsStateWithLifecycle().value,
        workers = viewModel.workers.collectAsStateWithLifecycle().value,
        selectedWorkerId = viewModel.selectedWorkerId.collectAsStateWithLifecycle().value,
        onWorkerClick = viewModel::onWorkerClick,
        onCreateWorker = viewModel::createWorker,
        onUpdateWorker = viewModel::updateWorker,
        onDeleteWorker = viewModel::deleteWorker,
        positions = viewModel.positions.collectAsStateWithLifecycle().value,
        selectedPositionId = viewModel.selectedPositionId.collectAsStateWithLifecycle().value,
        onPositionClick = viewModel::onPositionClick,
        onCreatePosition = viewModel::createPosition,
        onUpdatePosition = viewModel::updatePosition,
        onDeletePosition = viewModel::deletePosition,
        positionVariants = viewModel.positionVariants.collectAsStateWithLifecycle().value,
        selectedPositionVariantId = viewModel.selectedPositionVariantId.collectAsStateWithLifecycle().value,
        onPositionVariantClick = viewModel::onPositionVariantClick,
        onCreatePositionVariant = viewModel::createPositionVariant,
        onUpdatePositionVariant = viewModel::updatePositionVariant,
        onDeletePositionVariant = viewModel::deletePositionVariant,
    )
}

@Composable
private fun CashierTogglePositionsScreen(
    onBackClick: () -> Unit,
    isLoading: Boolean,
    isSaving: Boolean,
    workers: List<Worker>,
    selectedWorkerId: Int?,
    onWorkerClick: (Worker) -> Unit,
    onCreateWorker: (String) -> Unit,
    onUpdateWorker: (Int, String, Boolean) -> Unit,
    onDeleteWorker: (Int) -> Unit,
    positions: List<Position>,
    selectedPositionId: String?,
    onPositionClick: (Position) -> Unit,
    onCreatePosition: (String, String, String) -> Unit,
    onUpdatePosition: (String, String, String) -> Unit,
    onDeletePosition: (String) -> Unit,
    positionVariants: List<PositionVariant>,
    selectedPositionVariantId: String?,
    onPositionVariantClick: (PositionVariant) -> Unit,
    onCreatePositionVariant: (String, String, String, Float) -> Unit,
    onUpdatePositionVariant: (String, String, Float, Boolean) -> Unit,
    onDeletePositionVariant: (String) -> Unit,
) {
    val selectedWorker = workers.firstOrNull { it.id == selectedWorkerId }
    val selectedPosition = positions.firstOrNull { it.id == selectedPositionId }
    val selectedPositionVariant = positionVariants.firstOrNull { it.id == selectedPositionVariantId }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = "Каталог и команда",
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = onBackClick,
        )

        if (isLoading) {
            AppStateMessage(
                title = "Загружаем каталог",
                isLoading = true,
                modifier = Modifier.weight(1f),
            )
            return
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(AppTheme.dimensions.basePadding),
        ) {
            val isCompact = maxWidth < 1100.dp

            if (isCompact) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
                ) {
                    WorkersPanel(
                        workers = workers,
                        selectedWorker = selectedWorker,
                        isSaving = isSaving,
                        onWorkerClick = onWorkerClick,
                        onCreateWorker = onCreateWorker,
                        onUpdateWorker = onUpdateWorker,
                        onDeleteWorker = onDeleteWorker,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    PositionsPanel(
                        positions = positions,
                        selectedPosition = selectedPosition,
                        isSaving = isSaving,
                        onPositionClick = onPositionClick,
                        onCreatePosition = onCreatePosition,
                        onUpdatePosition = onUpdatePosition,
                        onDeletePosition = onDeletePosition,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    VariantsPanel(
                        selectedPosition = selectedPosition,
                        positionVariants = positionVariants,
                        selectedPositionVariant = selectedPositionVariant,
                        isSaving = isSaving,
                        onPositionVariantClick = onPositionVariantClick,
                        onCreatePositionVariant = onCreatePositionVariant,
                        onUpdatePositionVariant = onUpdatePositionVariant,
                        onDeletePositionVariant = onDeletePositionVariant,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
                ) {
                    WorkersPanel(
                        workers = workers,
                        selectedWorker = selectedWorker,
                        isSaving = isSaving,
                        onWorkerClick = onWorkerClick,
                        onCreateWorker = onCreateWorker,
                        onUpdateWorker = onUpdateWorker,
                        onDeleteWorker = onDeleteWorker,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )

                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
                    ) {
                        PositionsPanel(
                            positions = positions,
                            selectedPosition = selectedPosition,
                            isSaving = isSaving,
                            onPositionClick = onPositionClick,
                            onCreatePosition = onCreatePosition,
                            onUpdatePosition = onUpdatePosition,
                            onDeletePosition = onDeletePosition,
                            modifier = Modifier.weight(1f),
                        )

                        VariantsPanel(
                            selectedPosition = selectedPosition,
                            positionVariants = positionVariants,
                            selectedPositionVariant = selectedPositionVariant,
                            isSaving = isSaving,
                            onPositionVariantClick = onPositionVariantClick,
                            onCreatePositionVariant = onCreatePositionVariant,
                            onUpdatePositionVariant = onUpdatePositionVariant,
                            onDeletePositionVariant = onDeletePositionVariant,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkersPanel(
    workers: List<Worker>,
    selectedWorker: Worker?,
    isSaving: Boolean,
    onWorkerClick: (Worker) -> Unit,
    onCreateWorker: (String) -> Unit,
    onUpdateWorker: (Int, String, Boolean) -> Unit,
    onDeleteWorker: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var newWorkerName by rememberSaveable { mutableStateOf("") }
    var workerName by rememberSaveable { mutableStateOf("") }
    var workerOnline by rememberSaveable { mutableStateOf(false) }
    var isCreateExpanded by rememberSaveable { mutableStateOf(workers.isEmpty()) }

    LaunchedEffect(selectedWorker?.id, selectedWorker?.name, selectedWorker?.isOnLine) {
        workerName = selectedWorker?.name.orEmpty()
        workerOnline = selectedWorker?.isOnLine ?: false
    }

    SectionCard(
        title = "Сотрудники",
        modifier = modifier,
        action = {
            SectionActionButton(
                title = if (isCreateExpanded) "Скрыть форму" else "+ Добавить",
                color = if (isCreateExpanded) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.green,
                onClick = { isCreateExpanded = !isCreateExpanded },
            )
        },
    ) {
        if (isCreateExpanded) {
            Text(text = "Новый сотрудник", style = AppTheme.typography.title)
            AppFormTextField(
                value = newWorkerName,
                onValueChange = { newWorkerName = it },
                label = "Имя",
                singleLine = true,
            )
            ActionRow(
                primaryTitle = "Добавить",
                onPrimaryClick = {
                    onCreateWorker(newWorkerName)
                    newWorkerName = ""
                },
                primaryEnabled = newWorkerName.isNotBlank() && !isSaving,
                primaryColor = AppTheme.colorScheme.green,
                secondaryTitle = null,
                onSecondaryClick = {},
                secondaryEnabled = false,
                isSaving = isSaving,
            )

            AppHorizontalDivider()
        }

        if (workers.isEmpty()) {
            AppStateMessage(
                title = "Сотрудников пока нет",
                description = if (isCreateExpanded) {
                    "Заполните форму выше, чтобы завести первого сборщика."
                } else {
                    "Нажмите «+ Добавить», чтобы завести первого сборщика."
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 180.dp),
            )
        } else {
            SelectableList(
                modifier = Modifier.heightIn(min = 180.dp, max = 320.dp),
                items = workers,
                selectedItem = selectedWorker,
                itemKey = Worker::id,
                onItemClick = onWorkerClick,
            ) { worker ->
                Column {
                    Text(text = worker.name, style = AppTheme.typography.title)
                    Text(
                        text = if (worker.isOnLine) "В сети" else "Офлайн",
                        color = if (worker.isOnLine) AppTheme.colorScheme.green else AppTheme.colorScheme.text,
                        style = AppTheme.typography.body,
                    )
                }
            }
        }

        AppHorizontalDivider()

        if (selectedWorker == null) {
            AppStateMessage(
                title = "Выберите сотрудника",
                description = "Можно менять имя, удалять карточку и вручную переключать статус онлайн.",
                modifier = Modifier.heightIn(min = 180.dp),
            )
        } else {
            Text(text = "Карточка сотрудника", style = AppTheme.typography.title)
            AppFormTextField(
                value = workerName,
                onValueChange = { workerName = it },
                label = "Имя",
                singleLine = true,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Онлайн",
                    modifier = Modifier.weight(1f),
                    style = AppTheme.typography.body,
                )
                Switch(
                    checked = workerOnline,
                    onCheckedChange = { workerOnline = it },
                )
            }

            ActionRow(
                primaryTitle = "Сохранить",
                onPrimaryClick = { onUpdateWorker(selectedWorker.id, workerName, workerOnline) },
                primaryEnabled = workerName.isNotBlank() && !isSaving,
                primaryColor = AppTheme.colorScheme.accent,
                secondaryTitle = "Удалить",
                onSecondaryClick = { onDeleteWorker(selectedWorker.id) },
                secondaryEnabled = !isSaving,
                isSaving = isSaving,
            )
        }
    }
}

@Composable
private fun PositionsPanel(
    positions: List<Position>,
    selectedPosition: Position?,
    isSaving: Boolean,
    onPositionClick: (Position) -> Unit,
    onCreatePosition: (String, String, String) -> Unit,
    onUpdatePosition: (String, String, String) -> Unit,
    onDeletePosition: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var newPositionId by rememberSaveable { mutableStateOf("") }
    var newPositionName by rememberSaveable { mutableStateOf("") }
    var newPositionDescription by rememberSaveable { mutableStateOf("") }
    var positionName by rememberSaveable { mutableStateOf("") }
    var positionDescription by rememberSaveable { mutableStateOf("") }
    var isCreateExpanded by rememberSaveable { mutableStateOf(positions.isEmpty()) }

    LaunchedEffect(selectedPosition?.id, selectedPosition?.name, selectedPosition?.description) {
        positionName = selectedPosition?.name.orEmpty()
        positionDescription = selectedPosition?.description.orEmpty()
    }

    SectionCard(
        title = "Позиции",
        modifier = modifier,
        action = {
            SectionActionButton(
                title = if (isCreateExpanded) "Скрыть форму" else "+ Добавить",
                color = if (isCreateExpanded) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.green,
                onClick = { isCreateExpanded = !isCreateExpanded },
            )
        },
    ) {
        if (isCreateExpanded) {
            Text(text = "Новая позиция", style = AppTheme.typography.title)
            AppFormTextField(
                value = newPositionId,
                onValueChange = { newPositionId = it },
                label = "ID",
                singleLine = true,
            )
            AppFormTextField(
                value = newPositionName,
                onValueChange = { newPositionName = it },
                label = "Название",
                singleLine = true,
            )
            AppFormTextField(
                value = newPositionDescription,
                onValueChange = { newPositionDescription = it },
                label = "Описание",
                minLines = 3,
            )
            ActionRow(
                primaryTitle = "Добавить",
                onPrimaryClick = {
                    onCreatePosition(newPositionId, newPositionName, newPositionDescription)
                    newPositionId = ""
                    newPositionName = ""
                    newPositionDescription = ""
                },
                primaryEnabled = newPositionId.isNotBlank() && newPositionName.isNotBlank() && !isSaving,
                primaryColor = AppTheme.colorScheme.green,
                secondaryTitle = null,
                onSecondaryClick = {},
                secondaryEnabled = false,
                isSaving = isSaving,
            )

            AppHorizontalDivider()
        }

        if (positions.isEmpty()) {
            AppStateMessage(
                title = "Позиции пока не заведены",
                description = if (isCreateExpanded) {
                    "Заполните форму выше, чтобы создать первую позицию меню."
                } else {
                    "Нажмите «+ Добавить», чтобы создать первую позицию меню."
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp),
            )
        } else {
            SelectableList(
                modifier = Modifier.heightIn(min = 160.dp, max = 280.dp),
                items = positions,
                selectedItem = selectedPosition,
                itemKey = Position::id,
                onItemClick = onPositionClick,
            ) { position ->
                Column {
                    Text(text = position.name, style = AppTheme.typography.title)
                    Text(text = position.id, style = AppTheme.typography.body)
                }
            }
        }

        AppHorizontalDivider()

        if (selectedPosition == null) {
            AppStateMessage(
                title = "Выберите позицию",
                description = "После выбора можно обновить название и описание или удалить позицию.",
                modifier = Modifier.heightIn(min = 160.dp),
            )
        } else {
            Text(text = "Редактирование позиции", style = AppTheme.typography.title)
            AppFormTextField(
                value = selectedPosition.id,
                onValueChange = {},
                label = "ID",
                enabled = false,
                singleLine = true,
            )
            AppFormTextField(
                value = positionName,
                onValueChange = { positionName = it },
                label = "Название",
                singleLine = true,
            )
            AppFormTextField(
                value = positionDescription,
                onValueChange = { positionDescription = it },
                label = "Описание",
                minLines = 3,
            )
            ActionRow(
                primaryTitle = "Сохранить",
                onPrimaryClick = { onUpdatePosition(selectedPosition.id, positionName, positionDescription) },
                primaryEnabled = positionName.isNotBlank() && !isSaving,
                primaryColor = AppTheme.colorScheme.accent,
                secondaryTitle = "Удалить",
                onSecondaryClick = { onDeletePosition(selectedPosition.id) },
                secondaryEnabled = !isSaving,
                isSaving = isSaving,
            )
        }
    }
}

@Composable
private fun VariantsPanel(
    selectedPosition: Position?,
    positionVariants: List<PositionVariant>,
    selectedPositionVariant: PositionVariant?,
    isSaving: Boolean,
    onPositionVariantClick: (PositionVariant) -> Unit,
    onCreatePositionVariant: (String, String, String, Float) -> Unit,
    onUpdatePositionVariant: (String, String, Float, Boolean) -> Unit,
    onDeletePositionVariant: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var newVariantId by rememberSaveable { mutableStateOf("") }
    var newVariantName by rememberSaveable { mutableStateOf("") }
    var newVariantPrice by rememberSaveable { mutableStateOf("") }
    var variantName by rememberSaveable { mutableStateOf("") }
    var variantPrice by rememberSaveable { mutableStateOf("") }
    var variantIsActive by rememberSaveable { mutableStateOf(true) }
    var isCreateExpanded by rememberSaveable(selectedPosition?.id) { mutableStateOf(positionVariants.isEmpty()) }

    LaunchedEffect(
        selectedPositionVariant?.id,
        selectedPositionVariant?.name,
        selectedPositionVariant?.price,
        selectedPositionVariant?.isActive,
    ) {
        variantName = selectedPositionVariant?.name.orEmpty()
        variantPrice = selectedPositionVariant?.price?.format(2).orEmpty()
        variantIsActive = selectedPositionVariant?.isActive ?: true
    }

    val newPriceValue = remember(newVariantPrice) { newVariantPrice.replace(",", ".").toFloatOrNull() }
    val existingPriceValue = remember(variantPrice) { variantPrice.replace(",", ".").toFloatOrNull() }

    SectionCard(
        title = selectedPosition?.name?.let { "Варианты: $it" } ?: "Варианты",
        modifier = modifier,
        action = {
            SectionActionButton(
                title = if (isCreateExpanded) "Скрыть форму" else "+ Добавить",
                color = if (isCreateExpanded) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.green,
                enabled = selectedPosition != null,
                onClick = { isCreateExpanded = !isCreateExpanded },
            )
        },
    ) {
        if (selectedPosition == null) {
            AppStateMessage(
                title = "Сначала выберите позицию",
                description = "У вариантов всегда есть родительская позиция. После выбора станет доступна кнопка «+ Добавить».",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp),
            )
            return@SectionCard
        }

        if (isCreateExpanded) {
            Text(text = "Новый вариант", style = AppTheme.typography.title)
            AppFormTextField(
                value = newVariantId,
                onValueChange = { newVariantId = it },
                label = "ID",
                singleLine = true,
            )
            AppFormTextField(
                value = newVariantName,
                onValueChange = { newVariantName = it },
                label = "Название",
                singleLine = true,
            )
            AppFormTextField(
                value = newVariantPrice,
                onValueChange = { newVariantPrice = it },
                label = "Цена",
                singleLine = true,
            )
            ActionRow(
                primaryTitle = "Добавить",
                onPrimaryClick = {
                    newPriceValue?.let { price ->
                        onCreatePositionVariant(selectedPosition.id, newVariantId, newVariantName, price)
                        newVariantId = ""
                        newVariantName = ""
                        newVariantPrice = ""
                    }
                },
                primaryEnabled = newVariantId.isNotBlank() && newVariantName.isNotBlank() && newPriceValue != null && !isSaving,
                primaryColor = AppTheme.colorScheme.green,
                secondaryTitle = null,
                onSecondaryClick = {},
                secondaryEnabled = false,
                isSaving = isSaving,
            )

            AppHorizontalDivider()
        }

        if (positionVariants.isEmpty()) {
            AppStateMessage(
                title = "Вариантов пока нет",
                description = if (isCreateExpanded) {
                    "Заполните форму выше, чтобы добавить размеры, вкусы или ценовые опции."
                } else {
                    "Нажмите «+ Добавить», чтобы добавить размеры, вкусы или ценовые опции."
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp),
            )
        } else {
            SelectableList(
                modifier = Modifier.heightIn(min = 160.dp, max = 260.dp),
                items = positionVariants,
                selectedItem = selectedPositionVariant,
                itemKey = PositionVariant::id,
                onItemClick = onPositionVariantClick,
            ) { variant ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = variant.name, style = AppTheme.typography.title)
                        Text(
                            text = "${variant.price.format(2)} • ${if (variant.isActive) "Активен" else "Выключен"}",
                            color = if (variant.isActive) AppTheme.colorScheme.green else AppTheme.colorScheme.red,
                            style = AppTheme.typography.body,
                        )
                    }
                }
            }
        }

        AppHorizontalDivider()

        if (selectedPositionVariant == null) {
            AppStateMessage(
                title = "Выберите вариант",
                description = "Можно менять название, цену и активность без перехода в другой экран.",
                modifier = Modifier.heightIn(min = 160.dp),
            )
        } else {
            Text(text = "Редактирование варианта", style = AppTheme.typography.title)
            AppFormTextField(
                value = selectedPositionVariant.id,
                onValueChange = {},
                label = "ID",
                enabled = false,
                singleLine = true,
            )
            AppFormTextField(
                value = variantName,
                onValueChange = { variantName = it },
                label = "Название",
                singleLine = true,
            )
            AppFormTextField(
                value = variantPrice,
                onValueChange = { variantPrice = it },
                label = "Цена",
                singleLine = true,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Активен",
                    modifier = Modifier.weight(1f),
                    style = AppTheme.typography.body,
                )
                Switch(
                    checked = variantIsActive,
                    onCheckedChange = { variantIsActive = it },
                )
            }
            ActionRow(
                primaryTitle = "Сохранить",
                onPrimaryClick = {
                    existingPriceValue?.let { price ->
                        onUpdatePositionVariant(selectedPositionVariant.id, variantName, price, variantIsActive)
                    }
                },
                primaryEnabled = variantName.isNotBlank() && existingPriceValue != null && !isSaving,
                primaryColor = AppTheme.colorScheme.accent,
                secondaryTitle = "Удалить",
                onSecondaryClick = { onDeletePositionVariant(selectedPositionVariant.id) },
                secondaryEnabled = !isSaving,
                isSaving = isSaving,
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .animateContentSize()
            .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
            .background(AppTheme.colorScheme.surface)
            .padding(AppTheme.dimensions.basePadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.big,
            )

            action?.invoke()
        }
        content()
    }
}

@Composable
private fun SectionActionButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = AppTheme.colorScheme.green,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
            .background(if (enabled) color else AppTheme.colorScheme.surfaceSelected)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(
                horizontal = AppTheme.dimensions.basePadding,
                vertical = AppTheme.dimensions.basePadding / 2,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = AppTheme.typography.body.copy(
                color = if (enabled) AppTheme.colorScheme.text else AppTheme.colorScheme.divider,
            ),
        )
    }
}

@Composable
private fun AppFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    minLines: Int = 1,
) {
    val borderColor = if (enabled) AppTheme.colorScheme.divider else AppTheme.colorScheme.surfaceSelected
    val textStyle = if (enabled) {
        AppTheme.typography.body
    } else {
        AppTheme.typography.body.copy(color = AppTheme.colorScheme.divider)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
    ) {
        Text(
            text = label,
            style = AppTheme.typography.body,
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
                .background(AppTheme.colorScheme.background)
                .border(
                    width = AppTheme.dimensions.thinDivider,
                    color = borderColor,
                    shape = RoundedCornerShape(AppTheme.dimensions.cornerRadius),
                )
                .padding(AppTheme.dimensions.basePadding),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = if (singleLine) 24.dp else 96.dp),
                enabled = enabled,
                singleLine = singleLine,
                minLines = minLines,
                textStyle = textStyle.asPlainTextStyle(),
                cursorBrush = SolidColor(AppTheme.colorScheme.accent),
            )
        }
    }
}

private fun TextStyle.asPlainTextStyle(): TextStyle = copy()

@Composable
private fun <T, K> SelectableList(
    items: List<T>,
    selectedItem: T?,
    itemKey: (T) -> K,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider),
    ) {
        items.forEach { item ->
            val isSelected = itemKey(item) == selectedItem?.let(itemKey)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
                    .background(if (isSelected) AppTheme.colorScheme.surfaceSelected else AppTheme.colorScheme.background)
                    .clickable { onItemClick(item) }
                    .padding(AppTheme.dimensions.basePadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                itemContent(item)
            }
        }
    }
}

@Composable
private fun ActionRow(
    primaryTitle: String,
    onPrimaryClick: () -> Unit,
    primaryEnabled: Boolean,
    primaryColor: androidx.compose.ui.graphics.Color,
    secondaryTitle: String?,
    onSecondaryClick: () -> Unit,
    secondaryEnabled: Boolean,
    isSaving: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
    ) {
        AppBoxButton(
            title = primaryTitle,
            onClick = onPrimaryClick,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            color = primaryColor,
            enabled = primaryEnabled,
            isLoading = isSaving,
        )

        secondaryTitle?.let {
            AppBoxButton(
                title = secondaryTitle,
                onClick = onSecondaryClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                color = AppTheme.colorScheme.red,
                enabled = secondaryEnabled,
            )
        }
    }
}
