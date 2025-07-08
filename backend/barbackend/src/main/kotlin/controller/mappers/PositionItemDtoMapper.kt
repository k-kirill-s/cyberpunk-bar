package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.PositionItemDto
import by.cyberpunkfandom.domain.models.PositionItem

class PositionItemDtoMapper(
    private val positionDtoMapper: PositionDtoMapper,
    private val positionExtraItemDtoMapper: PositionExtraItemDtoMapper,
) {

    fun getDto(domain: PositionItem): PositionItemDto {
        return PositionItemDto(
            id = domain.id,
            position = positionDtoMapper.getDto(domain.position),
            extraItems = domain.extraItems.map { positionExtraItemDtoMapper.getDto(it) },
            price = domain.price,
        )
    }
}
