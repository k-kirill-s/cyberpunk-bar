package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.positions.PositionEntity
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantEntity
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantsTable
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.PositionMapper
import by.cyberpunkfandom.domain.models.Position
import by.cyberpunkfandom.domain.repository.PositionsRepository

class PositionsRepositoryImpl(
    private val positionMapper: PositionMapper,
) : PositionsRepository {

    override suspend fun getPositions(): List<Position> = suspendTransaction {
        PositionEntity.all()
            .sortedBy { it.id.value }
            .map { positionMapper.getDomain(it) }
    }

    override suspend fun getActivePositions(): List<Position> = suspendTransaction {
        PositionEntity
            .all()
            .filter { PositionVariantEntity.find { PositionVariantsTable.position eq it.id }.count { it.isActive } > 0 }
            .sortedBy { it.id.value }
            .map { positionMapper.getDomain(it) }
    }

    override suspend fun addPosition(
        id: String,
        name: String,
        description: String,
    ): Position = suspendTransaction {
        val newEntity = PositionEntity.new(id = id) {
            this.name = name
            this.description = description
        }
        positionMapper.getDomain(newEntity)
    }

    override suspend fun deletePosition(id: String) = suspendTransaction<Unit> {
        PositionEntity.findById(id)?.delete()
    }

    override suspend fun updatePosition(
        id: String,
        name: String?,
        description: String?,
    ): Position = suspendTransaction {
        val entity = PositionEntity.findByIdAndUpdate(id = id) { barPosition ->
            name?.let { barPosition.name = it }
            description?.let { barPosition.description = it }
        }!!
        positionMapper.getDomain(entity)
    }
}
