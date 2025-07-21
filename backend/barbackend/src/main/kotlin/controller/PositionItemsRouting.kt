package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.PositionItemDtoMapper
import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException
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
            val orderId = call.parameters["order_id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val positionItems = positionItemsRepository.getPositionItems(orderId)

            val positionItemsDto = positionItems.map { positionItemDtoMapper.getDto(it) }
            call.respond(positionItemsDto)
        }

        post("/orders/{order_id}/position_items") {
            val orderId = call.parameters["order_id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val formParameters = call.receiveParameters()
            val positionId = formParameters["position_id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val positionItem = positionItemsRepository.addPositionItem(
                orderId = orderId,
                positionId = positionId
            )

            val positionItemDto = positionItemDtoMapper.getDto(positionItem)
            call.respond(positionItemDto)
        }

        delete("/position_items/{id}") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            positionItemsRepository.deletePositionItem(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
