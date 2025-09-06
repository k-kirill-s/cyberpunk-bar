package by.cyberpunkfandom.barfrontend.data.repositories

import by.cyberpunkfandom.barfrontend.data.mappers.PositionItemMapper
import by.cyberpunkfandom.barfrontend.data.services.MainService
import by.cyberpunkfandom.barfrontend.domain.PositionItem

class PositionItemsRepository(
    private val mainService: MainService,
    private val positionItemMapper: PositionItemMapper,
) {

    suspend fun addPositionToOrder(
        orderId: Int,
        positionId: String,
        positionVariantId: String,
    ): PositionItem {
        val dto = mainService.addPositionToOrder(
            orderId = orderId,
            positionId = positionId,
            positionVariantId = positionVariantId,
        )
        return positionItemMapper.getDomain(dto)
    }

    suspend fun deletePositionItem(positionItemId: Int) {
        mainService.deletePositionItem(positionItemId)
    }
}
