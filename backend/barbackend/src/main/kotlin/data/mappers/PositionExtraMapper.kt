package by.cyberpunkfandom.data.mappers

import by.cyberpunkfandom.data.database.positionextra.PositionExtraEntity
import by.cyberpunkfandom.domain.models.PositionExtra

class PositionExtraMapper {

    fun getDomain(entity: PositionExtraEntity): PositionExtra {
        return PositionExtra(
            id = entity.id.value,
            name = entity.name,
            price = entity.price,
            isActive = entity.isActive,
        )
    }
}
