package by.cyberpunkfandom.barfrontend.domain

import kotlin.time.Instant

data class OrderFull(
    val id: Int,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val status: OrderStatus,
    val price: Float,
    val positionItems: List<PositionItem>,
)
