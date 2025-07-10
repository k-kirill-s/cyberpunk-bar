package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.OrderDtoMapper
import by.cyberpunkfandom.controller.mappers.OrderFullDtoMapper
import by.cyberpunkfandom.domain.repository.OrdersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@Suppress("DuplicatedCode")
fun Application.ordersRouting() {

    val ordersRepository by inject<OrdersRepository>()

    val orderDtoMapper by inject<OrderDtoMapper>()
    val orderFullDtoMapper by inject<OrderFullDtoMapper>()

    routing {
        get("/orders") {
            val orders = ordersRepository.getOrders()
            val dto = orders.map { orderDtoMapper.getDto(it) }
            call.respond(dto)
        }

        get("/orders/active") {
            val activeOrders = ordersRepository.getActiveOrders()
            val dto = activeOrders.map { orderDtoMapper.getDto(it) }
            call.respond(dto)
        }

        get("/orders/by_worker") {
            val formParameters = call.receiveParameters()
            val workerId = requireNotNull(formParameters["worker_id"]).toInt()
            val activeOrderByWorker = ordersRepository.getOrderInProgressByWorker(workerId)
            val dto = listOfNotNull(activeOrderByWorker?.let { orderFullDtoMapper.getDto(it) })
            call.respond(dto)
        }

        get("/orders/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val order = ordersRepository.getOrder(id)
            val dto = order?.let { orderFullDtoMapper.getDto(it) }
            if (dto != null) {
                call.respond(dto)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/orders") {
            val order = ordersRepository.createOrder()
            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        delete("/orders/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            ordersRepository.deleteOrder(id)
            call.respond(HttpStatusCode.NoContent)
        }

        // Change status

        post("/orders/{id}/form") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val order = ordersRepository.formOrder(id)
            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        post("/orders/{id}/start") {
            val id = requireNotNull(call.parameters["id"]).toInt()

            val formParameters = call.receiveParameters()
            val workerId = requireNotNull(formParameters["worker_id"]).toInt()

            val order = ordersRepository.startOrder(id = id, workerId = workerId)
            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        post("/orders/{id}/finish") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val order = ordersRepository.finishOrder(id)
            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        post("/orders/{id}/give") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val order = ordersRepository.giveOrder(id)
            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        post("/orders/{id}/decline") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val order = ordersRepository.declineOrder(id)
            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }
    }
}
