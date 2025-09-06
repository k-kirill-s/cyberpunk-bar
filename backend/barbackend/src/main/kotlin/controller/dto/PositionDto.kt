package by.cyberpunkfandom.controller.dto

import kotlinx.serialization.Serializable

@Serializable
data class PositionDto(
    val id: String,
    val name: String,
    val description: String,
)
