package by.cyberpunkfandom.data.mappers

import by.cyberpunkfandom.data.database.orders.OrderEntity
import by.cyberpunkfandom.data.mappers.WorkerMapper
import by.cyberpunkfandom.domain.models.Order
import by.cyberpunkfandom.domain.models.OrderStatus
import java.time.Instant

class OrderMapper(
    private val workerMapper: WorkerMapper,
) {

    fun getDomain(entity: OrderEntity): Order {
        return Order(
            id = entity.id.value,
            name = entity.formedIndex.toString(),
            createdAt = Instant.ofEpochMilli(entity.createdAt),
            updatedAt = Instant.ofEpochMilli(entity.lastStatusChangedEvent?.happenedAt ?: entity.createdAt),
            status = entity.lastStatusChangedEvent?.status?.let { OrderStatus.valueOf(it) } ?: OrderStatus.CREATED,
            createdBy = entity.createdBy?.let(workerMapper::getDomain),
            completedBy = entity.completedBy?.let(workerMapper::getDomain),
        )
    }
}
