package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.PositionDto
import by.cyberpunkfandom.domain.models.Position

class PositionDtoMapper {

    fun getDto(domain: Position): PositionDto {
        return PositionDto(
            id = domain.id,
            name = domain.name,
            description = domain.description,
        )
    }
}
