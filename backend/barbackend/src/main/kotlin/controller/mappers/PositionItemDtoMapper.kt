package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.PositionItemDto
import by.cyberpunkfandom.domain.models.PositionItem

class PositionItemDtoMapper(
    private val positionDtoMapper: PositionDtoMapper,
    private val positionVariantDtoMapper: PositionVariantDtoMapper,
) {

    fun getDto(domain: PositionItem): PositionItemDto {
        return PositionItemDto(
            id = domain.id,
            position = positionDtoMapper.getDto(domain.position),
            positionVariant = positionVariantDtoMapper.getDto(domain.positionVariant),
            price = domain.price,
        )
    }
}
