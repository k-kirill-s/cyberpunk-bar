package by.cyberpunkfandom.barfrontend.data.mappers

import by.cyberpunkfandom.barfrontend.data.models.WorkerDto
import by.cyberpunkfandom.barfrontend.domain.Worker

class WorkerMapper {

    fun getDomain(dto: WorkerDto): Worker {
        return Worker(
            id = dto.id,
            name = dto.name,
            isOnLine = dto.isOnLine,
            canBeCashier = dto.canBeCashier,
            canBeBartender = dto.canBeBartender,
        )
    }
}
