package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.PositionExtraDtoMapper
import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException
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
            val positionExtra = positionExtraRepository.getPositionExtra()

            val positionExtraDto = positionExtra.map { positionExtraDtoMapper.getDto(it) }
            call.respond(positionExtraDto)
        }

        post("position_extra") {
            val formParameters = call.receiveParameters()
            val id = formParameters["id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val name = formParameters["name"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val price = formParameters["price"]?.toFloat() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val positionExtra = positionExtraRepository.addPositionExtra(id, name, price)

            val positionExtraDto = positionExtraDtoMapper.getDto(positionExtra)
            call.respond(positionExtraDto)
        }

        patch("position_extra/{id}") {
            val id = call.parameters["id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val price = formParameters["price"]?.toFloat()
            val isActive = formParameters["is_active"]?.toBoolean()

            val positionExtra = positionExtraRepository.updatePositionExtra(
                id = id,
                name = name,
                price = price,
                isActive = isActive,
            )

            val positionExtraDto = positionExtraDtoMapper.getDto(positionExtra)
            call.respond(positionExtraDto)
        }

        delete("position_extra/{id}") {
            val id = call.parameters["id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            positionExtraRepository.deletePositionExtra(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
