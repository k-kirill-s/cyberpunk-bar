package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.positions.PositionEntity
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantEntity
import by.cyberpunkfandom.data.database.positionvariants.PositionVariantsTable
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.PositionVariantMapper
import by.cyberpunkfandom.domain.models.PositionVariant
import by.cyberpunkfandom.domain.repository.PositionVariantsRepository

class PositionVariantsRepositoryImpl(
    private val positionVariantMapper: PositionVariantMapper,
) : PositionVariantsRepository {

    override suspend fun getPositionVariants(positionId: String): List<PositionVariant> = suspendTransaction {
        PositionVariantEntity
            .find { PositionVariantsTable.position eq positionId }
            .map { positionVariantMapper.getDomain(it) }
    }

    override suspend fun addPositionVariant(positionId: String, id: String, name: String, price: Float): PositionVariant = suspendTransaction {
        val position = PositionEntity[positionId]
        val positionVariant = PositionVariantEntity.new(id = id) {
            this.name = name
            this.price = price
            this.position = position
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
