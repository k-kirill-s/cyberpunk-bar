package by.cyberpunkfandom.data.mappers

import by.cyberpunkfandom.data.database.discounts.DiscountEntity
import by.cyberpunkfandom.domain.models.Discount

class DiscountMapper {

    fun getDomain(entity: DiscountEntity): Discount {
        return Discount(
            id = entity.id.value,
            name = entity.name,
            value = entity.value,
        )
    }
}
