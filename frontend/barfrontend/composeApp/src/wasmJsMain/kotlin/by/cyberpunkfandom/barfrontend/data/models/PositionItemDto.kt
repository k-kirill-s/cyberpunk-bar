package by.cyberpunkfandom.barfrontend.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PositionItemDto(
    val id: Int,
    val position: PositionDto,
    val positionVariant: PositionVariantDto,
    val price: Float,
    val isCompleted: Boolean,
)
