package by.cyberpunkfandom.data.database.positionitems

import by.cyberpunkfandom.data.database.orders.OrderEntity
import by.cyberpunkfandom.data.database.positions.PositionEntity
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PositionItemEntity(id: EntityID<Int>) : IntEntity(id) {

    val createdAtMillis by PositionItemsTable.createdAtMillis

    var order by OrderEntity referencedOn PositionItemsTable.order
    var position by PositionEntity referencedOn PositionItemsTable.position
    var positionVariant by PositionVariantEntity referencedOn PositionItemsTable.positionVariant


    companion object : IntEntityClass<PositionItemEntity>(PositionItemsTable)
}
