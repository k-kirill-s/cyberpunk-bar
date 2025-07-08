package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.PositionExtraItemDtoMapper
import by.cyberpunkfandom.domain.repository.PositionExtraItemsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.positionExtraItemsRouting() {

    val positionExtraItemsRepository by inject<PositionExtraItemsRepository>()

    val positionExtraItemDtoMapper by inject<PositionExtraItemDtoMapper>()

    routing {
        get("/position_items/{position_item_id}/position_extra") {
            val positionItemId = requireNotNull(call.parameters["position_item_id"]).toInt()
            val positionExtraItems = positionExtraItemsRepository.getPositionExtraItems(positionItemId)
            val positionExtraItemsDto = positionExtraItems.map { positionExtraItemDtoMapper.getDto(it) }
            call.respond(positionExtraItemsDto)
        }

        post("/position_items/{position_item_id}/position_extra") {
            val positionItemId = requireNotNull(call.parameters["position_item_id"]).toInt()
            val formParameters = call.receiveParameters()
            val positionExtraId = requireNotNull(formParameters["position_extra_id"])
            val positionExtraItem = positionExtraItemsRepository.addPositionExtraItem(
                positionItemId = positionItemId,
                positionExtraId = positionExtraId,
            )
            val positionExtraItemDto = positionExtraItemDtoMapper.getDto(positionExtraItem)
            call.respond(positionExtraItemDto)
        }

        delete("/position_extra_items/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            positionExtraItemsRepository.deletePositionExtraItem(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
