package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.PositionDtoMapper
import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException
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
            val positions = positionsRepository.getPositions()

            val positionsDto = positions.map { positionDtoMapper.getDto(it) }
            call.respond(positionsDto)
        }

        post("positions") {
            val formParameters = call.receiveParameters()
            val id = formParameters["id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val name = formParameters["name"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val description = formParameters["description"].orEmpty()
            val price = formParameters["price"]?.toFloat() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val position = positionsRepository.addPosition(
                id = id,
                name = name,
                description = description,
                price = price,
            )

            val positionDto = positionDtoMapper.getDto(position)
            call.respond(positionDto)
        }

        patch("positions/{id}") {
            val id = call.parameters["id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val description = formParameters["description"]
            val price = formParameters["price"]?.toFloat()
            val isActive = formParameters["is_active"]?.toBoolean()

            val position = positionsRepository.updatePosition(
                id = id,
                name = name,
                description = description,
                price = price,
                isActive = isActive,
            )

            val positionDto = positionDtoMapper.getDto(position)
            call.respond(positionDto)
        }

        delete("positions/{id}") {
            val id = call.parameters["id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            positionsRepository.deletePosition(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
