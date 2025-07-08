package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.PositionExtraItemDto
import by.cyberpunkfandom.domain.models.PositionExtraItem

class PositionExtraItemDtoMapper(
    private val positionExtraDtoMapper: PositionExtraDtoMapper,
) {

    fun getDto(domain: PositionExtraItem): PositionExtraItemDto {
        return PositionExtraItemDto(
            id = domain.id,
            positionExtra = positionExtraDtoMapper.getDto(domain.positionExtra),
            price = domain.price,
        )
    }
}
