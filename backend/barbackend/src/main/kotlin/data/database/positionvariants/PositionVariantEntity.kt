package by.cyberpunkfandom.data.database.positionvariants

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PositionVariantEntity(id: EntityID<String>) : Entity<String>(id) {

    var name by PositionVariantsTable.name
    var price by PositionVariantsTable.price
    var isActive by PositionVariantsTable.isActive
    var positionId by PositionVariantsTable.position

    companion object : EntityClass<String, PositionVariantEntity>(PositionVariantsTable)
}
