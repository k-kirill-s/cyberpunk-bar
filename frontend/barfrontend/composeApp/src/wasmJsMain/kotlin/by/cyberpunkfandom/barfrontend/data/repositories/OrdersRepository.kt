package by.cyberpunkfandom.barfrontend.data.repositories

import by.cyberpunkfandom.barfrontend.data.mappers.OrderFullMapper
import by.cyberpunkfandom.barfrontend.data.mappers.OrderMapper
import by.cyberpunkfandom.barfrontend.data.services.MainService
import by.cyberpunkfandom.barfrontend.domain.Order
import by.cyberpunkfandom.barfrontend.domain.OrderFull

class OrdersRepository(
    private val mainService: MainService,
    private val orderMapper: OrderMapper,
    private val orderFullMapper: OrderFullMapper,
) {

    suspend fun getActiveOrders(): List<Order> {
        val dtoList = mainService.getActiveOrders()
        return dtoList.map { orderMapper.getDomain(it) }
    }

    suspend fun getNextOrderToCollect(): OrderFull? {
        val dto = mainService.getNextOrderToCollect()
        return dto?.let { orderFullMapper.getDomain(it) }
    }

    suspend fun getInProgressOrderByWorker(workerId: Int): OrderFull? {
        val dto = mainService.getInProgressOrderByWorker(workerId)
        return dto?.let { orderFullMapper.getDomain(it) }
    }

    suspend fun getOrder(id: Int): OrderFull {
        val dto = mainService.getOrder(id)
        return orderFullMapper.getDomain(dto)
    }

    suspend fun createOrder(): OrderFull {
        val dto = mainService.createOrder()
        return orderFullMapper.getDomain(dto)
    }

    suspend fun formOrder(orderId: Int): OrderFull {
        val dto = mainService.formOrder(orderId)
        return orderFullMapper.getDomain(dto)
    }

    suspend fun startOrder(orderId: Int, workerId: Int): OrderFull {
        val dto = mainService.startOrder(orderId, workerId)
        return orderFullMapper.getDomain(dto)
    }

    suspend fun finishOrder(orderId: Int): OrderFull {
        val dto = mainService.finishOrder(orderId)
        return orderFullMapper.getDomain(dto)
    }

    suspend fun giveAwayOrder(orderId: Int): OrderFull {
        val dto = mainService.giveAwayOrder(orderId)
        return orderFullMapper.getDomain(dto)
    }

    suspend fun deleteOrder(orderId: Int) {
        mainService.deleteOrder(orderId)
    }
}
