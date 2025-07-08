package by.cyberpunkfandom.data.database.orderdiscounts

import by.cyberpunkfandom.data.database.discounts.DiscountsTable
import by.cyberpunkfandom.data.database.orders.OrdersTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object OrderDiscountsTable : Table("order_discounts") {

    val order = reference("order_id", OrdersTable, onDelete = ReferenceOption.CASCADE)
    val discount = reference("discount_id", DiscountsTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(order, discount)
}
