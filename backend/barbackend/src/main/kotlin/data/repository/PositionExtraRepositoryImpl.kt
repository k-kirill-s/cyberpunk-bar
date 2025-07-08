package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.positionextra.PositionExtraEntity
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.PositionExtraMapper
import by.cyberpunkfandom.domain.models.PositionExtra
import by.cyberpunkfandom.domain.repository.PositionExtraRepository

class PositionExtraRepositoryImpl(
    private val positionExtraMapper: PositionExtraMapper,
) : PositionExtraRepository {

    override suspend fun getPositionExtra(): List<PositionExtra> = suspendTransaction {
        PositionExtraEntity.all()
            .map { positionExtraMapper.getDomain(it) }
    }

    override suspend fun addPositionExtra(
        id: String,
        name: String,
        price: Float,
    ): PositionExtra = suspendTransaction {
        val newEntity = PositionExtraEntity.new(id = id) {
            this.name = name
            this.price = price
        }
        positionExtraMapper.getDomain(newEntity)
    }

    override suspend fun deletePositionExtra(id: String) = suspendTransaction<Unit> {
        PositionExtraEntity.findById(id)?.delete()
    }

    override suspend fun updatePositionExtra(
        id: String,
        name: String?,
        price: Float?,
        isActive: Boolean?
    ): PositionExtra? = suspendTransaction {
        val entity = PositionExtraEntity.findByIdAndUpdate(id = id) { itemExtra ->
            name?.let { itemExtra.name = it }
            price?.let { itemExtra.price = it }
            isActive?.let { itemExtra.isActive = it }
        }
        entity?.let { positionExtraMapper.getDomain(it) }
    }
}
