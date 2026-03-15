package by.cyberpunkfandom.data.mappers

import by.cyberpunkfandom.data.database.positionitems.PositionItemEntity
import by.cyberpunkfandom.domain.models.PositionItem

class PositionItemMapper(
    private val positionMapper: PositionMapper,
    private val positionVariantMapper: PositionVariantMapper,
) {

    fun getDomain(entity: PositionItemEntity): PositionItem {
        return PositionItem(
            id = entity.id.value,
            position = positionMapper.getDomain(entity.position),
            positionVariant = positionVariantMapper.getDomain(entity.positionVariant),
            isCompleted = entity.isCompleted,
        )
    }
}
