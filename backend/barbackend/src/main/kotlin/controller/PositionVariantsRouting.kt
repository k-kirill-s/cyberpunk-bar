package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.PositionVariantDtoMapper
import by.cyberpunkfandom.domain.repository.PositionVariantsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@Suppress("DuplicatedCode")
fun Application.positionVariantsRouting() {
    val adminCredentials = loadAdminCredentials()

    val positionVariantsRepository by inject<PositionVariantsRepository>()

    val positionVariantDtoMapper by inject<PositionVariantDtoMapper>()

    routing {
        get("/position_variants") {
            val positionVariants = positionVariantsRepository.getPositionVariants()
            val positionVariantsDto = positionVariants.map { positionVariantDtoMapper.getDto(it) }

            call.respond(positionVariantsDto)
        }

        get("/positions/{position_id}/position_variants") {
            val positionId = call.parameters["position_id"].requiredParameter()

            val positionVariants = positionVariantsRepository.getPositionVariantsByPosition(positionId)
            val positionVariantsDto = positionVariants.map { positionVariantDtoMapper.getDto(it) }

            call.respond(positionVariantsDto)
        }

        post("/position_variants") {
            call.requireAdminAccess(adminCredentials)
            val formParameters = call.receiveParameters()
            val name = formParameters["name"].requiredParameter()
            val price = formParameters["price"].requiredFloatParameter()

            val positionVariant = positionVariantsRepository.addPositionVariant(
                name = name,
                price = price,
            )

            val positionVariantDto = positionVariantDtoMapper.getDto(positionVariant)
            call.respond(positionVariantDto)
        }

        patch("/position_variants/{id}") {
            call.requireAdminAccess(adminCredentials)
            val id = call.parameters["id"].requiredParameter()
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val price = formParameters["price"]?.toFloatOrNull()
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
            call.requireAdminAccess(adminCredentials)
            val id = call.parameters["id"].requiredParameter()

            positionVariantsRepository.deletePositionVariant(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
