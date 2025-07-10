package by.cyberpunkfandom

import by.cyberpunkfandom.data.database.discounts.DiscountsTable
import by.cyberpunkfandom.data.database.orderdiscounts.OrderDiscountsTable
import by.cyberpunkfandom.data.database.orderevents.OrderStatusChangedEventsTable
import by.cyberpunkfandom.data.database.orders.OrdersTable
import by.cyberpunkfandom.data.database.positionextra.PositionExtraTable
import by.cyberpunkfandom.data.database.positionextraitems.PositionExtraItemsTable
import by.cyberpunkfandom.data.database.positionitems.PositionItemsTable
import by.cyberpunkfandom.data.database.positions.PositionsTable
import by.cyberpunkfandom.data.database.workers.WorkersTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    Database.connect(
        url = "jdbc:postgresql://db:5432/bar",
        user = "postgres",
        password = "postgres"
    )

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(
            PositionsTable,
            PositionExtraTable,
            DiscountsTable,
            OrdersTable,
            PositionItemsTable,
            PositionExtraItemsTable,
            OrderDiscountsTable,
            OrderStatusChangedEventsTable,
            WorkersTable,
        )
    }
}
