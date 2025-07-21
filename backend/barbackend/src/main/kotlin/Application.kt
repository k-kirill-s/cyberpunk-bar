package by.cyberpunkfandom

import by.cyberpunkfandom.controller.di.controllerKoinModule
import by.cyberpunkfandom.controller.dto.ErrorDto
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
        anyHost()
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            if (cause is GeneralException) {
                call.respond(HttpStatusCode.InternalServerError, ErrorDto(cause.code.name))
            } else {
                call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            }
        }
    }

    configureDatabases()

    configureRouting()
}
