package by.cyberpunkfandom.data.mappers

import by.cyberpunkfandom.data.database.positionvariants.PositionVariantEntity
import by.cyberpunkfandom.domain.models.PositionVariant

class PositionVariantMapper {

    fun getDomain(entity: PositionVariantEntity): PositionVariant {
        return PositionVariant(
            id = entity.id.value,
            name = entity.name,
            price = entity.price,
            isActive = entity.isActive,
        )
    }
}
