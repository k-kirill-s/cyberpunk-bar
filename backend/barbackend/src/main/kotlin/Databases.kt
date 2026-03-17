package by.cyberpunkfandom

import by.cyberpunkfandom.data.database.orderevents.OrderStatusChangedEventsTable
import by.cyberpunkfandom.data.database.orders.OrdersTable
import by.cyberpunkfandom.data.database.positionitems.PositionItemsTable
import by.cyberpunkfandom.data.database.positionvariantpositions.PositionVariantPositionsTable
import by.cyberpunkfandom.data.database.positions.PositionsTable
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantsTable
import by.cyberpunkfandom.data.database.workers.WorkersTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val dbHost = System.getenv("DB_HOST") ?: "db"
    val dbPort = System.getenv("DB_PORT") ?: "5432"
    val dbName = System.getenv("DB_NAME") ?: "bar"
    val dbUser = System.getenv("DB_USER") ?: "postgres"
    val dbPassword = System.getenv("DB_PASSWORD") ?: "postgres"

    Database.connect(
        url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
        user = dbUser,
        password = dbPassword
    )

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.createMissingTablesAndColumns(
            PositionsTable,
            PositionVariantsTable,
            PositionVariantPositionsTable,
            OrdersTable,
            PositionItemsTable,
            OrderStatusChangedEventsTable,
            WorkersTable,
        )

        exec("ALTER TABLE position_variants ALTER COLUMN position_id DROP NOT NULL")
        exec(
            """
            INSERT INTO position_variant_positions (position_id, position_variant_id)
            SELECT position_id, id
            FROM position_variants
            WHERE position_id IS NOT NULL
            ON CONFLICT DO NOTHING
            """.trimIndent()
        )
    }
}
