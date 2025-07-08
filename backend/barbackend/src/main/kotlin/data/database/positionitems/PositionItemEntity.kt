package by.cyberpunkfandom.data.database.positionitems

import by.cyberpunkfandom.data.database.orders.OrderEntity
import by.cyberpunkfandom.data.database.positionextraitems.PositionExtraItemEntity
import by.cyberpunkfandom.data.database.positionextraitems.PositionExtraItemsTable
import by.cyberpunkfandom.data.database.positions.PositionEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PositionItemEntity(id: EntityID<Int>) : IntEntity(id) {

    var order by OrderEntity referencedOn PositionItemsTable.order
    var position by PositionEntity referencedOn PositionItemsTable.position

    val extraItems by PositionExtraItemEntity referrersOn PositionExtraItemsTable.positionItem

    companion object : IntEntityClass<PositionItemEntity>(PositionItemsTable)
}
