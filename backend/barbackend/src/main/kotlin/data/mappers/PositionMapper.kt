package by.cyberpunkfandom.data.mappers

import by.cyberpunkfandom.data.database.positions.PositionEntity
import by.cyberpunkfandom.domain.models.Position

class PositionMapper {

    fun getDomain(entity: PositionEntity): Position {
        return Position(
            id = entity.id.value,
            name = entity.name,
            description = entity.description,
        )
    }
}
