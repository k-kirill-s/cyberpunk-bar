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
    val adminCredentials = loadAdminCredentials()

    val positionsRepository by inject<PositionsRepository>()

    val positionDtoMapper by inject<PositionDtoMapper>()

    routing {
        get("/positions") {
            val positions = positionsRepository.getPositions()

            val positionsDto = positions.map { positionDtoMapper.getDto(it) }
            call.respond(positionsDto)
        }

        get("/positions/active") {
            val positions = positionsRepository.getActivePositions()

            val positionsDto = positions.map { positionDtoMapper.getDto(it) }
            call.respond(positionsDto)
        }

        post("positions") {
            call.requireAdminAccess(adminCredentials)
            val formParameters = call.receiveParameters()
            val name = formParameters["name"].requiredParameter()
            val description = formParameters["description"].orEmpty()
            val positionVariantIds = formParameters.getAll("position_variant_id").orEmpty()

            val position = positionsRepository.addPosition(
                name = name,
                description = description,
                positionVariantIds = positionVariantIds,
            )

            val positionDto = positionDtoMapper.getDto(position)
            call.respond(positionDto)
        }

        patch("positions/{id}") {
            call.requireAdminAccess(adminCredentials)
            val id = call.parameters["id"].requiredParameter()
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val description = formParameters["description"]
            val positionVariantIds = formParameters.getAll("position_variant_id")

            val position = positionsRepository.updatePosition(
                id = id,
                name = name,
                description = description,
                positionVariantIds = positionVariantIds,
            )

            val positionDto = positionDtoMapper.getDto(position)
            call.respond(positionDto)
        }

        delete("positions/{id}") {
            call.requireAdminAccess(adminCredentials)
            val id = call.parameters["id"].requiredParameter()

            positionsRepository.deletePosition(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
