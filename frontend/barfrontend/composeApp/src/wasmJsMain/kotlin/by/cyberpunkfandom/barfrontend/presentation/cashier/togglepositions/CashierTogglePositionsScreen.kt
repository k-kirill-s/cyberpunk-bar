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
import androidx.compose.material3.MaterialTheme
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
import by.cyberpunkfandom.barfrontend.domain.AdminAnalytics
import by.cyberpunkfandom.barfrontend.domain.DrinkAnalytics
import by.cyberpunkfandom.barfrontend.domain.Position
import by.cyberpunkfandom.barfrontend.domain.PositionVariant
import by.cyberpunkfandom.barfrontend.domain.ProductAnalytics
import by.cyberpunkfandom.barfrontend.domain.Worker
import by.cyberpunkfandom.barfrontend.domain.WorkerAnalytics
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
        analytics = viewModel.analytics.collectAsStateWithLifecycle().value,
        isAnalyticsRefreshing = viewModel.isAnalyticsRefreshing.collectAsStateWithLifecycle().value,
        onAnalyticsRefreshClick = viewModel::onAnalyticsRefreshClick,
        selectedWorkerId = viewModel.selectedWorkerId.collectAsStateWithLifecycle().value,
        onWorkerClick = viewModel::onWorkerClick,
        onCreateWorker = { name, canBeCashier, canBeBartender, onSuccess ->
            viewModel.createWorker(name, canBeCashier, canBeBartender, onSuccess)
        },
        onUpdateWorker = { workerId, name, isOnLine, canBeCashier, canBeBartender, onSuccess ->
            viewModel.updateWorker(workerId, name, isOnLine, canBeCashier, canBeBartender, onSuccess)
        },
        onDeleteWorker = { workerId, onSuccess -> viewModel.deleteWorker(workerId, onSuccess) },
        positions = viewModel.positions.collectAsStateWithLifecycle().value,
        selectedPositionId = viewModel.selectedPositionId.collectAsStateWithLifecycle().value,
        selectedPositionVariantIds = viewModel.selectedPositionVariantIds.collectAsStateWithLifecycle().value,
        onPositionClick = viewModel::onPositionClick,
        onCreatePosition = { name, description, positionVariantIds, onSuccess ->
            viewModel.createPosition(name, description, positionVariantIds, onSuccess)
        },
        onUpdatePosition = { positionId, name, description, positionVariantIds, onSuccess ->
            viewModel.updatePosition(positionId, name, description, positionVariantIds, onSuccess)
        },
        onDeletePosition = { positionId, onSuccess -> viewModel.deletePosition(positionId, onSuccess) },
        positionVariants = viewModel.positionVariants.collectAsStateWithLifecycle().value,
        selectedPositionVariantId = viewModel.selectedPositionVariantId.collectAsStateWithLifecycle().value,
        onPositionVariantClick = viewModel::onPositionVariantClick,
        onCreatePositionVariant = { name, price, onSuccess ->
            viewModel.createPositionVariant(name, price, onSuccess)
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
    analytics: AdminAnalytics?,
    isAnalyticsRefreshing: Boolean,
    onAnalyticsRefreshClick: () -> Unit,
    selectedWorkerId: Int?,
    onWorkerClick: (Worker) -> Unit,
    onCreateWorker: (String, Boolean, Boolean, () -> Unit) -> Unit,
    onUpdateWorker: (Int, String, Boolean, Boolean, Boolean, () -> Unit) -> Unit,
    onDeleteWorker: (Int, () -> Unit) -> Unit,
    positions: List<Position>,
    selectedPositionId: String?,
    selectedPositionVariantIds: Set<String>,
    onPositionClick: (Position) -> Unit,
    onCreatePosition: (String, String, List<String>, () -> Unit) -> Unit,
    onUpdatePosition: (String, String, String, List<String>, () -> Unit) -> Unit,
    onDeletePosition: (String, () -> Unit) -> Unit,
    positionVariants: List<PositionVariant>,
    selectedPositionVariantId: String?,
    onPositionVariantClick: (PositionVariant) -> Unit,
    onCreatePositionVariant: (String, Float, () -> Unit) -> Unit,
    onUpdatePositionVariant: (String, String, Float, Boolean, () -> Unit) -> Unit,
    onDeletePositionVariant: (String, () -> Unit) -> Unit,
) {
    var currentPage by rememberSaveable { mutableStateOf(CatalogPage.OVERVIEW) }

    val selectedWorker = workers.firstOrNull { it.id == selectedWorkerId }
    val selectedPosition = positions.firstOrNull { it.id == selectedPositionId }
    val selectedPositionVariant = positionVariants.firstOrNull { it.id == selectedPositionVariantId }

    val topBarTitle = when (currentPage) {
        CatalogPage.OVERVIEW -> "Каталог и команда"
        CatalogPage.CREATE_WORKER -> "Новый стендовик"
        CatalogPage.EDIT_WORKER -> "Стендовик"
        CatalogPage.CREATE_POSITION -> "Новый напиток"
        CatalogPage.EDIT_POSITION -> "Напиток"
        CatalogPage.CREATE_VARIANT -> "Новый товар"
        CatalogPage.EDIT_VARIANT -> "Товар"
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
                analytics = analytics,
                isAnalyticsRefreshing = isAnalyticsRefreshing,
                onAnalyticsRefreshClick = onAnalyticsRefreshClick,
                selectedWorker = selectedWorker,
                onWorkerClick = onWorkerClick,
                onOpenCreateWorker = { currentPage = CatalogPage.CREATE_WORKER },
                onOpenEditWorker = { currentPage = CatalogPage.EDIT_WORKER },
                positions = positions,
                selectedPosition = selectedPosition,
                selectedPositionVariantIds = selectedPositionVariantIds,
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
                onSave = { name, isOnLine, canBeCashier, canBeBartender, onSuccess ->
                    onCreateWorker(name, canBeCashier, canBeBartender) {
                        onSuccess()
                        currentPage = CatalogPage.OVERVIEW
                    }
                },
                onDelete = null,
            )

            CatalogPage.EDIT_WORKER -> WorkerEditorScreen(
                worker = selectedWorker,
                isSaving = isSaving,
                onSave = { name, isOnLine, canBeCashier, canBeBartender, onSuccess ->
                    selectedWorker?.let { worker ->
                        onUpdateWorker(worker.id, name, isOnLine, canBeCashier, canBeBartender) {
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
                availablePositionVariants = positionVariants,
                initiallySelectedPositionVariantIds = emptySet(),
                isSaving = isSaving,
                onSave = { name, description, positionVariantIds, onSuccess ->
                    onCreatePosition(name, description, positionVariantIds) {
                        onSuccess()
                        currentPage = CatalogPage.OVERVIEW
                    }
                },
                onDelete = null,
            )

            CatalogPage.EDIT_POSITION -> PositionEditorScreen(
                position = selectedPosition,
                availablePositionVariants = positionVariants,
                initiallySelectedPositionVariantIds = selectedPositionVariantIds,
                isSaving = isSaving,
                onSave = { name, description, positionVariantIds, onSuccess ->
                    selectedPosition?.let { position ->
                        onUpdatePosition(position.id, name, description, positionVariantIds) {
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
                variant = null,
                isSaving = isSaving,
                onSave = { name, price, isActive, onSuccess ->
                    onCreatePositionVariant(name, price) {
                        onSuccess()
                        currentPage = CatalogPage.OVERVIEW
                    }
                },
                onDelete = null,
            )

            CatalogPage.EDIT_VARIANT -> VariantEditorScreen(
                variant = selectedPositionVariant,
                isSaving = isSaving,
                onSave = { name, price, isActive, onSuccess ->
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
    analytics: AdminAnalytics?,
    isAnalyticsRefreshing: Boolean,
    onAnalyticsRefreshClick: () -> Unit,
    selectedWorker: Worker?,
    onWorkerClick: (Worker) -> Unit,
    onOpenCreateWorker: () -> Unit,
    onOpenEditWorker: () -> Unit,
    positions: List<Position>,
    selectedPosition: Position?,
    selectedPositionVariantIds: Set<String>,
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
                AnalyticsPanel(
                    analytics = analytics,
                    isRefreshing = isAnalyticsRefreshing,
                    onRefreshClick = onAnalyticsRefreshClick,
                    modifier = Modifier.fillMaxWidth(),
                )

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
                    linkedProductsCount = selectedPositionVariantIds.size,
                    onPositionClick = onPositionClick,
                    onOpenCreate = onOpenCreatePosition,
                    onOpenEdit = onOpenEditPosition,
                    modifier = Modifier.fillMaxWidth(),
                )

                VariantsPanel(
                    positionVariants = positionVariants,
                    selectedPositionVariant = selectedPositionVariant,
                    selectedPositionVariantIds = selectedPositionVariantIds,
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
                    AnalyticsPanel(
                        analytics = analytics,
                        isRefreshing = isAnalyticsRefreshing,
                        onRefreshClick = onAnalyticsRefreshClick,
                        modifier = Modifier.weight(1.1f),
                    )

                    PositionsPanel(
                        positions = positions,
                        selectedPosition = selectedPosition,
                        linkedProductsCount = selectedPositionVariantIds.size,
                        onPositionClick = onPositionClick,
                        onOpenCreate = onOpenCreatePosition,
                        onOpenEdit = onOpenEditPosition,
                        modifier = Modifier.weight(1f),
                    )

                    VariantsPanel(
                        positionVariants = positionVariants,
                        selectedPositionVariant = selectedPositionVariant,
                        selectedPositionVariantIds = selectedPositionVariantIds,
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
private fun AnalyticsPanel(
    analytics: AdminAnalytics?,
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Аналитика",
        modifier = modifier,
        action = {
            SectionActionButton(
                title = if (isRefreshing) "Обновляем..." else "Обновить",
                onClick = onRefreshClick,
                enabled = !isRefreshing,
                color = AppTheme.colorScheme.accent,
            )
        },
    ) {
        if (analytics == null) {
            AppStateMessage(
                title = "Загружаем аналитику",
                isLoading = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp),
            )
            return@SectionCard
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding),
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val compactMetrics = maxWidth < 680.dp

                if (compactMetrics) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding / 2),
                    ) {
                        AnalyticsMetricCard(
                            title = "Выдано заказов",
                            value = analytics.soldOrdersCount.toString(),
                        )
                        AnalyticsMetricCard(
                            title = "Продано позиций",
                            value = analytics.soldItemsCount.toString(),
                        )
                        AnalyticsMetricCard(
                            title = "Выручка",
                            value = analytics.totalRevenue.format(2),
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.basePadding / 2),
                    ) {
                        AnalyticsMetricCard(
                            title = "Выдано заказов",
                            value = analytics.soldOrdersCount.toString(),
                            modifier = Modifier.weight(1f),
                        )
                        AnalyticsMetricCard(
                            title = "Продано позиций",
                            value = analytics.soldItemsCount.toString(),
                            modifier = Modifier.weight(1f),
                        )
                        AnalyticsMetricCard(
                            title = "Выручка",
                            value = analytics.totalRevenue.format(2),
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            AnalyticsSalesSection(
                title = "Продажи по напиткам",
                rows = analytics.drinks,
                name = DrinkAnalytics::positionName,
                count = DrinkAnalytics::soldCount,
                revenue = DrinkAnalytics::revenue,
                emptyText = "Выданных напитков пока нет.",
            )

            AnalyticsSalesSection(
                title = "Продажи по товарам",
                rows = analytics.products,
                name = ProductAnalytics::positionVariantName,
                count = ProductAnalytics::soldCount,
                revenue = ProductAnalytics::revenue,
                emptyText = "Проданных товаров пока нет.",
            )

            AnalyticsWorkersSection(workers = analytics.workers)
        }
    }
}

@Composable
private fun AnalyticsMetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.cornerRadius))
            .background(AppTheme.colorScheme.background)
            .padding(AppTheme.dimensions.basePadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
    ) {
        Text(
            text = title,
            color = AppTheme.colorScheme.divider,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
private fun <T> AnalyticsSalesSection(
    title: String,
    rows: List<T>,
    name: (T) -> String,
    count: (T) -> Int,
    revenue: (T) -> Float,
    emptyText: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )

        if (rows.isEmpty()) {
            Text(
                text = emptyText,
                color = AppTheme.colorScheme.divider,
                style = MaterialTheme.typography.bodyLarge,
            )
            return@Column
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 260.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
        ) {
            rows.forEach { row ->
                SelectionSummary(
                    title = name(row),
                    subtitle = "${count(row)} шт • ${revenue(row).format(2)}",
                )
            }
        }
    }
}

@Composable
private fun AnalyticsWorkersSection(
    workers: List<WorkerAnalytics>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
    ) {
        Text(
            text = "Команда",
            style = MaterialTheme.typography.titleLarge,
        )

        if (workers.isEmpty()) {
            Text(
                text = "Стендовики пока не добавлены.",
                color = AppTheme.colorScheme.divider,
                style = MaterialTheme.typography.bodyLarge,
            )
            return@Column
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 320.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
        ) {
            workers.forEach { worker ->
                SelectionSummary(
                    title = worker.workerName,
                    subtitle = buildString {
                        append("Создал заказов: ${worker.createdOrdersCount}")
                        append(" • ")
                        append("Собрал заказов: ${worker.preparedOrdersCount}")
                        append(" • ")
                        append("Выдал заказов: ${worker.givenOrdersCount}")
                        append(" • ")
                        append("Приготовил напитков: ${worker.preparedDrinksCount}")
                    },
                )
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
        title = "Стендовики",
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
                title = "Стендовиков пока нет",
                description = "Откройте отдельную страницу и создайте первого стендовика.",
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
                Text(text = worker.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = buildString {
                        append(
                            when {
                                worker.canBeCashier && worker.canBeBartender -> "Кассир и бармен"
                                worker.canBeCashier -> "Кассир"
                                worker.canBeBartender -> "Бармен"
                                else -> "Без роли"
                            }
                        )
                        append(" • ")
                        append(if (worker.isOnLine) "В сети" else "Офлайн")
                    },
                    color = if (worker.isOnLine) AppTheme.colorScheme.green else AppTheme.colorScheme.divider,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        SelectionSummary(
            title = selectedWorker?.name ?: "Стендовик не выбран",
            subtitle = selectedWorker?.let {
                buildString {
                    append(
                        when {
                            it.canBeCashier && it.canBeBartender -> "Кассир и бармен"
                            it.canBeCashier -> "Кассир"
                            it.canBeBartender -> "Бармен"
                            else -> "Без роли"
                        }
                    )
                    append(" • ")
                    append(if (it.isOnLine) "Сейчас в сети" else "Сейчас офлайн")
                }
            } ?: "Выберите карточку из списка, затем откройте отдельную страницу редактирования.",
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
    linkedProductsCount: Int,
    onPositionClick: (Position) -> Unit,
    onOpenCreate: () -> Unit,
    onOpenEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Напитки",
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
                title = "Напитки пока не заведены",
                description = "Создайте напиток и сразу отметьте, с какими товарами он продаётся.",
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
                Text(text = position.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = position.description.ifBlank { "Описание не заполнено" },
                    color = AppTheme.colorScheme.divider,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        SelectionSummary(
            title = selectedPosition?.name ?: "Напиток не выбран",
            subtitle = selectedPosition?.let { "Привязано товаров: $linkedProductsCount" }
                ?: "Выберите напиток, чтобы посмотреть и изменить связанные товары.",
        )
    }
}

@Composable
private fun VariantsPanel(
    positionVariants: List<PositionVariant>,
    selectedPositionVariant: PositionVariant?,
    selectedPositionVariantIds: Set<String>,
    onPositionVariantClick: (PositionVariant) -> Unit,
    onOpenCreate: () -> Unit,
    onOpenEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        title = "Товары",
        modifier = modifier,
        action = {
            HeaderActions(
                primaryTitle = "+ Добавить",
                onPrimaryClick = onOpenCreate,
                secondaryTitle = "Изменить",
                onSecondaryClick = onOpenEdit,
                secondaryEnabled = selectedPositionVariant != null,
            )
        },
    ) {
        if (positionVariants.isEmpty()) {
            AppStateMessage(
                title = "Товаров пока нет",
                description = "Добавьте первый товар, задайте ему цену и затем привязывайте к напиткам.",
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
                Text(text = variant.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = buildString {
                        append(variant.price.format(2))
                        append(" • ")
                        append(if (variant.isActive) "Активен" else "Выключен")
                        if (variant.id in selectedPositionVariantIds) {
                            append(" • Привязан к выбранному напитку")
                        }
                    },
                    color = when {
                        variant.id in selectedPositionVariantIds -> AppTheme.colorScheme.accent
                        variant.isActive -> AppTheme.colorScheme.green
                        else -> AppTheme.colorScheme.red
                    },
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        SelectionSummary(
            title = selectedPositionVariant?.name ?: "Товар не выбран",
            subtitle = selectedPositionVariant?.let { "${it.price.format(2)} • ${if (it.isActive) "Активен" else "Выключен"}" }
                ?: "Выберите товар, чтобы открыть отдельную страницу редактирования.",
        )
    }
}

@Composable
private fun WorkerEditorScreen(
    worker: Worker?,
    isSaving: Boolean,
    onSave: (String, Boolean, Boolean, Boolean, () -> Unit) -> Unit,
    onDelete: (() -> Unit)?,
) {
    if (worker == null && onDelete != null) {
        MissingSelectionState(
            title = "Стендовик не выбран",
            description = "Вернитесь назад, выберите стендовика в списке и затем откройте страницу редактирования.",
        )
        return
    }

    val isCreateMode = worker == null
    var name by rememberSaveable(worker?.id) { mutableStateOf(worker?.name.orEmpty()) }
    var isOnline by rememberSaveable(worker?.id) { mutableStateOf(worker?.isOnLine ?: false) }
    var canBeCashier by rememberSaveable(worker?.id) { mutableStateOf(worker?.canBeCashier ?: true) }
    var canBeBartender by rememberSaveable(worker?.id) { mutableStateOf(worker?.canBeBartender ?: true) }

    EditorScrollContainer {
        SectionCard(title = if (isCreateMode) "Новый стендовик" else "Карточка стендовика") {
            Text(
                text = if (isCreateMode) {
                    "Задайте имя и сразу отметьте, может ли стендовик работать кассиром и/или барменом."
                } else {
                    "Здесь можно обновить имя стендовика, роли и вручную переключить статус онлайн."
                },
                style = MaterialTheme.typography.bodyLarge.copy(color = AppTheme.colorScheme.divider),
            )

            AppFormTextField(
                value = name,
                onValueChange = { name = it },
                label = "Имя",
                singleLine = true,
            )

            SwitchRow(
                title = "Может быть кассиром",
                checked = canBeCashier,
                onCheckedChange = { canBeCashier = it },
            )

            SwitchRow(
                title = "Может быть барменом",
                checked = canBeBartender,
                onCheckedChange = { canBeBartender = it },
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
                onPrimaryClick = {
                    onSave(name, isOnline, canBeCashier, canBeBartender) {
                        name = ""
                        canBeCashier = true
                        canBeBartender = true
                    }
                },
                primaryEnabled = name.isNotBlank() && (canBeCashier || canBeBartender) && !isSaving,
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
    availablePositionVariants: List<PositionVariant>,
    initiallySelectedPositionVariantIds: Set<String>,
    isSaving: Boolean,
    onSave: (String, String, List<String>, () -> Unit) -> Unit,
    onDelete: (() -> Unit)?,
) {
    if (position == null && onDelete != null) {
        MissingSelectionState(
            title = "Напиток не выбран",
            description = "Вернитесь назад, выберите напиток и затем откройте страницу редактирования.",
        )
        return
    }

    val isCreateMode = position == null
    var name by rememberSaveable(position?.id) { mutableStateOf(position?.name.orEmpty()) }
    var description by rememberSaveable(position?.id) { mutableStateOf(position?.description.orEmpty()) }
    var selectedPositionVariantIds by rememberSaveable(position?.id) {
        mutableStateOf(initiallySelectedPositionVariantIds.toSet())
    }

    EditorScrollContainer {
        SectionCard(title = if (isCreateMode) "Новый напиток" else "Редактирование напитка") {
            Text(
                text = if (isCreateMode) {
                    "Напиток создаётся отдельно от товаров. Сразу выберите, с какими товарами он доступен."
                } else {
                    "Здесь меняются название, описание и состав доступных товаров для напитка."
                },
                style = MaterialTheme.typography.bodyLarge.copy(color = AppTheme.colorScheme.divider),
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

            PositionVariantsSelector(
                positionVariants = availablePositionVariants,
                selectedPositionVariantIds = selectedPositionVariantIds,
                onCheckedChange = { positionVariantId, isChecked ->
                    selectedPositionVariantIds = if (isChecked) {
                        selectedPositionVariantIds + positionVariantId
                    } else {
                        selectedPositionVariantIds - positionVariantId
                    }
                },
            )

            ActionRow(
                primaryTitle = if (isCreateMode) "Добавить" else "Сохранить",
                onPrimaryClick = {
                    onSave(name, description, selectedPositionVariantIds.toList()) {
                        name = ""
                        description = ""
                        selectedPositionVariantIds = emptySet()
                    }
                },
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
private fun VariantEditorScreen(
    variant: PositionVariant?,
    isSaving: Boolean,
    onSave: (String, Float, Boolean, () -> Unit) -> Unit,
    onDelete: (() -> Unit)?,
) {
    val isCreateMode = variant == null
    if (variant == null && onDelete != null) {
        MissingSelectionState(
            title = "Товар не выбран",
            description = "Вернитесь назад, выберите товар из списка и затем откройте страницу редактирования.",
        )
        return
    }

    var name by rememberSaveable(variant?.id) { mutableStateOf(variant?.name.orEmpty()) }
    var price by rememberSaveable(variant?.id) {
        mutableStateOf(variant?.price?.format(2).orEmpty())
    }
    var isActive by rememberSaveable(variant?.id) { mutableStateOf(variant?.isActive ?: true) }

    val parsedPrice = remember(price) { price.replace(",", ".").toFloatOrNull() }

    EditorScrollContainer {
        SectionCard(title = if (isCreateMode) "Новый товар" else "Редактирование товара") {
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
                        onSave(name, value, isActive) {
                            name = ""
                            price = ""
                            isActive = true
                        }
                    }
                },
                primaryEnabled = name.isNotBlank() && parsedPrice != null && !isSaving,
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
private fun PositionVariantsSelector(
    positionVariants: List<PositionVariant>,
    selectedPositionVariantIds: Set<String>,
    onCheckedChange: (String, Boolean) -> Unit,
) {
    SectionCard(title = "Товары для напитка") {
        if (positionVariants.isEmpty()) {
            Text(
                text = "Сначала добавьте товары в соседнем разделе каталога.",
                style = MaterialTheme.typography.bodyLarge.copy(color = AppTheme.colorScheme.divider),
            )
            return@SectionCard
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
        ) {
            positionVariants.forEach { variant ->
                SwitchRow(
                    title = "${variant.name} • ${variant.price.format(2)}",
                    checked = variant.id in selectedPositionVariantIds,
                    onCheckedChange = { onCheckedChange(variant.id, it) },
                )
            }
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
    BoxWithConstraints {
        val stackButtons = maxWidth < 340.dp

        if (stackButtons) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
            ) {
                SectionActionButton(
                    title = primaryTitle,
                    onClick = onPrimaryClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = primaryEnabled,
                    color = AppTheme.colorScheme.green,
                )

                secondaryTitle?.let {
                    SectionActionButton(
                        title = secondaryTitle,
                        onClick = onSecondaryClick,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = secondaryEnabled,
                        color = AppTheme.colorScheme.accent,
                    )
                }
            }
        } else {
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
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = subtitle,
            color = subtitleColor,
            style = MaterialTheme.typography.bodyLarge,
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
            style = MaterialTheme.typography.bodyLarge,
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
                        style = MaterialTheme.typography.headlineSmall,
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
                        style = MaterialTheme.typography.headlineMedium,
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
            style = MaterialTheme.typography.labelLarge.copy(
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
        MaterialTheme.typography.bodyLarge.copy(color = AppTheme.colorScheme.text)
    } else {
        MaterialTheme.typography.bodyLarge.copy(color = AppTheme.colorScheme.divider)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.thinDivider * 4),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
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
