package by.cyberpunkfandom.controller.dto

import kotlinx.serialization.Serializable

@Serializable
data class PositionVariantDto(
    val id: String,
    val name: String,
    val price: Float,
    val isActive: Boolean,
)
