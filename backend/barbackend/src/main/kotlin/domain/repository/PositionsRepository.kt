package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.Position

interface PositionsRepository {

    suspend fun getPositions(): List<Position>

    suspend fun addPosition(
        id: String,
        name: String,
        description: String,
        price: Float,
    ): Position

    suspend fun deletePosition(
        id: String,
    )

    suspend fun updatePosition(
        id: String,
        name: String?,
        price: Float?,
        isActive: Boolean?,
    ): Position
}
