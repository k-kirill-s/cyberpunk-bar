package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.WorkerDto
import by.cyberpunkfandom.domain.models.Worker

class WorkerDtoMapper {

    fun getDto(domain: Worker): WorkerDto {
        return WorkerDto(
            id = domain.id,
            name = domain.name,
            isOnLine = domain.isOnLine,
            canBeCashier = domain.canBeCashier,
            canBeBartender = domain.canBeBartender,
        )
    }
}
