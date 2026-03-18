package by.cyberpunkfandom.domain.models

class AdminAnalytics(
    val soldOrdersCount: Int,
    val soldItemsCount: Int,
    val totalRevenue: Float,
    val drinks: List<DrinkAnalytics>,
    val products: List<ProductAnalytics>,
    val workers: List<WorkerAnalytics>,
)

class DrinkAnalytics(
    val positionId: String,
    val positionName: String,
    val soldCount: Int,
    val revenue: Float,
)

class ProductAnalytics(
    val positionVariantId: String,
    val positionVariantName: String,
    val soldCount: Int,
    val revenue: Float,
)

class WorkerAnalytics(
    val workerId: Int,
    val workerName: String,
    val createdOrdersCount: Int,
    val preparedOrdersCount: Int,
    val givenOrdersCount: Int,
    val preparedDrinksCount: Int,
)
