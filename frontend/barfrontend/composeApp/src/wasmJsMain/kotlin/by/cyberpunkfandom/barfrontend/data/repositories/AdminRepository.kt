package by.cyberpunkfandom.barfrontend.data.repositories

import by.cyberpunkfandom.barfrontend.data.mappers.AdminAnalyticsMapper
import by.cyberpunkfandom.barfrontend.data.services.AdminSession
import by.cyberpunkfandom.barfrontend.data.services.MainService
import by.cyberpunkfandom.barfrontend.domain.AdminAnalytics

class AdminRepository(
    private val mainService: MainService,
    private val adminSession: AdminSession,
    private val adminAnalyticsMapper: AdminAnalyticsMapper,
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

    suspend fun getAnalytics(): AdminAnalytics {
        val dto = mainService.getAdminAnalytics()
        return adminAnalyticsMapper.getDomain(dto)
    }
}
