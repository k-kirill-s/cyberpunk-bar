package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.DiscountDto
import by.cyberpunkfandom.domain.models.Discount

class DiscountDtoMapper {

    fun getDto(domain: Discount): DiscountDto {
        return DiscountDto(
            id = domain.id,
            name = domain.name,
            value = domain.value,
        )
    }
}
