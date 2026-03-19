package by.cyberpunkfandom.controller.mappers

import by.cyberpunkfandom.controller.dto.AdminAnalyticsDto
import by.cyberpunkfandom.controller.dto.DrinkAnalyticsDto
import by.cyberpunkfandom.controller.dto.ProductAnalyticsDto
import by.cyberpunkfandom.controller.dto.WorkerAnalyticsDto
import by.cyberpunkfandom.domain.models.AdminAnalytics

class AdminAnalyticsDtoMapper {

    fun getDto(domain: AdminAnalytics): AdminAnalyticsDto {
        return AdminAnalyticsDto(
            soldOrdersCount = domain.soldOrdersCount,
            soldItemsCount = domain.soldItemsCount,
            totalRevenue = domain.totalRevenue,
            drinks = domain.drinks.map { drink ->
                DrinkAnalyticsDto(
                    positionId = drink.positionId,
                    positionName = drink.positionName,
                    soldCount = drink.soldCount,
                    revenue = drink.revenue,
                )
            },
            products = domain.products.map { product ->
                ProductAnalyticsDto(
                    positionVariantId = product.positionVariantId,
                    positionVariantName = product.positionVariantName,
                    soldCount = product.soldCount,
                    revenue = product.revenue,
                )
            },
            workers = domain.workers.map { worker ->
                WorkerAnalyticsDto(
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
