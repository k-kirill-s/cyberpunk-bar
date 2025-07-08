package by.cyberpunkfandom.data.database.positionextraitems

import by.cyberpunkfandom.data.database.positionextra.PositionExtraEntity
import by.cyberpunkfandom.data.database.positionitems.PositionItemEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PositionExtraItemEntity(id: EntityID<Int>) : IntEntity(id) {

    var positionItem by PositionItemEntity referencedOn PositionExtraItemsTable.positionItem
    var positionExtra by PositionExtraEntity referencedOn PositionExtraItemsTable.positionExtra

    companion object : IntEntityClass<PositionExtraItemEntity>(PositionExtraItemsTable)
}
