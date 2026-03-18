package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.orderevents.OrderStatusChangedEventsTable
import by.cyberpunkfandom.data.database.orders.OrderEntity
import by.cyberpunkfandom.data.database.orders.OrderFullEntity
import by.cyberpunkfandom.data.database.orders.OrdersTable
import by.cyberpunkfandom.data.database.acquireTransactionLock
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.OrderFullMapper
import by.cyberpunkfandom.data.mappers.OrderMapper
import by.cyberpunkfandom.data.database.workers.WorkerEntity
import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException
import by.cyberpunkfandom.domain.models.Order
import by.cyberpunkfandom.domain.models.OrderFull
import by.cyberpunkfandom.domain.models.OrderStatus
import by.cyberpunkfandom.domain.repository.OrdersRepository
import org.jetbrains.exposed.sql.*

class OrdersRepositoryImpl(
    private val orderMapper: OrderMapper,
    private val orderFullMapper: OrderFullMapper,
) : OrdersRepository {

    override suspend fun getOrders(): List<Order> = suspendTransaction {
        val query = getOrdersQuery()
        OrderEntity.wrapRows(query)
            .toList()
            .map { orderMapper.getDomain(it) }
    }

    override suspend fun getActiveOrders(): List<Order> = suspendTransaction {
        val query = getOrdersQuery(OrderStatus.entries.filter { it.isActive })
        OrderEntity.wrapRows(query)
            .toList()
            .map { orderMapper.getDomain(it) }
    }

    override suspend fun getNextOrderToCollect(): OrderFull = suspendTransaction {
        val query = getOrdersQuery(listOf(OrderStatus.FORMED))
        OrderFullEntity.wrapRows(query)
            .firstOrNull()
            ?.let { orderFullMapper.getDomain(it) }
            ?: throw GeneralException(ExceptionCodes.ORDER_NOT_FOUND)
    }

    override suspend fun getOrderInProgressByWorker(workerId: Int): OrderFull = suspendTransaction {
        val query = getOrdersQuery(listOf(OrderStatus.STARTED))
        OrderFullEntity.wrapRows(query)
            .firstOrNull { it.worker?.id?.value == workerId }
            ?.let { orderFullMapper.getDomain(it) }
            ?: throw GeneralException(ExceptionCodes.ORDER_NOT_FOUND)
    }

    override suspend fun getOrder(id: Int): OrderFull = suspendTransaction { getOrderFull(id) }

    override suspend fun createOrder(createdByWorkerId: Int): OrderFull = suspendTransaction {
        val worker = getWorker(createdByWorkerId)
        requireCashier(worker)

        val entity = OrderFullEntity.new {}
        OrdersTable.update(where = { OrdersTable.id eq entity.id.value }) {
            it[this.createdByWorkerId] = createdByWorkerId
        }
        changeOrderStatus(entity.id.value, OrderStatus.CREATED)
    }

    override suspend fun deleteOrder(id: Int): Unit = suspendTransaction {
        assertOrder(id) {}
        OrderFullEntity.findById(id)?.delete()
    }

    override suspend fun formOrder(id: Int): OrderFull = suspendTransaction {
        acquireTransactionLock(FORM_ORDER_LOCK)

        assertOrder(id) { order ->
            checkOrderStatus(order, OrderStatus.CREATED)
            if (order.positionItems.empty()) {
                throw GeneralException(ExceptionCodes.ORDER_MUST_HAVE_ITEMS)
            }
        }

        val maxFormedIndex = OrdersTable.select(OrdersTable.formedIndex.max())
            .map { it[OrdersTable.formedIndex.max()] }
            .firstOrNull() as Int?
            ?: 0

        OrdersTable.update(where = { OrdersTable.id eq id }) {
            it[this.formedIndex] = maxFormedIndex + 1
        }

        changeOrderStatus(id, OrderStatus.FORMED)
    }

    override suspend fun startOrder(id: Int, workerId: Int): OrderFull = suspendTransaction {
        acquireTransactionLock(START_ORDER_LOCK)
        val worker = getWorker(workerId)
        requireBartender(worker)

        assertOrder(id) { order ->
            checkOrderStatus(order, OrderStatus.FORMED)
        }

        OrdersTable.update(where = { OrdersTable.id eq id }) {
            it[this.workerId] = workerId
        }
        changeOrderStatus(id, OrderStatus.STARTED)
    }

    override suspend fun finishOrder(id: Int, workerId: Int): OrderFull = suspendTransaction {
        getWorker(workerId)

        assertOrder(id) { order ->
            checkOrderStatus(order, OrderStatus.STARTED)
            if (order.worker?.id?.value != workerId) {
                throw GeneralException(ExceptionCodes.ORDER_IN_INCOMPATIBLE_STATUS)
            }
        }

        OrdersTable.update(where = { OrdersTable.id eq id }) {
            it[this.completedByWorkerId] = workerId
        }
        changeOrderStatus(id, OrderStatus.FINISHED)
    }

    override suspend fun giveOrder(id: Int, workerId: Int): OrderFull = suspendTransaction {
        val worker = getWorker(workerId)
        requireCashier(worker)

        assertOrder(id) { order ->
            checkOrderStatus(order, OrderStatus.FINISHED)
        }
        OrdersTable.update(where = { OrdersTable.id eq id }) {
            it[this.givenByWorkerId] = workerId
        }
        changeOrderStatus(id, OrderStatus.GIVEN)
    }

    override suspend fun declineOrder(id: Int): OrderFull = suspendTransaction {
        assertOrder(id) { order ->
            if (order.lastStatusChangedEvent?.status in listOf(OrderStatus.GIVEN, OrderStatus.FINISHED).map { it.name }) {
                throw GeneralException(ExceptionCodes.ORDER_IN_INCOMPATIBLE_STATUS)
            }
        }
        changeOrderStatus(id, OrderStatus.DECLINED)
    }

    private fun changeOrderStatus(orderId: Int, status: OrderStatus): OrderFull {
        val eventId = OrderStatusChangedEventsTable.insertAndGetId {
            it[this.status] = status.name
            it[this.order] = orderId
        }
        OrdersTable.update(where = { OrdersTable.id eq orderId }) {
            it[this.lastStatusChangedEvent] = eventId
        }
        return getOrderFull(orderId)
    }

    private fun getOrderFull(orderId: Int): OrderFull {
        val orderFullEntity = OrderFullEntity.findById(orderId) ?: throw GeneralException(ExceptionCodes.ORDER_NOT_FOUND)
        return orderFullMapper.getDomain(orderFullEntity)
    }

    private fun getWorker(workerId: Int): WorkerEntity {
        return WorkerEntity.findById(workerId) ?: throw GeneralException(ExceptionCodes.WORKER_NOT_FOUND)
    }

    private fun requireCashier(worker: WorkerEntity) {
        if (!worker.canBeCashier) {
            throw GeneralException(ExceptionCodes.WORKER_ROLE_NOT_ALLOWED)
        }
    }

    private fun requireBartender(worker: WorkerEntity) {
        if (!worker.canBeBartender) {
            throw GeneralException(ExceptionCodes.WORKER_ROLE_NOT_ALLOWED)
        }
    }

    private fun getOrdersQuery(statuses: List<OrderStatus> = OrderStatus.entries): Query {
        return OrdersTable
            .innerJoin(OrderStatusChangedEventsTable, { lastStatusChangedEvent }, { id })
            .select(OrdersTable.columns)
            .where { OrderStatusChangedEventsTable.status inList statuses.map { it.name } }
            .orderBy(OrderStatusChangedEventsTable.happenedAt)
    }

    private fun assertOrder(id: Int, assert: (OrderFullEntity) -> Unit) {
        val order = OrderFullEntity.findById(id) ?: throw GeneralException(ExceptionCodes.ORDER_NOT_FOUND)
        assert(order)
    }

    private fun checkOrderStatus(order: OrderFullEntity, status: OrderStatus) {
        if (order.lastStatusChangedEvent?.status != status.name) {
            throw GeneralException(ExceptionCodes.ORDER_IN_INCOMPATIBLE_STATUS)
        }
    }

    private companion object {
        const val FORM_ORDER_LOCK = 1_001L
        const val START_ORDER_LOCK = 1_002L
    }
}
