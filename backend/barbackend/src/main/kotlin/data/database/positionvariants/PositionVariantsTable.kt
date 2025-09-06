package by.cyberpunkfandom.data.database.positionvariants

import by.cyberpunkfandom.data.database.positions.PositionsTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption

object PositionVariantsTable : IdTable<String>("position_variants") {

    override val id: Column<EntityID<String>> = text("id").entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)

    val name = text("name")
    val price = float("price")
    val isActive = bool("is_active").default(true)

    val position = reference("position_id", PositionsTable, onDelete = ReferenceOption.CASCADE)
}
