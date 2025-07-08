package by.cyberpunkfandom.barfrontend.data.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderFullDto(
    val id: Int,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val status: String,
    val price: Float,
    val positionItems: List<PositionItemDto>,
)
