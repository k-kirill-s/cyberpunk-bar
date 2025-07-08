package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.PositionExtraDtoMapper
import by.cyberpunkfandom.domain.repository.PositionExtraRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@Suppress("DuplicatedCode")
fun Application.positionExtraRouting() {

    val positionExtraRepository by inject<PositionExtraRepository>()

    val positionExtraDtoMapper by inject<PositionExtraDtoMapper>()

    routing {
        get("/position_extra") {
            val positionExtraDto = positionExtraRepository.getPositionExtra()
                .map { positionExtraDtoMapper.getDto(it) }
            call.respond(positionExtraDto)
        }

        post("position_extra") {
            val formParameters = call.receiveParameters()
            val id = requireNotNull(formParameters["id"])
            val name = requireNotNull(formParameters["name"])
            val price = requireNotNull(formParameters["price"]).toFloat()
            val positionExtraDto = positionExtraRepository.addPositionExtra(id, name, price)
                .let { positionExtraDtoMapper.getDto(it) }
            call.respond(positionExtraDto)
        }

        patch("position_extra/{id}") {
            val id = requireNotNull(call.parameters["id"])
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val price = formParameters["price"]?.toFloat()
            val isActive = formParameters["is_active"]?.toBoolean()
            val positionExtraDto = positionExtraRepository.updatePositionExtra(
                id = id,
                name = name,
                price = price,
                isActive = isActive,
            )?.let { positionExtraDtoMapper.getDto(it) }
            if (positionExtraDto != null) {
                call.respond(positionExtraDto)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("position_extra/{id}") {
            val id = requireNotNull(call.parameters["id"])
            positionExtraRepository.deletePositionExtra(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
