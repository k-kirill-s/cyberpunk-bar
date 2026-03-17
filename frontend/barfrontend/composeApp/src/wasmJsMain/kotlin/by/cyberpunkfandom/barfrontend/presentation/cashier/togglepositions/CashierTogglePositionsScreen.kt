package by.cyberpunkfandom.barfrontend.presentation.cashier.togglepositions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppStateMessage
import by.cyberpunkfandom.barfrontend.presentation.core.components.AppTopBar
import by.cyberpunkfandom.barfrontend.presentation.core.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

private enum class CatalogPage {
    OVERVIEW,
    CREATE_WORKER,
    EDIT_WORKER,
    CREATE_POSITION,
    EDIT_POSITION,
    CREATE_VARIANT,
    EDIT_VARIANT,
}

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
        onCreateWorker = { name, onSuccess -> viewModel.createWorker(name, onSuccess) },
        onUpdateWorker = { workerId, name, isOnLine, onSuccess ->
            viewModel.updateWorker(workerId, name, isOnLine, onSuccess)
        },
        onDeleteWorker = { workerId, onSuccess -> viewModel.deleteWorker(workerId, onSuccess) },
        positions = viewModel.positions.collectAsStateWithLifecycle().value,
        selectedPositionId = viewModel.selectedPositionId.collectAsStateWithLifecycle().value,
        onPositionClick = viewModel::onPositionClick,
        onCreatePosition = { id, name, description, onSuccess ->
            viewModel.createPosition(id, name, description, onSuccess)
        },
        onUpdatePosition = { positionId, name, description, onSuccess ->
            viewModel.updatePosition(positionId, name, description, onSuccess)
        },
        onDeletePosition = { positionId, onSuccess -> viewModel.deletePosition(positionId, onSuccess) },
        positionVariants = viewModel.positionVariants.collectAsStateWithLifecycle().value,
        selectedPositionVariantId = viewModel.selectedPositionVariantId.collectAsStateWithLifecycle().value,
        onPositionVariantClick = viewModel::onPositionVariantClick,
        onCreatePositionVariant = { positionId, id, name, price, onSuccess ->
            viewModel.createPositionVariant(positionId, id, name, price, onSuccess)
        },
        onUpdatePositionVariant = { positionVariantId, name, price, isActive, onSuccess ->
            viewModel.updatePositionVariant(positionVariantId, name, price, isActive, onSuccess)
        },
        onDeletePositionVariant = { positionVariantId, onSuccess ->
            viewModel.deletePositionVariant(positionVariantId, onSuccess)
        },
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
    onCreateWorker: (String, () -> Unit) -> Unit,
    onUpdateWorker: (Int, String, Boolean, () -> Unit) -> Unit,
    onDeleteWorker: (Int, () -> Unit) -> Unit,
    positions: List<Position>,
    selectedPositionId: String?,
    onPositionClick: (Position) -> Unit,
    onCreatePosition: (String, String, String, () -> Unit) -> Unit,
    onUpdatePosition: (String, String, String, () -> Unit) -> Unit,
    onDeletePosition: (String, () -> Unit) -> Unit,
    positionVariants: List<PositionVariant>,
    selectedPositionVariantId: String?,
    onPositionVariantClick: (PositionVariant) -> Unit,
    onCreatePositionVariant: (String, String, String, Float, () -> Unit) -> Unit,
    onUpdatePositionVariant: (String, String, Float, Boolean, () -> Unit) -> Unit,
    onDeletePositionVariant: (String, () -> Unit) -> Unit,
) {
    var currentPage by rememberSaveable { mutableStateOf(CatalogPage.OVERVIEW) }

    val selectedWorker = workers.firstOrNull { it.id == selectedWorkerId }
    val selectedPosition = positions.firstOrNull { it.id == selectedPositionId }
    val selectedPositionVariant = positionVariants.firstOrNull { it.id == selectedPositionVariantId }

    val topBarTitle = when (currentPage) {
        CatalogPage.OVERVIEW -> "Каталог и команда"
        CatalogPage.CREATE_WORKER -> "Новый сотрудник"
        CatalogPage.EDIT_WORKER -> "Сотрудник"
        CatalogPage.CREATE_POSITION -> "Новая позиция"
        CatalogPage.EDIT_POSITION -> "Позиция"
        CatalogPage.CREATE_VARIANT -> "Новый вариант"
        CatalogPage.EDIT_VARIANT -> "Вариант"
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = topBarTitle,
            leftIcon = painterResource(Res.drawable.back_24dp),
            onLeftIconClick = {
                if (currentPage == CatalogPage.OVERVIEW) {
                    onBackClick()
                } else {
                    currentPage = CatalogPage.OVERVIEW
                }
            },
        )

        if (isLoading && currentPage == CatalogPage.OVERVIEW) {
            AppStateMessage(
                title = "Загружаем каталог",
                isLoading = true,
                modifier = Modifier.weight(1f),
            )
            return
        }

        when (currentPage) {
            CatalogPage.OVERVIEW -> CatalogOverviewScreen(
                workers = workers,
                selectedWorker = selectedWorker,
                onWorkerClick = onWorkerClick,
                onOpenCreateWorker = { currentPage = CatalogPage.CREATE_WORKER },
                onOpenEditWorker = { currentPage = CatalogPage.EDIT_WORKER },
                positions = positions,
                selectedPosition = selectedPosition,
                positionVariants = positionVariants,
                selectedPositionVariant = selectedPositionVariant,
                onPositionClick = onPositionClick,
                onPositionVariantClick = onPositionVariantClick,
                onOpenCreatePosition = { currentPage = CatalogPage.CREATE_POSITION },
                onOpenEditPosition = { currentPage = CatalogPage.EDIT_POSITION },
                onOpenCreateVariant = { currentPage = CatalogPage.CREATE_VARIANT },
                onOpenEditVariant = { currentPage = CatalogPage.EDIT_VARIANT },
            )

            CatalogPage.CREATE_WORKER -> WorkerEditorScreen(
                worker = null,
                isSaving = isSaving,
                onSave = { name, isOnLine, onSuccess ->
                    onCreateWorker(name) {
                        onSuccess()
                        currentPage = CatalogPage.OVERVIEW
                    }
                },
                onDelete = null,
            )

            CatalogPage.EDIT_WORKER -> WorkerEditorScreen(
                worker = selectedWorker,
                isSaving = isSaving,
                onSave = { name, isOnLine, onSuccess ->
                    selectedWorker?.let { worker ->
                        onUpdateWorker(worker.id, name, isOnLine) {
                            onSuccess()
                            currentPage = CatalogPage.OVERVIEW
                        }
                    }
                },
                onDelete = selectedWorker?.let { worker ->
                    {
                        onDeleteWorker(worker.id) {
                            currentPage = CatalogPage.OVERVIEW
                        }
                    }
                },
            )

            CatalogPage.CREATE_POSITION -> PositionEditorScreen(
                position = null,
                isSaving = isSaving,
                onSave = { id, name, description, onSuccess ->
                    onCreatePosition(id, name, description) {
                        onSuccess()
                        currentPage = CatalogPage.OVERVIEW
                    }
                },
                onDelete = null,
            )

            CatalogPage.EDIT_POSITION -> PositionEditorScreen(
                position = selectedPosition,
                isSaving = isSaving,
                onSave = { id, name, description, onSuccess ->
                    selectedPosition?.let { position ->
                        onUpdatePosition(position.id, name, description) {
                            onSuccess()
                            currentPage = CatalogPage.OVERVIEW
                        }
                    }
                },
                onDelete = selectedPosition?.let { position ->
                    {
                        onDeletePosition(position.id) {
                            currentPage = CatalogPage.OVERVIEW
                        }
                    }
                },
            )

            CatalogPage.CREATE_VARIANT -> VariantEditorScreen(
                position = selectedPosition,
                variant = null,
                isSaving = isSaving,
                onSave = { id, name, price, isActive, onSuccess ->
                    selectedPosition?.let { position ->
                        onCreatePositionVariant(position.id, id, name, price) {
                            onSuccess()
                            currentPage = CatalogPage.OVERVIEW
                        }
                    }
                },
                onDelete = null,
            )

            CatalogPage.EDIT_VARIANT -> VariantEditorScreen(
                position = selectedPosition,
                variant = selectedPositionVariant,
                isSaving = isSaving,
                onSave = { id, name, price, isActive, onSuccess ->
                    selectedPositionVariant?.let { variant ->
                        onUpdatePositionVariant(variant.id, name, price, isActive) {
                            onSuccess()
                            currentPage = CatalogPage.OVERVIEW
                        }
                    }
                },
                onDelete = selectedPositionVariant?.let { variant ->
                    {
                        onDeletePositionVariant(variant.id) {
                            currentPage = CatalogPage.OVERVIEW
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun CatalogOverviewScreen(
    workers: List<Worker>,
    selectedWorker: Worker?,
    onWorkerClick: (Worker) -> Unit,
    onOpenCreateWorker: () -> Unit,
    onOpenEditWorker: () -> Unit,
    positions: List<Position>,
    selectedPosition: Position?,
    positionVariants: List<PositionVariant>,
    selectedPositionVariant: PositionVariant?,
    onPositionClick: (Position) -> Unit,
    onPositionVariantClick: (PositionVariant) -> Unit,
    onOpenCreatePosition: () -> Unit,
    onOpenEditPosition: () -> Unit,
    onOpenCreateVariant: () -> Unit,
    onOpenEditVariant: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
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
                    onWorkerClick = onWorkerClick,
                    onOpenCreate = onOpenCreateWorker,
                    onOpenEdit = onOpenEditWorker,
                    modifier = Modifier.fillMaxWidth(),
                )

                PositionsPanel(
                    positions = positions,
                    selectedPosition = selectedPosition,
                    positionVariantsCount = positionVariants.size,
                    onPositionClick = onPositionClick,
                    onOpenCreate = onOpenCreatePosition,
                    onOpenEdit = onOpenEditPosition,
                    modifier = Modifier.fillMaxWidth(),
                )

                VariantsPanel(
                    selectedPosition = selectedPosition,
                    positionVariants = positionVariants,
                    selectedPositionVariant = selectedPositionVariant,
                    onPositionVariantClick = onPositionVariantClick,
                    onOpenCreate = onOpenCreateVariant,
                    onOpenEdit = onOpenEditVariant,
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
                    onWorkerClick = onWorkerClick,
                    onOpenCreate = onOpenCreateWorker,
                    onOpenEdit = onOpenEditWorker,
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
                        positionVariantsCount = positionVariants.size,
                        onPositionClick = onPositionClick,
                        onOpenCreate = onOpenCreatePosition,
                        onOpenEdit = onOpenEditPosition,
                        modifier = Modifier.weight(1f),
                    )

                    VariantsPanel(
                        selectedPosition = selectedPosition,
                        positionVariants = positionVariants,
                        selectedPositionVariant = selectedPositionVariant,
                        onPositionVariantClick = onPositionVariantClick,
                        onOpenCreate = onOpenCreateVariant,
                        onOpenEdit = onOpenEditVariant,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkersPanel(
    workers: List<Worker>,
    selectedWorker: Worker?,
    onWorkerClick: (Worker) -> Unit,
    onOpenCreate: () -> Unit,
    onOpenEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Сотрудники",
        modifier = modifier,
        action = {
            HeaderActions(
                primaryTitle = "+ Добавить",
                onPrimaryClick = onOpenCreate,
                secondaryTitle = "Изменить",
                onSecondaryClick = onOpenEdit,
                secondaryEnabled = selectedWorker != null,
            )
        },
    ) {
        if (workers.isEmpty()) {
            AppStateMessage(
                title = "Сотрудников пока нет",
                description = "Откройте отдельную страницу и создайте первого сотрудника.",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp),
            )
            return@SectionCard
        }

        SelectableList(
            items = workers,
            selectedItem = selectedWorker,
            itemKey = Worker::id,
            onItemClick = onWorkerClick,
            modifier = Modifier.heightIn(min = 220.dp, max = 360.dp),
        ) { worker ->
            Column {
                Text(text = worker.name, style = AppTheme.typography.title)
                Text(
                    text = if (worker.isOnLine) "В сети" else "Офлайн",
                    color = if (worker.isOnLine) AppTheme.colorScheme.green else AppTheme.colorScheme.divider,
                    style = AppTheme.typography.body,
                )
            }
        }

        SelectionSummary(
            title = selectedWorker?.name ?: "Сотрудник не выбран",
            subtitle = selectedWorker?.let { if (it.isOnLine) "Сейчас в сети" else "Сейчас офлайн" }
                ?: "Выберите карточку из списка, затем откройте отдельную страницу редактирования.",
            subtitleColor = if (selectedWorker?.isOnLine == true) {
                AppTheme.colorScheme.green
            } else {
                AppTheme.colorScheme.divider
            },
        )
    }
}

@Composable
private fun PositionsPanel(
    positions: List<Position>,
    selectedPosition: Position?,
    positionVariantsCount: Int,
    onPositionClick: (Position) -> Unit,
    onOpenCreate: () -> Unit,
    onOpenEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Позиции",
        modifier = modifier,
        action = {
            HeaderActions(
                primaryTitle = "+ Добавить",
                onPrimaryClick = onOpenCreate,
                secondaryTitle = "Изменить",
                onSecondaryClick = onOpenEdit,
                secondaryEnabled = selectedPosition != null,
            )
        },
    ) {
        if (positions.isEmpty()) {
            AppStateMessage(
                title = "Позиции пока не заведены",
                description = "Создайте позицию на отдельной странице, чтобы потом добавлять варианты.",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp),
            )
            return@SectionCard
        }

        SelectableList(
            items = positions,
            selectedItem = selectedPosition,
            itemKey = Position::id,
            onItemClick = onPositionClick,
            modifier = Modifier.heightIn(min = 220.dp, max = 320.dp),
        ) { position ->
            Column {
                Text(text = position.name, style = AppTheme.typography.title)
                Text(
                    text = position.id,
                    color = AppTheme.colorScheme.divider,
                    style = AppTheme.typography.body,
                )
            }
        }

        SelectionSummary(
            title = selectedPosition?.name ?: "Позиция не выбрана",
            subtitle = selectedPosition?.let { "${it.id} • вариантов: $positionVariantsCount" }
                ?: "Выберите позицию, чтобы открыть отдельные страницы редактирования и вариантов.",
        )
    }
}

@Composable
private fun VariantsPanel(
    selectedPosition: Position?,
    positionVariants: List<PositionVariant>,
    selectedPositionVariant: PositionVariant?,
    onPositionVariantClick: (PositionVariant) -> Unit,
    onOpenCreate: () -> Unit,
    onOpenEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = selectedPosition?.name?.let { "Варианты: $it" } ?: "Варианты",
        modifier = modifier,
        action = {
            HeaderActions(
                primaryTitle = "+ Добавить",
                onPrimaryClick = onOpenCreate,
                secondaryTitle = "Изменить",
                onSecondaryClick = onOpenEdit,
                primaryEnabled = selectedPosition != null,
                secondaryEnabled = selectedPositionVariant != null,
            )
        },
    ) {
        if (selectedPosition == null) {
            AppStateMessage(
                title = "Сначала выберите позицию",
                description = "У вариантов всегда есть родительская позиция. После выбора откроются страницы добавления и редактирования.",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp),
            )
            return@SectionCard
        }

        if (positionVariants.isEmpty()) {
            AppStateMessage(
                title = "Вариантов пока нет",
                description = "Добавьте первый вариант для выбранной позиции на отдельной странице.",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp),
            )
            return@SectionCard
        }

        SelectableList(
            items = positionVariants,
            selectedItem = selectedPositionVariant,
            itemKey = PositionVariant::id,
            onItemClick = onPositionVariantClick,
            modifier = Modifier.heightIn(min = 220.dp, max = 300.dp),
        ) { variant ->
            Column {
                Text(text = variant.name, style = AppTheme.typography.title)
                Text(
                    text = "${variant.price.format(2)} • ${if (variant.isActive) "Активен" else "Выключен"}",
                    color = if (variant.isActive) AppTheme.colorScheme.green else AppTheme.colorScheme.red,
                    style = AppTheme.typography.body,
                )
            }
        }

        SelectionSummary(
            title = selectedPositionVariant?.name ?: "Вариант не выбран",
            subtitle = selectedPositionVariant?.let { "${it.id} • ${it.price.format(2)}" }
                ?: "Выберите вариант, чтобы открыть отдельную страницу редактирования.",
        )
    }
}

@Composable
private fun WorkerEditorScreen(
    worker: Worker?,
    isSaving: Boolean,
    onSave: (String, Boolean, () -> Unit) -> Unit,
    onDelete: (() -> Unit)?,
) {
    if (worker == null && onDelete != null) {
        MissingSelectionState(
            title = "Сотрудник не выбран",
            description = "Вернитесь назад, выберите сотрудника в списке и затем откройте страницу редактирования.",
        )
        return
    }

    val isCreateMode = worker == null
    var name by rememberSaveable(worker?.id) { mutableStateOf(worker?.name.orEmpty()) }
    var isOnline by rememberSaveable(worker?.id) { mutableStateOf(worker?.isOnLine ?: false) }

    EditorScrollContainer {
        SectionCard(title = if (isCreateMode) "Новый сотрудник" else "Карточка сотрудника") {
            Text(
                text = if (isCreateMode) {
                    "Имя создаётся на отдельной странице, поэтому поле больше не ужимается в боковой панели."
                } else {
                    "Здесь можно обновить имя сотрудника и вручную переключить статус онлайн."
                },
                style = AppTheme.typography.body.copy(color = AppTheme.colorScheme.divider),
            )

            AppFormTextField(
                value = name,
                onValueChange = { name = it },
                label = "Имя",
                singleLine = true,
            )

            if (!isCreateMode) {
                SwitchRow(
                    title = "Онлайн",
                    checked = isOnline,
                    onCheckedChange = { isOnline = it },
                )
            }

            ActionRow(
                primaryTitle = if (isCreateMode) "Добавить" else "Сохранить",
                onPrimaryClick = { onSave(name, isOnline) { name = "" } },
                primaryEnabled = name.isNotBlank() && !isSaving,
                primaryColor = if (isCreateMode) AppTheme.colorScheme.green else AppTheme.colorScheme.accent,
                secondaryTitle = if (isCreateMode) null else "Удалить",
                onSecondaryClick = { onDelete?.invoke() },
                secondaryEnabled = !isSaving,
                isSaving = isSaving,
            )
        }
    }
}

@Composable
private fun PositionEditorScreen(
    position: Position?,
    isSaving: Boolean,
    onSave: (String, String, String, () -> Unit) -> Unit,
    onDelete: (() -> Unit)?,
) {
    if (position == null && onDelete != null) {
        MissingSelectionState(
            title = "Позиция не выбрана",
            description = "Вернитесь назад, выберите позицию и затем откройте страницу редактирования.",
        )
        return
    }

    val isCreateMode = position == null
    var id by rememberSaveable(position?.id) { mutableStateOf(position?.id.orEmpty()) }
    var name by rememberSaveable(position?.id) { mutableStateOf(position?.name.orEmpty()) }
    var description by rememberSaveable(position?.id) { mutableStateOf(position?.description.orEmpty()) }

    EditorScrollContainer {
        SectionCard(title = if (isCreateMode) "Новая позиция" else "Редактирование позиции") {
            Text(
                text = if (isCreateMode) {
                    "Создайте продукт на отдельной странице, затем привязывайте к нему варианты."
                } else {
                    "ID зафиксирован, здесь меняются только название и описание."
                },
                style = AppTheme.typography.body.copy(color = AppTheme.colorScheme.divider),
            )

            AppFormTextField(
                value = id,
                onValueChange = { id = it },
                label = "ID",
                enabled = isCreateMode,
                singleLine = true,
            )
            AppFormTextField(
                value = name,
                onValueChange = { name = it },
                label = "Название",
                singleLine = true,
            )
            AppFormTextField(
                value = description,
                onValueChange = { description = it },
                label = "Описание",
                minLines = 3,
            )

            ActionRow(
                primaryTitle = if (isCreateMode) "Добавить" else "Сохранить",
                onPrimaryClick = {
                    onSave(id, name, description) {
                        id = ""
                        name = ""
                        description = ""
                    }
                },
                primaryEnabled = id.isNotBlank() && name.isNotBlank() && !isSaving,
                primaryColor = if (isCreateMode) AppTheme.colorScheme.green else AppTheme.colorScheme.accent,
                secondaryTitle = if (isCreateMode) null else "Удалить",
                onSecondaryClick = { onDelete?.invoke() },
                secondaryEnabled = !isSaving,
                isSaving = isSaving,
            )
        }
    }
}

@Composable
private fun VariantEditorScreen(
    position: Position?,
    variant: PositionVariant?,
    isSaving: Boolean,
    onSave: (String, String, Float, Boolean, () -> Unit) -> Unit,
    onDelete: (() -> Unit)?,
) {
    val isCreateMode = variant == null
    if (position == null) {
        MissingSelectionState(
            title = "Позиция не выбрана",
            description = "Вернитесь назад, выберите позицию и затем откройте страницу добавления или редактирования варианта.",
        )
        return
    }
    if (variant == null && onDelete != null) {
        MissingSelectionState(
            title = "Вариант не выбран",
            description = "Вернитесь назад, выберите вариант из списка и затем откройте страницу редактирования.",
        )
        return
    }

    var id by rememberSaveable(variant?.id, position.id) { mutableStateOf(variant?.id.orEmpty()) }
    var name by rememberSaveable(variant?.id, position.id) { mutableStateOf(variant?.name.orEmpty()) }
    var price by rememberSaveable(variant?.id, position.id) {
        mutableStateOf(variant?.price?.format(2).orEmpty())
    }
    var isActive by rememberSaveable(variant?.id, position.id) { mutableStateOf(variant?.isActive ?: true) }

    val parsedPrice = remember(price) { price.replace(",", ".").toFloatOrNull() }

    EditorScrollContainer {
        SectionCard(title = if (isCreateMode) "Новый вариант" else "Редактирование варианта") {
            Text(
                text = "Родительская позиция: ${position.name}",
                style = AppTheme.typography.body.copy(color = AppTheme.colorScheme.divider),
            )

            AppFormTextField(
                value = id,
                onValueChange = { id = it },
                label = "ID",
                enabled = isCreateMode,
                singleLine = true,
            )
            AppFormTextField(
                value = name,
                onValueChange = { name = it },
                label = "Название",
                singleLine = true,
            )
            AppFormTextField(
                value = price,
                onValueChange = { price = it },
                label = "Цена",
                singleLine = true,
            )

            if (!isCreateMode) {
                SwitchRow(
                    title = "Активен",
                    checked = isActive,
                    onCheckedChange = { isActive = it },
                )
            }

            ActionRow(
                primaryTitle = if (isCreateMode) "Добавить" else "Сохранить",
                onPrimaryClick = {
                    parsedPrice?.let { value ->
                        onSave(id, name, value, isActive) {
                            id = ""
                            name = ""
                            price = ""
                            isActive = true
                        }
                    }
                },
                primaryEnabled = id.isNotBlank() && name.isNotBlank() && parsedPrice != null && !isSaving,
                primaryColor = if (isCreateMode) AppTheme.colorScheme.green else AppTheme.colorScheme.accent,
                secondaryTitle = if (isCreateMode) null else "Удалить",
                onSecondaryClick = { onDelete?.invoke() },
                secondaryEnabled = !isSaving,
                isSaving = isSaving,
            )
        }
    }
}

@Composable
private fun EditorScrollContainer(
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppTheme.dimensions.basePadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
        content = content,
    )
}

@Composable
private fun MissingSelectionState(
    title: String,
    description: String,
) {
    AppStateMessage(
        title = title,
        description = description,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun HeaderActions(
    primaryTitle: String,
    onPrimaryClick: () -> Unit,
    secondaryTitle: String? = null,
    onSecondaryClick: () -> Unit = {},
    primaryEnabled: Boolean = true,
    secondaryEnabled: Boolean = true,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
    ) {
        SectionActionButton(
            title = primaryTitle,
            onClick = onPrimaryClick,
            enabled = primaryEnabled,
            color = AppTheme.colorScheme.green,
        )

        secondaryTitle?.let {
            SectionActionButton(
                title = secondaryTitle,
                onClick = onSecondaryClick,
                enabled = secondaryEnabled,
                color = AppTheme.colorScheme.accent,
            )
        }
    }
}

@Composable
private fun SelectionSummary(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    subtitleColor: Color = AppTheme.colorScheme.divider,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
            .background(AppTheme.colorScheme.background)
            .padding(AppTheme.dimensions.basePadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
    ) {
        Text(
            text = title,
            style = AppTheme.typography.title,
        )
        Text(
            text = subtitle,
            color = subtitleColor,
            style = AppTheme.typography.body,
        )
    }
}

@Composable
private fun SwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = AppTheme.typography.body,
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
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
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val useStackedHeader = maxWidth < 480.dp

            if (useStackedHeader) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding / 2),
                ) {
                    Text(
                        text = title,
                        style = AppTheme.typography.title,
                    )

                    action?.invoke()
                }
            } else {
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
            }
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
        AppTheme.typography.body.copy(color = AppTheme.colorScheme.text)
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
                textStyle = textStyle,
                cursorBrush = SolidColor(AppTheme.colorScheme.accent),
            )
        }
    }
}

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
    primaryColor: Color,
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
