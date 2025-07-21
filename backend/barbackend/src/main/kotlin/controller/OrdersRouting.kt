package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.OrderDtoMapper
import by.cyberpunkfandom.controller.mappers.OrderFullDtoMapper
import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException
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

        get("/orders/next") {
            val nextOrderToCollect = ordersRepository.getNextOrderToCollect()

            val dto = orderFullDtoMapper.getDto(nextOrderToCollect)
            call.respond(dto)
        }

        get("/orders/by_worker/{worker_id}") {
            val workerId = call.parameters["worker_id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val activeOrderByWorker = ordersRepository.getOrderInProgressByWorker(workerId)

            val dto = orderFullDtoMapper.getDto(activeOrderByWorker)
            call.respond(dto)
        }

        get("/orders/{id}") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val order = ordersRepository.getOrder(id)

            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        post("/orders") {
            val order = ordersRepository.createOrder()

            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        delete("/orders/{id}") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            ordersRepository.deleteOrder(id)

            call.respond(HttpStatusCode.NoContent)
        }

        // Change status

        post("/orders/{id}/form") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val order = ordersRepository.formOrder(id)

            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        post("/orders/{id}/start") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val formParameters = call.receiveParameters()
            val workerId = formParameters["worker_id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val order = ordersRepository.startOrder(id = id, workerId = workerId)

            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        post("/orders/{id}/finish") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val order = ordersRepository.finishOrder(id)

            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        post("/orders/{id}/give") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val order = ordersRepository.giveOrder(id)

            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }

        post("/orders/{id}/decline") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val order = ordersRepository.declineOrder(id)

            val dto = orderFullDtoMapper.getDto(order)
            call.respond(dto)
        }
    }
}
