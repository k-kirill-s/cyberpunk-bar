package by.cyberpunkfandom.barfrontend.data.mappers

import by.cyberpunkfandom.barfrontend.data.models.OrderDto
import by.cyberpunkfandom.barfrontend.domain.Order
import by.cyberpunkfandom.barfrontend.domain.OrderStatus
import kotlin.time.Instant

class OrderMapper(
    private val workerMapper: WorkerMapper,
) {

    fun getDomain(dto: OrderDto): Order {
        return Order(
            id = dto.id,
            name = dto.name,
            createdAt = Instant.fromEpochMilliseconds(dto.createdAt),
            updatedAt = Instant.fromEpochMilliseconds(dto.updatedAt),
            status = OrderStatus.valueOf(dto.status),
            createdBy = dto.createdBy?.let(workerMapper::getDomain),
            completedBy = dto.completedBy?.let(workerMapper::getDomain),
        )
    }
}
