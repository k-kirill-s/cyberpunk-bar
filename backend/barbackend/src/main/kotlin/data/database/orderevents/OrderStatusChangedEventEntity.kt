package by.cyberpunkfandom.data.database.orderevents

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderStatusChangedEventEntity(id: EntityID<Int>) : IntEntity(id) {

    var status by OrderStatusChangedEventsTable.status
    val happenedAt by OrderStatusChangedEventsTable.happenedAt

    companion object : IntEntityClass<OrderStatusChangedEventEntity>(OrderStatusChangedEventsTable)
}
