package by.cyberpunkfandom.data.database.positions

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PositionEntity(id: EntityID<String>) : Entity<String>(id) {

    var name by PositionsTable.name
    var description by PositionsTable.description
    var price by PositionsTable.price
    var isActive by PositionsTable.isActive

    companion object : EntityClass<String, PositionEntity>(PositionsTable)
}
