package by.cyberpunkfandom.data.database.orders

import by.cyberpunkfandom.data.database.orderevents.OrderStatusChangedEventEntity
import by.cyberpunkfandom.data.database.positionitems.PositionItemEntity
import by.cyberpunkfandom.data.database.positionitems.PositionItemsTable
import by.cyberpunkfandom.data.database.workers.WorkerEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderFullEntity(id: EntityID<Int>) : IntEntity(id) {

    var formedIndex by OrdersTable.formedIndex

    val createdAt by OrdersTable.createdAt

    val lastStatusChangedEvent by OrderStatusChangedEventEntity optionalReferencedOn OrdersTable.lastStatusChangedEvent

    val positionItems by PositionItemEntity referrersOn PositionItemsTable.order

    val worker by WorkerEntity optionalReferencedOn OrdersTable.workerId

    val createdBy by WorkerEntity optionalReferencedOn OrdersTable.createdByWorkerId

    val completedBy by WorkerEntity optionalReferencedOn OrdersTable.completedByWorkerId

    val givenBy by WorkerEntity optionalReferencedOn OrdersTable.givenByWorkerId

    companion object : IntEntityClass<OrderFullEntity>(OrdersTable)
}
