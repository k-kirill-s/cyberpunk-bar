package by.cyberpunkfandom.barfrontend.data.repositories

import by.cyberpunkfandom.barfrontend.data.services.AdminSession
import by.cyberpunkfandom.barfrontend.data.services.MainService

class AdminRepository(
    private val mainService: MainService,
    private val adminSession: AdminSession,
) {
    suspend fun login(
        username: String,
        password: String,
    ) {
        mainService.loginAdmin(
            username = username,
            password = password,
        )
        adminSession.setCredentials(
            username = username,
            password = password,
        )
    }

    fun logout() {
        adminSession.clear()
    }

    fun isAuthorized(): Boolean = adminSession.isAuthorized()
}
