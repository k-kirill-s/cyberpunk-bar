package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.PositionExtraDto
import by.cyberpunkfandom.domain.models.PositionExtra

class PositionExtraDtoMapper {

    fun getDto(domain: PositionExtra): PositionExtraDto {
        return PositionExtraDto(
            id = domain.id,
            name = domain.name,
            price = domain.price,
            isActive = domain.isActive,
        )
    }
}
