package by.cyberpunkfandom.barfrontend.domain

import kotlin.time.Instant

data class Order(
    val id: Int,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val status: OrderStatus,
)
