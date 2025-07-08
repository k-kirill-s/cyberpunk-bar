package by.cyberpunkfandom.data.database.discounts

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object DiscountsTable : IdTable<String>("discounts") {

    override val id: Column<EntityID<String>> = text("id").entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)

    val name = text("name")
    val value = float("value")
}
