package by.cyberpunkfandom.data.mappers

import by.cyberpunkfandom.data.database.orders.OrderFullEntity
import by.cyberpunkfandom.domain.models.OrderFull
import by.cyberpunkfandom.domain.models.OrderStatus
import java.time.Instant

class OrderFullMapper(private val positionItemMapper: PositionItemMapper) {

    fun getDomain(entity: OrderFullEntity): OrderFull {
        return OrderFull(
            id = entity.id.value,
            name = entity.formedIndex.toString(),
            createdAt = Instant.ofEpochMilli(entity.createdAt),
            updatedAt = Instant.ofEpochMilli(entity.lastStatusChangedEvent?.happenedAt ?: entity.createdAt),
            status = entity.lastStatusChangedEvent?.status?.let { OrderStatus.valueOf(it) } ?: OrderStatus.CREATED,
            positionItems = entity.positionItems.map { positionItemMapper.getDomain(it) },
        )
    }
}
