package by.cyberpunkfandom.domain.models

import java.time.Instant

class Order(
    val id: Int,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val status: OrderStatus,
)
