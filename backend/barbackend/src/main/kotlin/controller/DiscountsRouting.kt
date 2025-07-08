package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.DiscountDtoMapper
import by.cyberpunkfandom.domain.repository.DiscountsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.discountsRouting() {

    val discountsRepository by inject<DiscountsRepository>()

    val discountDtoMapper by inject<DiscountDtoMapper>()

    routing {
        get("/discounts") {
            val discountsDto = discountsRepository.getDiscounts()
                .map { discountDtoMapper.getDto(it) }
            call.respond(discountsDto)
        }

        post("discounts") {
            val formParameters = call.receiveParameters()
            val id = requireNotNull(formParameters["id"])
            val name = requireNotNull(formParameters["name"])
            val value = requireNotNull(formParameters["value"]).toFloat()
            val discountDto = discountsRepository.addDiscount(id, name, value)
                .let { discountDtoMapper.getDto(it) }
            call.respond(discountDto)
        }

        patch("discounts/{id}") {
            val id = requireNotNull(call.parameters["id"])
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val value = formParameters["value"]?.toFloat()
            val discountDto = discountsRepository.updateDiscount(
                id = id,
                name = name,
                value = value,
            )?.let { discountDtoMapper.getDto(it) }
            if (discountDto != null) {
                call.respond(discountDto)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("discounts/{id}") {
            val id = requireNotNull(call.parameters["id"])
            discountsRepository.deleteDiscount(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
