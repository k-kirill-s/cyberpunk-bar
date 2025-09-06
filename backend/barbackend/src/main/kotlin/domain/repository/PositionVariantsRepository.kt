package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.PositionVariant

interface PositionVariantsRepository {

    suspend fun getPositionVariants(
        positionId: String,
    ): List<PositionVariant>

    suspend fun addPositionVariant(
        positionId: String,
        id: String,
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
