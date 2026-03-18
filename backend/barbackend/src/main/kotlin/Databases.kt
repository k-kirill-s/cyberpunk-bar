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
            DO $$
            DECLARE
                existing_constraint text;
                delete_rule "char";
            BEGIN
                SELECT con.conname, con.confdeltype
                INTO existing_constraint, delete_rule
                FROM pg_constraint con
                JOIN pg_class rel ON rel.oid = con.conrelid
                JOIN pg_class referenced_rel ON referenced_rel.oid = con.confrelid
                JOIN pg_attribute attr ON attr.attrelid = rel.oid AND attr.attnum = ANY(con.conkey)
                WHERE con.contype = 'f'
                  AND rel.relname = 'position_variants'
                  AND referenced_rel.relname = 'positions'
                  AND attr.attname = 'position_id'
                LIMIT 1;

                IF existing_constraint IS NOT NULL AND delete_rule <> 'n' THEN
                    EXECUTE format('ALTER TABLE position_variants DROP CONSTRAINT %I', existing_constraint);
                    existing_constraint := NULL;
                END IF;

                IF existing_constraint IS NULL THEN
                    ALTER TABLE position_variants
                    ADD CONSTRAINT position_variants_position_id_fkey
                    FOREIGN KEY (position_id) REFERENCES positions(id) ON DELETE SET NULL;
                END IF;
            END $$;
            """.trimIndent()
        )
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
