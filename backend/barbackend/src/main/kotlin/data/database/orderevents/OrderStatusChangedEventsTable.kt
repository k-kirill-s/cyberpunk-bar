package by.cyberpunkfandom.data.database.orderevents

import by.cyberpunkfandom.data.database.orders.OrdersTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.time.Instant

object OrderStatusChangedEventsTable : IntIdTable("order_status_changed_events") {

    var status = text("status")

    val happenedAt = long("happened_at")
        .apply {
            defaultValueFun = { Instant.now().toEpochMilli() }
        }

    var order = reference("order_id", OrdersTable, onDelete = ReferenceOption.CASCADE)
}
