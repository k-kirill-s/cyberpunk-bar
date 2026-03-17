package by.cyberpunkfandom.controller.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkerDto(
    val id: Int,
    val name: String,
    val isOnLine: Boolean,
    val canBeCashier: Boolean,
    val canBeBartender: Boolean,
)
