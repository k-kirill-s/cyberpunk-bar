package by.cyberpunkfandom.barfrontend.data.mappers

import by.cyberpunkfandom.barfrontend.data.models.PositionExtraDto
import by.cyberpunkfandom.barfrontend.domain.PositionExtra

class PositionExtraMapper {

    fun getDomain(dto: PositionExtraDto): PositionExtra {
        return PositionExtra(
            id = dto.id,
            name = dto.name,
            price = dto.price,
            isActive = dto.isActive,
        )
    }
}
