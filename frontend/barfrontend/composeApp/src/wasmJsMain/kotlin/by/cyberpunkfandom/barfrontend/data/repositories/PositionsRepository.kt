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
}
