package by.cyberpunkfandom.data.database.workers

import org.jetbrains.exposed.dao.id.IntIdTable

object WorkersTable : IntIdTable("workers") {

    val name = text("name")

    val isOnLine = bool("is_on_line").default(false)
}
