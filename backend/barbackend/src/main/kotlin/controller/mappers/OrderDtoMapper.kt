package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.OrderDto
import by.cyberpunkfandom.domain.models.Order

class OrderDtoMapper(
    private val workerDtoMapper: WorkerDtoMapper,
) {

    fun getDto(domain: Order): OrderDto {
        return OrderDto(
            id = domain.id,
            name = domain.name,
            createdAt = domain.createdAt.toEpochMilli(),
            updatedAt = domain.updatedAt.toEpochMilli(),
            status = domain.status.name,
            createdBy = domain.createdBy?.let(workerDtoMapper::getDto),
            completedBy = domain.completedBy?.let(workerDtoMapper::getDto),
        )
    }
}
