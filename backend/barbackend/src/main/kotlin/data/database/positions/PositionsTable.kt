package by.cyberpunkfandom.data.database.positions

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object PositionsTable : IdTable<String>("positions") {

    override val id: Column<EntityID<String>> = text("id").entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)

    val name = text("name")
    val description = text("description")
    val price = float("price")
    val isActive = bool("is_active").default(true)
}
