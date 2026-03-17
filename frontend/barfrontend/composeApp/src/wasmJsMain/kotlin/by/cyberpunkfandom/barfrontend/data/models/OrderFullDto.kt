package by.cyberpunkfandom.barfrontend.data.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderFullDto(
    val id: Int,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val status: String,
    val createdBy: WorkerDto?,
    val completedBy: WorkerDto?,
    val price: Float,
    val positionItems: List<PositionItemDto>,
)
