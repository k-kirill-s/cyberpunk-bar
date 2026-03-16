package by.cyberpunkfandom.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.adminRouting() {
    val adminCredentials = loadAdminCredentials()

    routing {
        post("/admin/login") {
            val formParameters = call.receiveParameters()

            ensureAdminCredentials(
                adminCredentials = adminCredentials,
                username = formParameters["username"],
                password = formParameters["password"],
            )

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
