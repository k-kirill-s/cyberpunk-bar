package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.positions.PositionEntity
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.PositionMapper
import by.cyberpunkfandom.domain.models.Position
import by.cyberpunkfandom.domain.repository.PositionsRepository

class PositionsRepositoryImpl(
    private val positionMapper: PositionMapper,
) : PositionsRepository {

    override suspend fun getPositions(): List<Position> = suspendTransaction {
        PositionEntity.all()
            .map { positionMapper.getDomain(it) }
    }

    override suspend fun addPosition(
        id: String,
        name: String,
        description: String,
        price: Float,
    ): Position = suspendTransaction {
        val newEntity = PositionEntity.new(id = id) {
            this.name = name
            this.description = description
            this.price = price
        }
        positionMapper.getDomain(newEntity)
    }

    override suspend fun deletePosition(id: String) = suspendTransaction<Unit> {
        PositionEntity.findById(id)?.delete()
    }

    override suspend fun updatePosition(
        id: String,
        name: String?,
        price: Float?,
        isActive: Boolean?
    ): Position? = suspendTransaction {
        val entity = PositionEntity.findByIdAndUpdate(id = id) { barPosition ->
            name?.let { barPosition.name = it }
            price?.let { barPosition.price = it }
            isActive?.let { barPosition.isActive = it }
        }
        entity?.let { positionMapper.getDomain(it) }
    }
}
