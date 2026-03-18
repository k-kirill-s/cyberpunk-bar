package by.cyberpunkfandom.barfrontend.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AdminAnalyticsDto(
    val soldOrdersCount: Int,
    val soldItemsCount: Int,
    val totalRevenue: Float,
    val drinks: List<DrinkAnalyticsDto>,
    val products: List<ProductAnalyticsDto>,
    val workers: List<WorkerAnalyticsDto>,
)

@Serializable
data class DrinkAnalyticsDto(
    val positionId: String,
    val positionName: String,
    val soldCount: Int,
    val revenue: Float,
)

@Serializable
data class ProductAnalyticsDto(
    val positionVariantId: String,
    val positionVariantName: String,
    val soldCount: Int,
    val revenue: Float,
)

@Serializable
data class WorkerAnalyticsDto(
    val workerId: Int,
    val workerName: String,
    val createdOrdersCount: Int,
    val preparedOrdersCount: Int,
    val givenOrdersCount: Int,
    val preparedDrinksCount: Int,
)
