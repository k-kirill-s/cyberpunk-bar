package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.AdminAnalyticsDtoMapper
import by.cyberpunkfandom.domain.repository.AdminAnalyticsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.adminRouting() {
    val adminCredentials = loadAdminCredentials()
    val adminAnalyticsRepository by inject<AdminAnalyticsRepository>()
    val adminAnalyticsDtoMapper by inject<AdminAnalyticsDtoMapper>()

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

        get("/admin/analytics") {
            call.requireAdminAccess(adminCredentials)

            val analytics = adminAnalyticsRepository.getAnalytics()
            val dto = adminAnalyticsDtoMapper.getDto(analytics)
            call.respond(dto)
        }
    }
}
