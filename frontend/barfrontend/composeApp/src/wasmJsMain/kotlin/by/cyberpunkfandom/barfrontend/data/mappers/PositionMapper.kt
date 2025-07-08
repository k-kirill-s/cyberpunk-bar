package by.cyberpunkfandom.barfrontend.data.mappers

import by.cyberpunkfandom.barfrontend.data.models.PositionDto
import by.cyberpunkfandom.barfrontend.domain.Position

class PositionMapper {

    fun getDomain(dto: PositionDto): Position {
        return Position(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            price = dto.price,
            isActive = dto.isActive,
        )
    }
}
