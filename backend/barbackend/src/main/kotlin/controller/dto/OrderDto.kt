package by.cyberpunkfandom.controller.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: Int,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val status: String,
    val createdBy: WorkerDto?,
    val completedBy: WorkerDto?,
)
