package by.cyberpunkfandom.data.database.orders

import by.cyberpunkfandom.data.database.orderevents.OrderStatusChangedEventEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderEntity(id: EntityID<Int>) : IntEntity(id) {

    var formedIndex by OrdersTable.formedIndex

    val createdAt by OrdersTable.createdAt

    val lastStatusChangedEvent by OrderStatusChangedEventEntity optionalReferencedOn OrdersTable.lastStatusChangedEvent

    companion object : IntEntityClass<OrderEntity>(OrdersTable)
}
