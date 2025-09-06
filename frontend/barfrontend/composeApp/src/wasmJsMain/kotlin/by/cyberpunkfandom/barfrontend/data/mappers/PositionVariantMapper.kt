package by.cyberpunkfandom.barfrontend.data.mappers

import by.cyberpunkfandom.barfrontend.data.models.PositionVariantDto
import by.cyberpunkfandom.barfrontend.domain.PositionVariant

class PositionVariantMapper {

    fun getDomain(dto: PositionVariantDto): PositionVariant {
        return PositionVariant(
            id = dto.id,
            name = dto.name,
            price = dto.price,
            isActive = dto.isActive,
        )
    }
}
