package by.cyberpunkfandom.data.database.positionextra

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object PositionExtraTable : IdTable<String>("position_extra") {

    override val id: Column<EntityID<String>> = text("id").entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)

    val name = text("name")
    val price = float("price")
    val isActive = bool("is_active").default(true)
}
