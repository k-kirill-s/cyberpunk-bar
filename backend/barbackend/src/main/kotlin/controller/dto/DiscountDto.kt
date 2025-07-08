package by.cyberpunkfandom.controller.dto

import kotlinx.serialization.Serializable

@Serializable
data class DiscountDto(
    val id: String,
    val name: String,
    val value: Float,
)
