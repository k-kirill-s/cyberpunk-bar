package by.cyberpunkfandom.barfrontend.data.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: Int,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val status: String,
)
