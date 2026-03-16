package by.cyberpunkfandom.controller

import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException
import io.ktor.server.application.*

private const val adminUsernameHeader = "X-Admin-Username"
private const val adminPasswordHeader = "X-Admin-Password"

internal data class AdminCredentials(
    val username: String,
    val password: String,
)

internal fun loadAdminCredentials(): AdminCredentials {
    return AdminCredentials(
        username = System.getenv("ADMIN_USERNAME") ?: "cyberadm",
        password = System.getenv("ADMIN_PASSWORD") ?: "cyberadm",
    )
}

internal fun ApplicationCall.requireAdminAccess(adminCredentials: AdminCredentials) {
    val username = request.headers[adminUsernameHeader]
    val password = request.headers[adminPasswordHeader]
    ensureAdminCredentials(
        adminCredentials = adminCredentials,
        username = username,
        password = password,
    )
}

internal fun ensureAdminCredentials(
    adminCredentials: AdminCredentials,
    username: String?,
    password: String?,
) {
    if (username != adminCredentials.username || password != adminCredentials.password) {
        throw GeneralException(ExceptionCodes.ADMIN_AUTH_FAILED)
    }
}
