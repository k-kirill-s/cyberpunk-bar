package by.cyberpunkfandom.data.database.positionextraitems

import by.cyberpunkfandom.data.database.positionextra.PositionExtraTable
import by.cyberpunkfandom.data.database.positionitems.PositionItemsTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object PositionExtraItemsTable : IntIdTable("position_extra_items") {

    val positionItem = reference("position_item_id", PositionItemsTable, onDelete = ReferenceOption.CASCADE)
    val positionExtra = reference("position_extra_id", PositionExtraTable, onDelete = ReferenceOption.CASCADE)
}
