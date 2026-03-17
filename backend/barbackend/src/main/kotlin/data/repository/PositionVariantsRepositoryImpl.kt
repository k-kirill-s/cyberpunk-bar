package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.positionvariantpositions.PositionVariantPositionsTable
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantEntity
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantsTable
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.PositionVariantMapper
import by.cyberpunkfandom.domain.models.PositionVariant
import by.cyberpunkfandom.domain.repository.PositionVariantsRepository
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class PositionVariantsRepositoryImpl(
    private val positionVariantMapper: PositionVariantMapper,
) : PositionVariantsRepository {

    override suspend fun getPositionVariants(): List<PositionVariant> = suspendTransaction {
        PositionVariantEntity
            .all()
            .sortedBy { it.id.value }
            .map { positionVariantMapper.getDomain(it) }
    }

    override suspend fun getPositionVariantsByPosition(positionId: String): List<PositionVariant> = suspendTransaction {
        PositionVariantEntity
            .wrapRows(
                PositionVariantsTable
                    .innerJoin(PositionVariantPositionsTable)
                    .selectAll()
                    .where { PositionVariantPositionsTable.position eq positionId }
            )
            .sortedBy { it.id.value }
            .map { positionVariantMapper.getDomain(it) }
    }

    override suspend fun addPositionVariant(name: String, price: Float): PositionVariant = suspendTransaction {
        val id = UUID.randomUUID().toString()
        val positionVariant = PositionVariantEntity.new(id = id) {
            this.name = name
            this.price = price
            this.positionId = null
        }
        positionVariantMapper.getDomain(positionVariant)
    }

    override suspend fun deletePositionVariant(id: String) = suspendTransaction {
        PositionVariantEntity[id].delete()
    }

    override suspend fun updatePositionVariant(
        id: String,
        name: String?,
        price: Float?,
        isActive: Boolean?,
    ): PositionVariant = suspendTransaction {
        val entity = PositionVariantEntity.findByIdAndUpdate(id = id) { positionVariant ->
            name?.let { positionVariant.name = it }
            price?.let { positionVariant.price = it }
            isActive?.let { positionVariant.isActive = it }
        }!!
        positionVariantMapper.getDomain(entity)
    }
}
