package by.cyberpunkfandom.data.database.workers

import org.jetbrains.exposed.dao.id.IntIdTable

object WorkersTable : IntIdTable("workers") {

    val name = text("name")

    val isOnLine = bool("is_on_line").default(false)

    val canBeCashier = bool("can_be_cashier").default(true)

    val canBeBartender = bool("can_be_bartender").default(true)
}
