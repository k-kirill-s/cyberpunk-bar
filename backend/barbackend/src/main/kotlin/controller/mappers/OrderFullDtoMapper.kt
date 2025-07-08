package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.OrderFullDto
import by.cyberpunkfandom.domain.models.OrderFull

class OrderFullDtoMapper(
    private val positionItemDtoMapper: PositionItemDtoMapper,
    private val discountDtoMapper: DiscountDtoMapper,
) {

    fun getDto(domain: OrderFull): OrderFullDto {
        return OrderFullDto(
            id = domain.id,
            name = domain.name,
            createdAt = domain.createdAt.toEpochMilli(),
            updatedAt = domain.updatedAt.toEpochMilli(),
            status = domain.status.name,
            price = domain.price,
            positionItems = domain.positionItems.map { positionItemDtoMapper.getDto(it) },
            discounts = domain.discounts.map { discountDtoMapper.getDto(it) },
        )
    }
}
