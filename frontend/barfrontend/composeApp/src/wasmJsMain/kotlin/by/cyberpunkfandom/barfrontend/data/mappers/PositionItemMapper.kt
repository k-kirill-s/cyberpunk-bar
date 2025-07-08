package by.cyberpunkfandom.barfrontend.data.mappers

import by.cyberpunkfandom.barfrontend.data.models.PositionItemDto
import by.cyberpunkfandom.barfrontend.domain.PositionItem

class PositionItemMapper(
    private val positionMapper: PositionMapper,
    private val positionExtraItemMapper: PositionExtraItemMapper,
) {

    fun getDomain(dto: PositionItemDto): PositionItem {
        return PositionItem(
            id = dto.id,
            position = positionMapper.getDomain(dto.position),
            extraItems = dto.extraItems.map { positionExtraItemMapper.getDomain(it) },
            price = dto.price,
        )
    }
}
