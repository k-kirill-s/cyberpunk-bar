package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.PositionExtra

interface PositionExtraRepository {

    suspend fun getPositionExtra(): List<PositionExtra>

    suspend fun addPositionExtra(
        id: String,
        name: String,
        price: Float,
    ): PositionExtra

    suspend fun deletePositionExtra(
        id: String,
    )

    suspend fun updatePositionExtra(
        id: String,
        name: String?,
        price: Float?,
        isActive: Boolean?,
    ): PositionExtra
}
