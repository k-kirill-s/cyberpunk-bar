package by.cyberpunkfandom.barfrontend.data.mappers

import by.cyberpunkfandom.barfrontend.data.models.PositionExtraItemDto
import by.cyberpunkfandom.barfrontend.domain.PositionExtraItem

class PositionExtraItemMapper(
    private val positionExtraMapper: PositionExtraMapper,
) {

    fun getDomain(dto: PositionExtraItemDto): PositionExtraItem {
        return PositionExtraItem(
            id = dto.id,
            positionExtra = positionExtraMapper.getDomain(dto.positionExtra),
            price = dto.price,
        )
    }
}
