package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.PositionDtoMapper
import by.cyberpunkfandom.domain.repository.PositionsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@Suppress("DuplicatedCode")
fun Application.positionsRouting() {

    val positionsRepository by inject<PositionsRepository>()

    val positionDtoMapper by inject<PositionDtoMapper>()

    routing {
        get("/positions") {
            val positionsDto = positionsRepository.getPositions()
                .map { positionDtoMapper.getDto(it) }
            call.respond(positionsDto)
        }

        post("positions") {
            val formParameters = call.receiveParameters()
            val id = requireNotNull(formParameters["id"])
            val name = requireNotNull(formParameters["name"])
            val description = formParameters["description"].orEmpty()
            val price = requireNotNull(formParameters["price"]).toFloat()
            val positionDto = positionsRepository.addPosition(
                id = id,
                name = name,
                description = description,
                price = price,
            ).let { positionDtoMapper.getDto(it) }
            call.respond(positionDto)
        }

        patch("positions/{id}") {
            val id = requireNotNull(call.parameters["id"])
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val price = formParameters["price"]?.toFloat()
            val isActive = formParameters["is_active"]?.toBoolean()
            val positionDto = positionsRepository.updatePosition(
                id = id,
                name = name,
                price = price,
                isActive = isActive,
            )?.let { positionDtoMapper.getDto(it) }
            if (positionDto != null) {
                call.respond(positionDto)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("positions/{id}") {
            val id = requireNotNull(call.parameters["id"])
            positionsRepository.deletePosition(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
