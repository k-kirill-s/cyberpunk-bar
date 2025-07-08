package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.discounts.DiscountEntity
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.mappers.DiscountMapper
import by.cyberpunkfandom.domain.models.Discount
import by.cyberpunkfandom.domain.repository.DiscountsRepository

class DiscountsRepositoryImpl(
    private val discountMapper: DiscountMapper,
) : DiscountsRepository {

    override suspend fun getDiscounts(): List<Discount> = suspendTransaction {
        DiscountEntity.all()
            .map { discountMapper.getDomain(it) }
    }

    override suspend fun addDiscount(
        id: String,
        name: String,
        value: Float,
    ): Discount = suspendTransaction {
        val newEntity = DiscountEntity.new(id = id) {
            this.name = name
            this.value = value
        }
        discountMapper.getDomain(newEntity)
    }

    override suspend fun deleteDiscount(id: String) = suspendTransaction<Unit> {
        DiscountEntity.findById(id)?.delete()
    }

    override suspend fun updateDiscount(
        id: String,
        name: String?,
        value: Float?,
    ): Discount? = suspendTransaction {
        val entity = DiscountEntity.findByIdAndUpdate(id = id) { discountItem ->
            name?.let { discountItem.name = it }
            value?.let { discountItem.value = it }
        }
        entity?.let { discountMapper.getDomain(it) }
    }
}
