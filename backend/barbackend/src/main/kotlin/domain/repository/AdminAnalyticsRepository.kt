package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.AdminAnalytics

interface AdminAnalyticsRepository {

    suspend fun getAnalytics(): AdminAnalytics
}
