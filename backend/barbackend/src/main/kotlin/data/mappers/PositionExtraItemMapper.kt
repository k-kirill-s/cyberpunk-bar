package by.cyberpunkfandom.data.mappers

import by.cyberpunkfandom.data.database.positionextraitems.PositionExtraItemEntity
import by.cyberpunkfandom.domain.models.PositionExtraItem

class PositionExtraItemMapper(
    private val positionExtraMapper: PositionExtraMapper,
) {

    fun getDomain(entity: PositionExtraItemEntity): PositionExtraItem {
        return PositionExtraItem(
            id = entity.id.value,
            positionExtra = positionExtraMapper.getDomain(entity.positionExtra),
        )
    }
}
