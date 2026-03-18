package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.positions.PositionEntity
import by.cyberpunkfandom.data.database.positionvariantpositions.PositionVariantPositionsTable
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantEntity
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantsTable
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.PositionMapper
import by.cyberpunkfandom.domain.models.Position
import by.cyberpunkfandom.domain.repository.PositionsRepository
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

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
            .filter { position ->
                PositionVariantPositionsTable
                    .innerJoin(PositionVariantsTable)
                    .selectAll()
                    .where {
                        (PositionVariantPositionsTable.position eq position.id) and
                            (PositionVariantsTable.isActive eq true)
                    }
                    .empty()
                    .not()
            }
            .sortedBy { it.id.value }
            .map { positionMapper.getDomain(it) }
    }

    override suspend fun addPosition(
        name: String,
        description: String,
        positionVariantIds: List<String>,
    ): Position = suspendTransaction {
        val id = UUID.randomUUID().toString()
        val newEntity = PositionEntity.new(id = id) {
            this.name = name
            this.description = description
        }
        syncPositionVariants(id, positionVariantIds)
        positionMapper.getDomain(newEntity)
    }

    override suspend fun deletePosition(id: String) = suspendTransaction<Unit> {
        PositionEntity.findById(id)?.delete()
    }

    override suspend fun updatePosition(
        id: String,
        name: String?,
        description: String?,
        positionVariantIds: List<String>?,
    ): Position = suspendTransaction {
        val entity = PositionEntity.findByIdAndUpdate(id = id) { barPosition ->
            name?.let { barPosition.name = it }
            description?.let { barPosition.description = it }
        }!!
        positionVariantIds?.let { syncPositionVariants(id, it) }
        positionMapper.getDomain(entity)
    }

    private fun syncPositionVariants(
        positionId: String,
        positionVariantIds: List<String>,
    ) {
        PositionVariantPositionsTable.deleteWhere { position eq positionId }
        positionVariantIds
            .distinct()
            .forEach { positionVariantId ->
                PositionVariantPositionsTable.insert { statement ->
                    statement[position] = EntityID(positionId, by.cyberpunkfandom.data.database.positions.PositionsTable)
                    statement[positionVariant] = EntityID(positionVariantId, PositionVariantsTable)
                }
            }
    }
}
