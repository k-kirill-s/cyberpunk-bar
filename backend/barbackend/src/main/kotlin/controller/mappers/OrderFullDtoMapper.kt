package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.OrderFullDto
import by.cyberpunkfandom.domain.models.OrderFull

class OrderFullDtoMapper(
    private val positionItemDtoMapper: PositionItemDtoMapper,
    private val workerDtoMapper: WorkerDtoMapper,
) {

    fun getDto(domain: OrderFull): OrderFullDto {
        return OrderFullDto(
            id = domain.id,
            name = domain.name,
            createdAt = domain.createdAt.toEpochMilli(),
            updatedAt = domain.updatedAt.toEpochMilli(),
            status = domain.status.name,
            createdBy = domain.createdBy?.let(workerDtoMapper::getDto),
            completedBy = domain.completedBy?.let(workerDtoMapper::getDto),
            price = domain.price,
            positionItems = domain.positionItems.map { positionItemDtoMapper.getDto(it) },
        )
    }
}
