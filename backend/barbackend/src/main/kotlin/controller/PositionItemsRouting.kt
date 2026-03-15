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
            val orderId = call.parameters["order_id"].requiredIntParameter()

            val positionItems = positionItemsRepository.getPositionItems(orderId)

            val positionItemsDto = positionItems.map { positionItemDtoMapper.getDto(it) }
            call.respond(positionItemsDto)
        }

        post("/orders/{order_id}/position_items") {
            val orderId = call.parameters["order_id"].requiredIntParameter()
            val formParameters = call.receiveParameters()
            val positionId = formParameters["position_id"].requiredParameter()
            val positionVariantId = formParameters["position_variant_id"].requiredParameter()

            val positionItem = positionItemsRepository.addPositionItem(
                orderId = orderId,
                positionId = positionId,
                positionVariantId = positionVariantId,
            )

            val positionItemDto = positionItemDtoMapper.getDto(positionItem)
            call.respond(positionItemDto)
        }

        patch("/position_items/{id}") {
            val id = call.parameters["id"].requiredIntParameter()
            val formParameters = call.receiveParameters()
            val isCompleted = formParameters["is_completed"].requiredBooleanParameter()

            val positionItem = positionItemsRepository.updatePositionItem(
                positionItemId = id,
                isCompleted = isCompleted,
            )

            val positionItemDto = positionItemDtoMapper.getDto(positionItem)
            call.respond(positionItemDto)
        }

        delete("/position_items/{id}") {
            val id = call.parameters["id"].requiredIntParameter()

            positionItemsRepository.deletePositionItem(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
