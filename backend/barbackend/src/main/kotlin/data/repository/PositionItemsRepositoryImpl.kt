package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.orders.OrderEntity
import by.cyberpunkfandom.data.database.positionitems.PositionItemEntity
import by.cyberpunkfandom.data.database.positionitems.PositionItemsTable
import by.cyberpunkfandom.data.database.positions.PositionEntity
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.PositionItemMapper
import by.cyberpunkfandom.domain.models.PositionItem
import by.cyberpunkfandom.domain.repository.PositionItemsRepository

class PositionItemsRepositoryImpl(
    private val positionItemMapper: PositionItemMapper,
) : PositionItemsRepository {

    override suspend fun getPositionItems(orderId: Int): List<PositionItem> = suspendTransaction {
        PositionItemEntity.find { PositionItemsTable.order eq orderId }
            .map { positionItemMapper.getDomain(it) }
    }

    override suspend fun addPositionItem(orderId: Int, positionId: String): PositionItem = suspendTransaction {
        val order = OrderEntity[orderId]
        val barPosition = PositionEntity[positionId]
        val positionItem = PositionItemEntity.new {
            this.order = order
            this.position = barPosition
        }
        positionItemMapper.getDomain(positionItem)
    }

    override suspend fun deletePositionItem(positionItemId: Int) = suspendTransaction {
        PositionItemEntity[positionItemId].delete()
    }
}
