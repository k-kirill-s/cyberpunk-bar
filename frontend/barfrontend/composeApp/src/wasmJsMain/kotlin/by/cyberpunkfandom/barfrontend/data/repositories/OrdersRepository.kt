package by.cyberpunkfandom.barfrontend.data.repositories

import by.cyberpunkfandom.barfrontend.data.mappers.OrderFullMapper
import by.cyberpunkfandom.barfrontend.data.services.MainService
import by.cyberpunkfandom.barfrontend.domain.OrderFull

class OrdersRepository(
    private val mainService: MainService,
    private val orderFullMapper: OrderFullMapper,
) {

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
}
