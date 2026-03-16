package by.cyberpunkfandom

import by.cyberpunkfandom.controller.di.controllerKoinModule
import by.cyberpunkfandom.controller.dto.ErrorDto
import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.data.di.dataKoinModule
import by.cyberpunkfandom.domain.exceptions.GeneralException
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(controllerKoinModule, dataKoinModule)
    }

    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("X-Admin-Username")
        allowHeader("X-Admin-Password")
        anyHost()
    }

    install(StatusPages) {
        exception<GeneralException> { call, cause ->
            val statusCode = when (cause.code) {
                ExceptionCodes.MISSING_PARAMETER -> HttpStatusCode.BadRequest
                ExceptionCodes.ADMIN_AUTH_FAILED -> HttpStatusCode.Unauthorized
                ExceptionCodes.ORDER_NOT_FOUND -> HttpStatusCode.NotFound
                ExceptionCodes.ORDER_IN_INCOMPATIBLE_STATUS -> HttpStatusCode.Conflict
                ExceptionCodes.ORDER_MUST_HAVE_ITEMS -> HttpStatusCode.BadRequest
                ExceptionCodes.UNKNOWN -> HttpStatusCode.InternalServerError
            }

            call.respond(statusCode, ErrorDto(cause.code.name))
        }

        exception<Throwable> { call, _ ->
            call.respond(HttpStatusCode.InternalServerError, ErrorDto(ExceptionCodes.UNKNOWN.name))
        }
    }

    configureDatabases()

    configureRouting()
}
