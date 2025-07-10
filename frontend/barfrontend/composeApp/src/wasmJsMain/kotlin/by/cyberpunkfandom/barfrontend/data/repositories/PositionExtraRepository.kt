package by.cyberpunkfandom.barfrontend.data.repositories

import by.cyberpunkfandom.barfrontend.data.mappers.PositionExtraMapper
import by.cyberpunkfandom.barfrontend.data.services.MainService
import by.cyberpunkfandom.barfrontend.domain.PositionExtra

class PositionExtraRepository(
    private val mainService: MainService,
    private val positionExtraMapper: PositionExtraMapper,
) {

    suspend fun getPositionExtra(): List<PositionExtra> {
        val dtoList = mainService.getPositionExtra()
        return dtoList.map { positionExtraMapper.getDomain(it) }
    }

    suspend fun setPositionExtraIsActive(positionExtraId: String, isActive: Boolean): PositionExtra {
        val dto = mainService.setPositionExtraIsActive(positionExtraId, isActive)
        return positionExtraMapper.getDomain(dto)
    }
}
