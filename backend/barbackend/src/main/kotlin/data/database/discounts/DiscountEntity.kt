package by.cyberpunkfandom.data.database.discounts

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DiscountEntity(id: EntityID<String>) : Entity<String>(id) {

    var name by DiscountsTable.name
    var value by DiscountsTable.value

    companion object : EntityClass<String, DiscountEntity>(DiscountsTable)
}
