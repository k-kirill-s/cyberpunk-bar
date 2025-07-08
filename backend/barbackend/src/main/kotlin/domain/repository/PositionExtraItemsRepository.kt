package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.PositionExtraItem

interface PositionExtraItemsRepository {

    suspend fun getPositionExtraItems(positionItemId: Int): List<PositionExtraItem>

    suspend fun addPositionExtraItem(positionItemId: Int, positionExtraId: String): PositionExtraItem

    suspend fun deletePositionExtraItem(positionExtraItemId: Int)
}
