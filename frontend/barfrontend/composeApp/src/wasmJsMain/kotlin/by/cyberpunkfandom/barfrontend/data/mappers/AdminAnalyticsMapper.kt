package by.cyberpunkfandom.barfrontend.data.mappers

import by.cyberpunkfandom.barfrontend.data.models.AdminAnalyticsDto
import by.cyberpunkfandom.barfrontend.domain.AdminAnalytics
import by.cyberpunkfandom.barfrontend.domain.DrinkAnalytics
import by.cyberpunkfandom.barfrontend.domain.ProductAnalytics
import by.cyberpunkfandom.barfrontend.domain.WorkerAnalytics

class AdminAnalyticsMapper {

    fun getDomain(dto: AdminAnalyticsDto): AdminAnalytics {
        return AdminAnalytics(
            soldOrdersCount = dto.soldOrdersCount,
            soldItemsCount = dto.soldItemsCount,
            totalRevenue = dto.totalRevenue,
            drinks = dto.drinks.map { drink ->
                DrinkAnalytics(
                    positionId = drink.positionId,
                    positionName = drink.positionName,
                    soldCount = drink.soldCount,
                    revenue = drink.revenue,
                )
            },
            products = dto.products.map { product ->
                ProductAnalytics(
                    positionVariantId = product.positionVariantId,
                    positionVariantName = product.positionVariantName,
                    soldCount = product.soldCount,
                    revenue = product.revenue,
                )
            },
            workers = dto.workers.map { worker ->
                WorkerAnalytics(
                    workerId = worker.workerId,
                    workerName = worker.workerName,
                    createdOrdersCount = worker.createdOrdersCount,
                    preparedOrdersCount = worker.preparedOrdersCount,
                    givenOrdersCount = worker.givenOrdersCount,
                    preparedDrinksCount = worker.preparedDrinksCount,
                )
            },
        )
    }
}
