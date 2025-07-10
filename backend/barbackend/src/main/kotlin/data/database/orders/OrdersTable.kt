package by.cyberpunkfandom.data.database.orders

import by.cyberpunkfandom.data.database.orderevents.OrderStatusChangedEventsTable
import by.cyberpunkfandom.data.database.workers.WorkersTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.time.Instant

object OrdersTable : IntIdTable("orders") {

    val formedIndex = integer("formed_index").default(0)

    val createdAt = long("created_at")
        .apply {
            defaultValueFun = { Instant.now().toEpochMilli() }
        }

    val lastStatusChangedEvent = optReference(
        "last_status_changed_event",
        OrderStatusChangedEventsTable,
        onDelete = ReferenceOption.RESTRICT,
    )

    var workerId = optReference(
        "worker_id",
        WorkersTable,
        onDelete = ReferenceOption.SET_NULL,
    )
}
