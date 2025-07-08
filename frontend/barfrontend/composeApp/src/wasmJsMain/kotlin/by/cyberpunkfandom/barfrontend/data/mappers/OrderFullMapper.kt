package by.cyberpunkfandom.barfrontend.data.mappers

import by.cyberpunkfandom.barfrontend.data.models.OrderFullDto
import by.cyberpunkfandom.barfrontend.domain.OrderFull
import by.cyberpunkfandom.barfrontend.domain.OrderStatus
import kotlin.time.Instant

class OrderFullMapper(
    private val positionItemMapper: PositionItemMapper,
) {

    fun getDomain(dto: OrderFullDto): OrderFull {
        return OrderFull(
            id = dto.id,
            name = dto.name,
            createdAt = Instant.fromEpochMilliseconds(dto.createdAt),
            updatedAt = Instant.fromEpochMilliseconds(dto.updatedAt),
            status = OrderStatus.valueOf(dto.status),
            price = dto.price,
            positionItems = dto.positionItems.map { positionItemMapper.getDomain(it) },
        )
    }
}
