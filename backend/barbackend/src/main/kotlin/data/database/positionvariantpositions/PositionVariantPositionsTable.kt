package by.cyberpunkfandom.data.database.positionvariantpositions

import by.cyberpunkfandom.data.database.positions.PositionsTable
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantsTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PositionVariantPositionsTable : Table("position_variant_positions") {

    val position = reference("position_id", PositionsTable, onDelete = ReferenceOption.CASCADE)
    val positionVariant = reference("position_variant_id", PositionVariantsTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey: PrimaryKey = PrimaryKey(position, positionVariant)
}
