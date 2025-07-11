package by.cyberpunkfandom.barfrontend.data.services

import by.cyberpunkfandom.barfrontend.data.models.OrderDto
import by.cyberpunkfandom.barfrontend.data.models.OrderFullDto
import by.cyberpunkfandom.barfrontend.data.models.PositionDto
import by.cyberpunkfandom.barfrontend.data.models.PositionExtraDto
import by.cyberpunkfandom.barfrontend.data.models.PositionExtraItemDto
import by.cyberpunkfandom.barfrontend.data.models.PositionItemDto
import by.cyberpunkfandom.barfrontend.data.models.WorkerDto
import by.cyberpunkfandom.barfrontend.domain.exceptions.OrderAlreadyStartedException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.parameters

class MainService(private val httpClient: HttpClient) {

    // ---------------------------------------------------------------------------------------------------------------
    // ORDERS
    suspend fun getActiveOrders(): List<OrderDto> {
        return httpClient.get("orders/active").body()
    }

    suspend fun getNextOrderToCollect(): OrderFullDto? {
        return httpClient.get("orders/next")
            .body<List<OrderFullDto>>()
            .firstOrNull()
    }

    suspend fun getInProgressOrderByWorker(workerId: Int): OrderFullDto? {
        return httpClient.get("orders/by_worker/${workerId}")
            .body<List<OrderFullDto>>()
            .firstOrNull()
    }

    suspend fun getOrder(id: Int): OrderFullDto {
        return httpClient.get("orders/$id").body()
    }

    suspend fun createOrder(): OrderFullDto {
        return httpClient.post("orders").body()
    }

    suspend fun formOrder(orderId: Int): OrderFullDto {
        return httpClient.post("/orders/${orderId}/form").body()
    }

    suspend fun startOrder(orderId: Int, workerId: Int): OrderFullDto {
        val response = httpClient.submitForm(
            url = "orders/${orderId}/start",
            formParameters = parameters {
                append("worker_id", workerId.toString())
            },
        )
        return when (response.status) {
            HttpStatusCode.Conflict -> throw OrderAlreadyStartedException()
            else -> response.body()
        }
    }

    suspend fun deleteOrder(orderId: Int) {
        return httpClient.delete("/orders/${orderId}").body()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // POSITION ITEMS
    suspend fun addPositionToOrder(orderId: Int, positionId: String): PositionItemDto {
        return httpClient.submitForm(
            url = "orders/${orderId}/position_items",
            formParameters = parameters {
                append("position_id", positionId)
            },
        ).body()
    }

    suspend fun deletePositionItem(positionItemId: Int) {
        return httpClient.delete("position_items/${positionItemId}").body()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // POSITION EXTRA ITEMS
    suspend fun addPositionExtraToPositionItem(positionItemId: Int, positionExtraId: String): PositionExtraItemDto {
        return httpClient.submitForm(
            url = "position_items/${positionItemId}/position_extra",
            formParameters = parameters {
                append("position_extra_id", positionExtraId)
            },
        ).body()
    }

    suspend fun deletePositionExtraItem(positionExtraItemId: Int) {
        return httpClient.delete("position_extra_items/${positionExtraItemId}").body()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // POSITIONS
    suspend fun getPositions(): List<PositionDto> {
        return httpClient.get("positions").body()
    }

    suspend fun setPositionIsActive(positionId: String, isActive: Boolean): PositionDto {
        return httpClient.submitForm(
            url = "positions/${positionId}",
            formParameters = parameters {
                append("is_active", isActive.toString())
            }
        ) {
            method = HttpMethod.Patch
        }.body()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // POSITION EXTRA

    suspend fun getPositionExtra(): List<PositionExtraDto> {
        return httpClient.get("position_extra").body()
    }

    suspend fun setPositionExtraIsActive(positionExtraId: String, isActive: Boolean): PositionExtraDto {
        return httpClient.submitForm(
            url = "position_extra/${positionExtraId}",
            formParameters = parameters {
                append("is_active", isActive.toString())
            }
        ) {
            method = HttpMethod.Patch
        }.body()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // WORKERS

    suspend fun getWorkers(): List<WorkerDto> {
        return httpClient.get("workers").body()
    }

    suspend fun setWorkerIsOnLine(workerId: Int, isOnLine: Boolean): WorkerDto {
        return httpClient.submitForm(
            url = "workers/${workerId}",
            formParameters = parameters {
                append("is_on_line", isOnLine.toString())
            }
        ) {
            method = HttpMethod.Patch
        }.body()
    }
}
