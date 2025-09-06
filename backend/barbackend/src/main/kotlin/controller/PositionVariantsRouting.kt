package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.PositionVariantDtoMapper
import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException
import by.cyberpunkfandom.domain.repository.PositionVariantsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@Suppress("DuplicatedCode")
fun Application.positionVariantsRouting() {

    val positionVariantsRepository by inject<PositionVariantsRepository>()

    val positionVariantDtoMapper by inject<PositionVariantDtoMapper>()

    routing {
        get("/positions/{position_id}/position_variants") {
            val positionId = call.parameters["position_id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val positionVariants = positionVariantsRepository.getPositionVariants(positionId)
            val positionVariantsDto = positionVariants.map { positionVariantDtoMapper.getDto(it) }

            call.respond(positionVariantsDto)
        }

        post("/positions/{position_id}/position_variants") {
            val positionId = call.parameters["position_id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val formParameters = call.receiveParameters()
            val id = formParameters["id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val name = formParameters["name"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val price = formParameters["price"]?.toFloat() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val positionVariant = positionVariantsRepository.addPositionVariant(
                positionId = positionId,
                id = id,
                name = name,
                price = price,
            )

            val positionVariantDto = positionVariantDtoMapper.getDto(positionVariant)
            call.respond(positionVariantDto)
        }

        patch("/position_variants/{id}") {
            val id = call.parameters["id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val price = formParameters["price"]?.toFloat()
            val isActive = formParameters["is_active"]?.toBoolean()

            val positionVariant = positionVariantsRepository.updatePositionVariant(
                id = id,
                name = name,
                price = price,
                isActive = isActive,
            )

            val positionVariantDto = positionVariantDtoMapper.getDto(positionVariant)
            call.respond(positionVariantDto)
        }

        delete("/position_variants/{id}") {
            val id = call.parameters["id"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            positionVariantsRepository.deletePositionVariant(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
