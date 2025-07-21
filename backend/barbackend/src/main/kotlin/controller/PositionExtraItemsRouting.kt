package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.PositionExtraItemDtoMapper
import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException
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
            val positionItemId = call.parameters["position_item_id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val positionExtraItems = positionExtraItemsRepository.getPositionExtraItems(positionItemId)

            val positionExtraItemsDto = positionExtraItems.map { positionExtraItemDtoMapper.getDto(it) }
            call.respond(positionExtraItemsDto)
        }

        post("/position_items/{position_item_id}/position_extra") {
            val positionItemId = call.parameters["position_item_id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val formParameters = call.receiveParameters()
            val positionExtraId = formParameters["position_extra_id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val positionExtraItem = positionExtraItemsRepository.addPositionExtraItem(
                positionItemId = positionItemId,
                positionExtraId = positionExtraId,
            )

            val positionExtraItemDto = positionExtraItemDtoMapper.getDto(positionExtraItem)
            call.respond(positionExtraItemDto)
        }

        delete("/position_extra_items/{id}") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            positionExtraItemsRepository.deletePositionExtraItem(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
