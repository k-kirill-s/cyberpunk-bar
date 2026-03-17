package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.Order
import by.cyberpunkfandom.domain.models.OrderFull

interface OrdersRepository {

    suspend fun getOrders(): List<Order>

    suspend fun getActiveOrders(): List<Order>

    suspend fun getNextOrderToCollect(): OrderFull

    suspend fun getOrderInProgressByWorker(workerId: Int): OrderFull

    suspend fun getOrder(id: Int): OrderFull

    suspend fun createOrder(createdByWorkerId: Int): OrderFull

    suspend fun deleteOrder(id: Int)

    // statuses

    suspend fun formOrder(id: Int): OrderFull

    suspend fun startOrder(id: Int, workerId: Int): OrderFull

    suspend fun finishOrder(id: Int, workerId: Int): OrderFull

    suspend fun giveOrder(id: Int): OrderFull

    suspend fun declineOrder(id: Int): OrderFull
}
