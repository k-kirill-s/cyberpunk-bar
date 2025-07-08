package by.cyberpunkfandom.barfrontend.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PositionExtraDto(
    val id: String,
    val name: String,
    val price: Float,
    val isActive: Boolean,
)
