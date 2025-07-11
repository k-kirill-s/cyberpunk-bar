package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.orderevents.OrderStatusChangedEventsTable
import by.cyberpunkfandom.data.database.orders.OrderEntity
import by.cyberpunkfandom.data.database.orders.OrderFullEntity
import by.cyberpunkfandom.data.database.orders.OrdersTable
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.OrderFullMapper
import by.cyberpunkfandom.data.mappers.OrderMapper
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
        OrderEntity.all().map { orderMapper.getDomain(it) }
    }

    override suspend fun getActiveOrders(): List<Order> = suspendTransaction {
        val activeStatuses = OrderStatus.entries.filter { it.isActive }.map { it.name }
        val activeOrderIds = OrdersTable.join(
            OrderStatusChangedEventsTable,
            JoinType.INNER,
            additionalConstraint = { OrdersTable.lastStatusChangedEvent eq OrderStatusChangedEventsTable.id },
        )
            .selectAll()
            .where { OrderStatusChangedEventsTable.status inList activeStatuses }
            .map { it[OrdersTable.id].value }

        OrderEntity.find { OrdersTable.id inList activeOrderIds }
            .map { orderMapper.getDomain(it) }
    }

    override suspend fun getNextOrderToCollect(): OrderFull? = suspendTransaction {
        val orderId = OrdersTable.join(
            OrderStatusChangedEventsTable,
            JoinType.INNER,
            additionalConstraint = { OrdersTable.lastStatusChangedEvent eq OrderStatusChangedEventsTable.id },
        )
            .selectAll()
            .where { OrderStatusChangedEventsTable.status eq OrderStatus.FORMED.name }
            .orderBy(OrderStatusChangedEventsTable.happenedAt to SortOrder.ASC)
            .map { it[OrdersTable.id].value }
            .firstOrNull()

        orderId
            ?.let { OrderFullEntity.findById(it) }
            ?.let { orderFullMapper.getDomain(it) }
    }

    override suspend fun getOrderInProgressByWorker(workerId: Int): OrderFull? = suspendTransaction {
        val orderId = OrdersTable.join(
            OrderStatusChangedEventsTable,
            JoinType.INNER,
            additionalConstraint = { OrdersTable.lastStatusChangedEvent eq OrderStatusChangedEventsTable.id },
        )
            .selectAll()
            .where { (OrderStatusChangedEventsTable.status eq OrderStatus.STARTED.name) and (OrdersTable.workerId eq workerId) }
            .map { it[OrdersTable.id].value }
            .firstOrNull()

        orderId
            ?.let { OrderFullEntity.findById(it) }
            ?.let { orderFullMapper.getDomain(it) }
    }

    override suspend fun getOrder(id: Int): OrderFull? = suspendTransaction {
        getOrderFull(id)
    }

    override suspend fun createOrder(): OrderFull = suspendTransaction {
        val entity = OrderFullEntity.new {}
        requireNotNull(getOrderFull(entity.id.value))
    }

    override suspend fun deleteOrder(id: Int): Unit = suspendTransaction {
        OrderEntity.findById(id)?.delete()
    }

    override suspend fun formOrder(id: Int): OrderFull = suspendTransaction {
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
        val order = requireNotNull(OrderFullEntity.findById(id))
        require(order.worker == null)

        OrdersTable.update(where = { OrdersTable.id eq id }) {
            it[this.workerId] = workerId
        }
        changeOrderStatus(id, OrderStatus.STARTED)
    }

    override suspend fun finishOrder(id: Int): OrderFull = suspendTransaction {
        changeOrderStatus(id, OrderStatus.FINISHED)
    }

    override suspend fun giveOrder(id: Int): OrderFull = suspendTransaction {
        changeOrderStatus(id, OrderStatus.GIVEN)
    }

    override suspend fun declineOrder(id: Int): OrderFull = suspendTransaction {
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
        return requireNotNull(getOrderFull(orderId))
    }

    private fun getOrderFull(orderId: Int): OrderFull? {
        val orderFullEntity = OrderFullEntity.findById(orderId) ?: return null
        return orderFullMapper.getDomain(orderFullEntity)
    }
}
