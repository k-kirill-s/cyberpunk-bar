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
        name: String,
        description: String,
        positionVariantIds: List<String>,
    ): Position {
        val dto = mainService.createPosition(
            name = name,
            description = description,
            positionVariantIds = positionVariantIds,
        )
        return positionMapper.getDomain(dto)
    }

    suspend fun updatePosition(
        positionId: String,
        name: String?,
        description: String?,
        positionVariantIds: List<String>? = null,
    ): Position {
        val dto = mainService.updatePosition(
            positionId = positionId,
            name = name,
            description = description,
            positionVariantIds = positionVariantIds,
        )
        return positionMapper.getDomain(dto)
    }

    suspend fun deletePosition(positionId: String) {
        mainService.deletePosition(positionId)
    }
}
