package by.cyberpunkfandom.data.database.workers

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class WorkerEntity(id: EntityID<Int>) : IntEntity(id) {

    var name by WorkersTable.name

    var isOnLine by WorkersTable.isOnLine

    var canBeCashier by WorkersTable.canBeCashier

    var canBeBartender by WorkersTable.canBeBartender

    companion object : IntEntityClass<WorkerEntity>(WorkersTable)
}
