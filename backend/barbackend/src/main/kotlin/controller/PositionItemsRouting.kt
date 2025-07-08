package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.PositionItemDtoMapper
import by.cyberpunkfandom.domain.repository.PositionItemsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.positionItemsRouting() {

    val positionItemsRepository by inject<PositionItemsRepository>()

    val positionItemDtoMapper by inject<PositionItemDtoMapper>()

    routing {
        get("/orders/{order_id}/position_items") {
            val orderId = requireNotNull(call.parameters["order_id"]).toInt()
            val positionItems = positionItemsRepository.getPositionItems(orderId)
            val positionItemsDto = positionItems.map { positionItemDtoMapper.getDto(it) }
            call.respond(positionItemsDto)
        }

        post("/orders/{order_id}/position_items") {
            val orderId = requireNotNull(call.parameters["order_id"]).toInt()
            val formParameters = call.receiveParameters()
            val positionId = requireNotNull(formParameters["position_id"])
            val positionItem = positionItemsRepository.addPositionItem(
                orderId = orderId,
                positionId = positionId
            )
            val positionItemDto = positionItemDtoMapper.getDto(positionItem)
            call.respond(positionItemDto)
        }

        delete("/position_items/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            positionItemsRepository.deletePositionItem(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
