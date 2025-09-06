package by.cyberpunkfandom.barfrontend.data.mappers

import by.cyberpunkfandom.barfrontend.data.models.PositionItemDto
import by.cyberpunkfandom.barfrontend.domain.PositionItem

class PositionItemMapper(
    private val positionMapper: PositionMapper,
    private val positionVariantMapper: PositionVariantMapper,
) {

    fun getDomain(dto: PositionItemDto): PositionItem {
        return PositionItem(
            id = dto.id,
            position = positionMapper.getDomain(dto.position),
            positionVariant = positionVariantMapper.getDomain(dto.positionVariant),
            price = dto.price,
        )
    }
}
