package by.cyberpunkfandom.data.mappers

import by.cyberpunkfandom.data.database.workers.WorkerEntity
import by.cyberpunkfandom.domain.models.Worker

class WorkerMapper {

    fun getDomain(entity: WorkerEntity): Worker {
        return Worker(
            id = entity.id.value,
            name = entity.name,
            isOnLine = entity.isOnLine,
            canBeCashier = entity.canBeCashier,
            canBeBartender = entity.canBeBartender,
        )
    }
}
