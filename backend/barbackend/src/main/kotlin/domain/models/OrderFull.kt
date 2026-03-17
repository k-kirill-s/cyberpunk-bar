package by.cyberpunkfandom.domain.models

import java.time.Instant

class OrderFull(
    val id: Int,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val status: OrderStatus,
    val createdBy: Worker?,
    val completedBy: Worker?,
    val positionItems: List<PositionItem>,
) {

    val price: Float = positionItems.sumOf { it.price.toDouble() }.toFloat()
}
