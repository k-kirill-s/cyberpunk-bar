package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.PositionVariantDto
import by.cyberpunkfandom.domain.models.PositionVariant

class PositionVariantDtoMapper {

    fun getDto(domain: PositionVariant): PositionVariantDto {
        return PositionVariantDto(
            id = domain.id,
            name = domain.name,
            price = domain.price,
            isActive = domain.isActive,
        )
    }
}
