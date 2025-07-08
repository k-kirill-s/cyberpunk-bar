package by.cyberpunkfandom.data.database.positionitems

import by.cyberpunkfandom.data.database.orders.OrdersTable
import by.cyberpunkfandom.data.database.positions.PositionsTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object PositionItemsTable : IntIdTable("position_items") {

    val order = reference("order_id", OrdersTable, onDelete = ReferenceOption.CASCADE)
    val position = reference("position_id", PositionsTable, onDelete = ReferenceOption.CASCADE)
}
