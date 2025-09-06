package by.cyberpunkfandom.barfrontend.data.services

import by.cyberpunkfandom.barfrontend.data.models.ErrorDto
import by.cyberpunkfandom.barfrontend.data.models.OrderDto
import by.cyberpunkfandom.barfrontend.data.models.OrderFullDto
import by.cyberpunkfandom.barfrontend.data.models.PositionDto
import by.cyberpunkfandom.barfrontend.data.models.PositionItemDto
import by.cyberpunkfandom.barfrontend.data.models.PositionVariantDto
import by.cyberpunkfandom.barfrontend.data.models.WorkerDto
import by.cyberpunkfandom.barfrontend.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.barfrontend.domain.exceptions.GeneralException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.parameters

class MainService(private val httpClient: HttpClient) {

    // ---------------------------------------------------------------------------------------------------------------
    // ORDERS
    suspend fun getActiveOrders(): List<OrderDto> {
        return httpClient.get("orders/active").bodyOrThrowGeneralError()
    }

    suspend fun getNextOrderToCollect(): OrderFullDto {
        return httpClient.get("orders/next").bodyOrThrowGeneralError()
    }

    suspend fun getInProgressOrderByWorker(workerId: Int): OrderFullDto {
        return httpClient.get("orders/by_worker/${workerId}").bodyOrThrowGeneralError()
    }

    suspend fun getOrder(id: Int): OrderFullDto {
        return httpClient.get("orders/$id").bodyOrThrowGeneralError()
    }

    suspend fun createOrder(): OrderFullDto {
        return httpClient.post("orders").bodyOrThrowGeneralError()
    }

    suspend fun formOrder(orderId: Int): OrderFullDto {
        return httpClient.post("/orders/${orderId}/form").bodyOrThrowGeneralError()
    }

    suspend fun startOrder(orderId: Int, workerId: Int): OrderFullDto {
        return httpClient.submitForm(
            url = "orders/${orderId}/start",
            formParameters = parameters {
                append("worker_id", workerId.toString())
            },
        ).bodyOrThrowGeneralError()
    }

    suspend fun finishOrder(orderId: Int): OrderFullDto {
        return httpClient.post("/orders/${orderId}/finish").bodyOrThrowGeneralError()
    }

    suspend fun giveAwayOrder(orderId: Int): OrderFullDto {
        return httpClient.post("/orders/${orderId}/give").bodyOrThrowGeneralError()
    }

    suspend fun declineOrder(orderId: Int): OrderFullDto {
        return httpClient.post("/orders/${orderId}/decline").bodyOrThrowGeneralError()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // POSITION ITEMS
    suspend fun addPositionToOrder(
        orderId: Int,
        positionId: String,
        positionVariantId: String,
    ): PositionItemDto {
        return httpClient.submitForm(
            url = "orders/${orderId}/position_items",
            formParameters = parameters {
                append("position_id", positionId)
                append("position_variant_id", positionVariantId)
            },
        ).bodyOrThrowGeneralError()
    }

    suspend fun deletePositionItem(positionItemId: Int) {
        return httpClient.delete("position_items/${positionItemId}").bodyOrThrowGeneralError()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // POSITIONS
    suspend fun getPositions(): List<PositionDto> {
        return httpClient.get("positions").bodyOrThrowGeneralError()
    }

    suspend fun getActivePositions(): List<PositionDto> {
        return httpClient.get("positions/active").bodyOrThrowGeneralError()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // POSITIONS
    suspend fun getPositionVariants(positionId: String): List<PositionVariantDto> {
        return httpClient
            .get("positions/${positionId}/position_variants")
            .bodyOrThrowGeneralError()
    }

    suspend fun setPositionVariantIsActive(
        positionVariantId: String,
        isActive: Boolean,
    ): PositionVariantDto {
        return httpClient
            .submitForm(
                url = "position_variants/${positionVariantId}",
                formParameters = parameters {
                    append("is_active", isActive.toString())
                }
            ) { method = HttpMethod.Patch }
            .bodyOrThrowGeneralError()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // WORKERS

    suspend fun getWorkers(): List<WorkerDto> {
        return httpClient.get("workers").bodyOrThrowGeneralError()
    }

    suspend fun setWorkerIsOnLine(workerId: Int, isOnLine: Boolean): WorkerDto {
        return httpClient.submitForm(
            url = "workers/${workerId}",
            formParameters = parameters {
                append("is_on_line", isOnLine.toString())
            }
        ) {
            method = HttpMethod.Patch
        }.bodyOrThrowGeneralError()
    }

    private suspend inline fun <reified T> HttpResponse.bodyOrThrowGeneralError(): T {
        return if (status == HttpStatusCode.InternalServerError) {
            val error = body<ErrorDto>()
            throw GeneralException(ExceptionCodes.valueOf(error.code))
        } else {
            body()
        }
    }
}
