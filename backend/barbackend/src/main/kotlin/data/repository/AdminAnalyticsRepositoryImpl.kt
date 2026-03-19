package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.orders.OrderFullEntity
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.database.workers.WorkerEntity
import by.cyberpunkfandom.domain.models.AdminAnalytics
import by.cyberpunkfandom.domain.models.DrinkAnalytics
import by.cyberpunkfandom.domain.models.OrderStatus
import by.cyberpunkfandom.domain.models.ProductAnalytics
import by.cyberpunkfandom.domain.models.WorkerAnalytics
import by.cyberpunkfandom.domain.repository.AdminAnalyticsRepository

class AdminAnalyticsRepositoryImpl : AdminAnalyticsRepository {

    override suspend fun getAnalytics(): AdminAnalytics = suspendTransaction {
        val orders = OrderFullEntity.all().toList()
        val workers = WorkerEntity.all().toList()

        val soldOrders = orders.filter { it.status() == OrderStatus.GIVEN }
        val preparedOrders = orders.filter { it.status() in setOf(OrderStatus.FINISHED, OrderStatus.GIVEN) }
        val createdOrders = orders.filter { it.status() != OrderStatus.CREATED }

        val soldItems = soldOrders.flatMap { order -> order.positionItems.toList() }
        val soldItemsCount = soldItems.size
        val totalRevenue = soldItems.sumOf { it.positionVariant.price.toDouble() }.toFloat()

        val drinks = soldItems
            .groupBy { it.position }
            .map { (position, items) ->
                DrinkAnalytics(
                    positionId = position.id.value,
                    positionName = position.name,
                    soldCount = items.size,
                    revenue = items.sumOf { it.positionVariant.price.toDouble() }.toFloat(),
                )
            }
            .sortedWith(compareByDescending<DrinkAnalytics> { it.soldCount }.thenBy { it.positionName.lowercase() })

        val products = soldItems
            .groupBy { it.positionVariant }
            .map { (positionVariant, items) ->
                ProductAnalytics(
                    positionVariantId = positionVariant.id.value,
                    positionVariantName = positionVariant.name,
                    soldCount = items.size,
                    revenue = items.sumOf { it.positionVariant.price.toDouble() }.toFloat(),
                )
            }
            .sortedWith(compareByDescending<ProductAnalytics> { it.soldCount }.thenBy { it.positionVariantName.lowercase() })

        val workerAnalytics = workers
            .map { worker ->
                val workerId = worker.id.value
                val preparedByWorker = preparedOrders.filter { it.completedBy?.id?.value == workerId }

                WorkerAnalytics(
                    workerId = workerId,
                    workerName = worker.name,
                    createdOrdersCount = createdOrders.count { it.createdBy?.id?.value == workerId },
                    preparedOrdersCount = preparedByWorker.size,
                    givenOrdersCount = soldOrders.count { it.givenBy?.id?.value == workerId },
                    preparedDrinksCount = preparedByWorker.sumOf { it.positionItems.count().toInt() },
                )
            }
            .sortedWith(
                compareByDescending<WorkerAnalytics> {
                    it.givenOrdersCount + it.preparedOrdersCount + it.createdOrdersCount
                }.thenBy { it.workerName.lowercase() }
            )

        AdminAnalytics(
            soldOrdersCount = soldOrders.size,
            soldItemsCount = soldItemsCount,
            totalRevenue = totalRevenue,
            drinks = drinks,
            products = products,
            workers = workerAnalytics,
        )
    }

    private fun OrderFullEntity.status(): OrderStatus {
        return lastStatusChangedEvent?.status
            ?.let(OrderStatus::valueOf)
            ?: OrderStatus.CREATED
    }
}
