package by.cyberpunkfandom.data.database.positionitems

import by.cyberpunkfandom.data.database.orders.OrdersTable
import by.cyberpunkfandom.data.database.positions.PositionsTable
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantsTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.time.Instant

object PositionItemsTable : IntIdTable("position_items") {

    val createdAtMillis = long("create_at_millis").apply {
        defaultValueFun = { Instant.now().toEpochMilli() }
    }

    val order = reference("order_id", OrdersTable, onDelete = ReferenceOption.CASCADE)
    val position = reference("position_id", PositionsTable, onDelete = ReferenceOption.CASCADE)
    val positionVariant = reference("position_variant_id", PositionVariantsTable, onDelete = ReferenceOption.CASCADE)
}
