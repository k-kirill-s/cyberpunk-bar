package by.cyberpunkfandom.barfrontend.data.services

import by.cyberpunkfandom.barfrontend.data.models.OrderFullDto
import by.cyberpunkfandom.barfrontend.data.models.PositionDto
import by.cyberpunkfandom.barfrontend.data.models.PositionExtraDto
import by.cyberpunkfandom.barfrontend.data.models.PositionExtraItemDto
import by.cyberpunkfandom.barfrontend.data.models.PositionItemDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.parameters

class MainService(private val httpClient: HttpClient) {

    // ---------------------------------------------------------------------------------------------------------------
    // ORDERS
    suspend fun getOrder(id: Int): OrderFullDto {
        return httpClient.get("orders/$id").body()
    }

    suspend fun createOrder(): OrderFullDto {
        return httpClient.post("orders").body()
    }

    suspend fun formOrder(orderId: Int): OrderFullDto {
        return httpClient.post("/orders/${orderId}/form").body()
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

    // ---------------------------------------------------------------------------------------------------------------
    // POSITION EXTRA

    suspend fun getPositionExtra(): List<PositionExtraDto> {
        return httpClient.get("position_extra").body()
    }
}
