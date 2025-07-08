package by.cyberpunkfandom.domain.models

import java.time.Instant
import kotlin.math.min

class OrderFull(
    val id: Int,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val status: OrderStatus,
    val positionItems: List<PositionItem>,
    val discounts: List<Discount>,
) {

    val price: Float

    init {
        val totalDiscount = min(0.5, discounts.sumOf { it.value.toDouble() })
        price = (positionItems.sumOf { it.price.toDouble() } * (1 - totalDiscount)).toFloat()
    }
}
