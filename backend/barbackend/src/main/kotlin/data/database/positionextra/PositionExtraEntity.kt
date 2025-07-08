package by.cyberpunkfandom.data.database.positionextra

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PositionExtraEntity(id: EntityID<String>) : Entity<String>(id) {

    var name by PositionExtraTable.name
    var price by PositionExtraTable.price
    var isActive by PositionExtraTable.isActive

    companion object : EntityClass<String, PositionExtraEntity>(PositionExtraTable)
}
