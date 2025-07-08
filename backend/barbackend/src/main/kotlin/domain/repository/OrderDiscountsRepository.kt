package by.cyberpunkfandom.domain.repository

interface OrderDiscountsRepository {

    suspend fun addDiscountToOrder(orderId: Int, discountId: String)

    suspend fun removeDiscountFromOrder(orderId: Int, discountId: String)
}
