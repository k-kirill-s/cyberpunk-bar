package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.positionextra.PositionExtraEntity
import by.cyberpunkfandom.data.database.positionextraitems.PositionExtraItemEntity
import by.cyberpunkfandom.data.database.positionextraitems.PositionExtraItemsTable
import by.cyberpunkfandom.data.database.positionitems.PositionItemEntity
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.PositionExtraItemMapper
import by.cyberpunkfandom.domain.models.PositionExtraItem
import by.cyberpunkfandom.domain.repository.PositionExtraItemsRepository

class PositionExtraItemsRepositoryImpl(
    private val positionExtraItemMapper: PositionExtraItemMapper,
) : PositionExtraItemsRepository {

    override suspend fun getPositionExtraItems(positionItemId: Int): List<PositionExtraItem> = suspendTransaction {
        PositionExtraItemEntity.find { PositionExtraItemsTable.positionItem eq positionItemId }
            .map { positionExtraItemMapper.getDomain(it) }
    }

    override suspend fun addPositionExtraItem(
        positionItemId: Int,
        positionExtraId: String,
    ): PositionExtraItem = suspendTransaction {
        val positionItem = PositionItemEntity[positionItemId]
        val positionExtra = PositionExtraEntity[positionExtraId]
        val positionExtraItem = PositionExtraItemEntity.new {
            this.positionItem = positionItem
            this.positionExtra = positionExtra
        }
        positionExtraItemMapper.getDomain(positionExtraItem)
    }

    override suspend fun deletePositionExtraItem(positionExtraItemId: Int) = suspendTransaction {
        PositionExtraItemEntity[positionExtraItemId].delete()
    }
}
