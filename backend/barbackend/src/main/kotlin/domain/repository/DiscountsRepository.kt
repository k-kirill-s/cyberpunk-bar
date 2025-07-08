package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.Discount

interface DiscountsRepository {

    suspend fun getDiscounts(): List<Discount>

    suspend fun addDiscount(
        id: String,
        name: String,
        value: Float,
    ): Discount

    suspend fun deleteDiscount(
        id: String,
    )

    suspend fun updateDiscount(
        id: String,
        name: String?,
        value: Float?,
    ): Discount?
}
