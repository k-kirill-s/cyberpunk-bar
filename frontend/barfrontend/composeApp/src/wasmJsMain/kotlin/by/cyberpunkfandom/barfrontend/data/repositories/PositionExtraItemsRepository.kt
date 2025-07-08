package by.cyberpunkfandom.barfrontend.data.repositories

import by.cyberpunkfandom.barfrontend.data.mappers.PositionExtraItemMapper
import by.cyberpunkfandom.barfrontend.data.services.MainService
import by.cyberpunkfandom.barfrontend.domain.PositionExtraItem

class PositionExtraItemsRepository(
    private val mainService: MainService,
    private val positionExtraItemMapper: PositionExtraItemMapper,
) {

    suspend fun addPositionExtraToPositionItem(
        positionItemId: Int,
        positionExtraId: String,
    ): PositionExtraItem {
        val dto = mainService.addPositionExtraToPositionItem(
            positionItemId = positionItemId,
            positionExtraId = positionExtraId,
        )
        return positionExtraItemMapper.getDomain(dto)
    }

    suspend fun deletePositionExtraItem(positionExtraItemId: Int) {
        mainService.deletePositionExtraItem(positionExtraItemId)
    }
}
