package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.PositionItem

interface PositionItemsRepository {

    suspend fun getPositionItems(orderId: Int): List<PositionItem>

    suspend fun addPositionItem(orderId: Int, positionId: String, positionVariantId: String): PositionItem

    suspend fun deletePositionItem(positionItemId: Int)
}
