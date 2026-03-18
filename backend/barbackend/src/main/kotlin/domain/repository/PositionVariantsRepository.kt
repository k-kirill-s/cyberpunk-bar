package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.PositionVariant

interface PositionVariantsRepository {

    suspend fun getPositionVariants(): List<PositionVariant>

    suspend fun getPositionVariantsByPosition(
        positionId: String,
    ): List<PositionVariant>

    suspend fun addPositionVariant(
        name: String,
        price: Float,
    ): PositionVariant

    suspend fun deletePositionVariant(
        id: String,
    )

    suspend fun updatePositionVariant(
        id: String,
        name: String?,
        price: Float?,
        isActive: Boolean?
    ): PositionVariant
}
