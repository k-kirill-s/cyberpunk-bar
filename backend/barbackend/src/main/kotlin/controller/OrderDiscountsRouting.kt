package by.cyberpunkfandom.controller

import by.cyberpunkfandom.domain.repository.OrderDiscountsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.ordersDiscountsRouting() {

    val orderDiscountsRepository by inject<OrderDiscountsRepository>()

    routing {
        post("/orders/{order_id}/discounts") {
            val orderId = requireNotNull(call.parameters["order_id"]).toInt()
            val formParameters = call.receiveParameters()
            val discountId = requireNotNull(formParameters["discount_id"])
            orderDiscountsRepository.addDiscountToOrder(
                orderId = orderId,
                discountId = discountId,
            )
            call.respond(HttpStatusCode.NoContent)
        }

        delete("/orders/{order_id}/discounts") {
            val orderId = requireNotNull(call.parameters["order_id"]).toInt()
            val formParameters = call.receiveParameters()
            val discountId = requireNotNull(formParameters["discount_id"])
            orderDiscountsRepository.removeDiscountFromOrder(
                orderId = orderId,
                discountId = discountId,
            )
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
