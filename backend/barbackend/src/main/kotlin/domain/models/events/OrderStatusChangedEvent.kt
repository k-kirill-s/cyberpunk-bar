package by.cyberpunkfandom.domain.models.events

import by.cyberpunkfandom.domain.models.OrderStatus
import java.time.Instant

class OrderStatusChangedEvent(
    val status: OrderStatus,
    happenedAt: Instant
) : OrderEvent(happenedAt = happenedAt)
