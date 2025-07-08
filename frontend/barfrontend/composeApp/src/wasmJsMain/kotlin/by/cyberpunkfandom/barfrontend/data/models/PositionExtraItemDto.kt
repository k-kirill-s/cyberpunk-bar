package by.cyberpunkfandom.barfrontend.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PositionExtraItemDto(
    val id: Int,
    val positionExtra: PositionExtraDto,
    val price: Float,
)
