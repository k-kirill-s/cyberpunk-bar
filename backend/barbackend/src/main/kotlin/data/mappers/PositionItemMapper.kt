package by.cyberpunkfandom.data.mappers

import by.cyberpunkfandom.data.database.positionitems.PositionItemEntity
import by.cyberpunkfandom.domain.models.PositionItem

class PositionItemMapper(
    private val positionMapper: PositionMapper,
    private val positionExtraItemMapper: PositionExtraItemMapper,
) {

    fun getDomain(entity: PositionItemEntity): PositionItem {
        return PositionItem(
            id = entity.id.value,
            position = positionMapper.getDomain(entity.position),
            extraItems = entity.extraItems.map { positionExtraItemMapper.getDomain(it) },
        )
    }
}
