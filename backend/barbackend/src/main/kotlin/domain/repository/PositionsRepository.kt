package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.Position

interface PositionsRepository {

    suspend fun getPositions(): List<Position>

    suspend fun getActivePositions(): List<Position>

    suspend fun addPosition(
        id: String,
        name: String,
        description: String,
    ): Position

    suspend fun deletePosition(
        id: String,
    )

    suspend fun updatePosition(
        id: String,
        name: String?,
        description: String?,
    ): Position
}
