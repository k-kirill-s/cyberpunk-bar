package by.cyberpunkfandom.barfrontend.data.repositories

import by.cyberpunkfandom.barfrontend.data.mappers.PositionMapper
import by.cyberpunkfandom.barfrontend.data.services.MainService
import by.cyberpunkfandom.barfrontend.domain.Position

class PositionsRepository(
    private val mainService: MainService,
    private val positionMapper: PositionMapper,
) {

    suspend fun getPositions(): List<Position> {
        val dtoList = mainService.getPositions()
        return dtoList.map { positionMapper.getDomain(it) }
    }

    suspend fun getActivePositions(): List<Position> {
        val dtoList = mainService.getActivePositions()
        return dtoList.map { positionMapper.getDomain(it) }
    }

    suspend fun createPosition(
        id: String,
        name: String,
        description: String,
    ): Position {
        val dto = mainService.createPosition(id = id, name = name, description = description)
        return positionMapper.getDomain(dto)
    }

    suspend fun updatePosition(
        positionId: String,
        name: String?,
        description: String?,
    ): Position {
        val dto = mainService.updatePosition(positionId = positionId, name = name, description = description)
        return positionMapper.getDomain(dto)
    }

    suspend fun deletePosition(positionId: String) {
        mainService.deletePosition(positionId)
    }
}
