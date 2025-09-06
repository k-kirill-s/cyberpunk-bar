package by.cyberpunkfandom.controller.dto

import kotlinx.serialization.Serializable

@Serializable
data class PositionItemDto(
    val id: Int,
    val position: PositionDto,
    val positionVariant: PositionVariantDto,
    val price: Float,
)
