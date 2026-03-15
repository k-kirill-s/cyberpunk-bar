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
import io.ktor.http.parameters

class MainService(private val httpClient: HttpClient) {

    private fun api(path: String): String = "/api/$path"

    // ---------------------------------------------------------------------------------------------------------------
    // ORDERS
    suspend fun getActiveOrders(): List<OrderDto> {
        return httpClient.get(api("orders/active")).bodyOrThrowGeneralError()
    }

    suspend fun getNextOrderToCollect(): OrderFullDto {
        return httpClient.get(api("orders/next")).bodyOrThrowGeneralError()
    }

    suspend fun getInProgressOrderByWorker(workerId: Int): OrderFullDto {
        return httpClient.get(api("orders/by_worker/${workerId}")).bodyOrThrowGeneralError()
    }

    suspend fun getOrder(id: Int): OrderFullDto {
        return httpClient.get(api("orders/$id")).bodyOrThrowGeneralError()
    }

    suspend fun createOrder(): OrderFullDto {
        return httpClient.post(api("orders")).bodyOrThrowGeneralError()
    }

    suspend fun formOrder(orderId: Int): OrderFullDto {
        return httpClient.post(api("orders/${orderId}/form")).bodyOrThrowGeneralError()
    }

    suspend fun startOrder(orderId: Int, workerId: Int): OrderFullDto {
        return httpClient.submitForm(
            url = api("orders/${orderId}/start"),
            formParameters = parameters {
                append("worker_id", workerId.toString())
            },
        ).bodyOrThrowGeneralError()
    }

    suspend fun finishOrder(orderId: Int): OrderFullDto {
        return httpClient.post(api("orders/${orderId}/finish")).bodyOrThrowGeneralError()
    }

    suspend fun giveAwayOrder(orderId: Int): OrderFullDto {
        return httpClient.post(api("orders/${orderId}/give")).bodyOrThrowGeneralError()
    }

    suspend fun declineOrder(orderId: Int): OrderFullDto {
        return httpClient.post(api("orders/${orderId}/decline")).bodyOrThrowGeneralError()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // POSITION ITEMS
    suspend fun addPositionToOrder(
        orderId: Int,
        positionId: String,
        positionVariantId: String,
    ): PositionItemDto {
        return httpClient.submitForm(
            url = api("orders/${orderId}/position_items"),
            formParameters = parameters {
                append("position_id", positionId)
                append("position_variant_id", positionVariantId)
            },
        ).bodyOrThrowGeneralError()
    }

    suspend fun deletePositionItem(positionItemId: Int) {
        return httpClient.delete(api("position_items/${positionItemId}")).bodyOrThrowGeneralError()
    }

    suspend fun setPositionItemCompleted(
        positionItemId: Int,
        isCompleted: Boolean,
    ): PositionItemDto {
        return httpClient.submitForm(
            url = api("position_items/${positionItemId}"),
            formParameters = parameters {
                append("is_completed", isCompleted.toString())
            }
        ) {
            method = HttpMethod.Patch
        }.bodyOrThrowGeneralError()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // POSITIONS
    suspend fun getPositions(): List<PositionDto> {
        return httpClient.get(api("positions")).bodyOrThrowGeneralError()
    }

    suspend fun getActivePositions(): List<PositionDto> {
        return httpClient.get(api("positions/active")).bodyOrThrowGeneralError()
    }

    suspend fun createPosition(
        id: String,
        name: String,
        description: String,
    ): PositionDto {
        return httpClient.submitForm(
            url = api("positions"),
            formParameters = parameters {
                append("id", id)
                append("name", name)
                append("description", description)
            },
        ).bodyOrThrowGeneralError()
    }

    suspend fun updatePosition(
        positionId: String,
        name: String?,
        description: String?,
    ): PositionDto {
        return httpClient.submitForm(
            url = api("positions/${positionId}"),
            formParameters = parameters {
                name?.let { append("name", it) }
                description?.let { append("description", it) }
            }
        ) {
            method = HttpMethod.Patch
        }.bodyOrThrowGeneralError()
    }

    suspend fun deletePosition(positionId: String) {
        return httpClient.delete(api("positions/${positionId}")).bodyOrThrowGeneralError()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // POSITION VARIANTS
    suspend fun getPositionVariants(positionId: String): List<PositionVariantDto> {
        return httpClient
            .get(api("positions/${positionId}/position_variants"))
            .bodyOrThrowGeneralError()
    }

    suspend fun createPositionVariant(
        positionId: String,
        id: String,
        name: String,
        price: Float,
    ): PositionVariantDto {
        return httpClient.submitForm(
            url = api("positions/${positionId}/position_variants"),
            formParameters = parameters {
                append("id", id)
                append("name", name)
                append("price", price.toString())
            }
        ).bodyOrThrowGeneralError()
    }

    suspend fun updatePositionVariant(
        positionVariantId: String,
        name: String?,
        price: Float?,
        isActive: Boolean?,
    ): PositionVariantDto {
        return httpClient
            .submitForm(
                url = api("position_variants/${positionVariantId}"),
                formParameters = parameters {
                    name?.let { append("name", it) }
                    price?.let { append("price", it.toString()) }
                    isActive?.let { append("is_active", it.toString()) }
                }
            ) { method = HttpMethod.Patch }
            .bodyOrThrowGeneralError()
    }

    suspend fun setPositionVariantIsActive(
        positionVariantId: String,
        isActive: Boolean,
    ): PositionVariantDto {
        return updatePositionVariant(
            positionVariantId = positionVariantId,
            name = null,
            price = null,
            isActive = isActive,
        )
    }

    suspend fun deletePositionVariant(positionVariantId: String) {
        return httpClient.delete(api("position_variants/${positionVariantId}")).bodyOrThrowGeneralError()
    }

    // ---------------------------------------------------------------------------------------------------------------
    // WORKERS

    suspend fun getWorkers(): List<WorkerDto> {
        return httpClient.get(api("workers")).bodyOrThrowGeneralError()
    }

    suspend fun setWorkerIsOnLine(workerId: Int, isOnLine: Boolean): WorkerDto {
        return updateWorker(
            workerId = workerId,
            name = null,
            isOnLine = isOnLine,
        )
    }

    suspend fun createWorker(name: String): WorkerDto {
        return httpClient.submitForm(
            url = api("workers"),
            formParameters = parameters {
                append("name", name)
            }
        ).bodyOrThrowGeneralError()
    }

    suspend fun updateWorker(
        workerId: Int,
        name: String?,
        isOnLine: Boolean?,
    ): WorkerDto {
        return httpClient.submitForm(
            url = api("workers/${workerId}"),
            formParameters = parameters {
                name?.let { append("name", it) }
                isOnLine?.let { append("is_on_line", it.toString()) }
            }
        ) {
            method = HttpMethod.Patch
        }.bodyOrThrowGeneralError()
    }

    suspend fun deleteWorker(workerId: Int) {
        return httpClient.delete(api("workers/${workerId}")).bodyOrThrowGeneralError()
    }

    private suspend inline fun <reified T> HttpResponse.bodyOrThrowGeneralError(): T {
        if (status.value >= 400) {
            val error = runCatching { body<ErrorDto>() }.getOrNull()
            val code = error?.code
                ?.let { runCatching { ExceptionCodes.valueOf(it) }.getOrNull() }
                ?: ExceptionCodes.UNKNOWN
            throw GeneralException(code)
        }

        return body()
    }
}
