package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.orderdiscounts.OrderDiscountsTable
import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.domain.repository.OrderDiscountsRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore

class OrderDiscountsRepositoryImpl : OrderDiscountsRepository {

    override suspend fun addDiscountToOrder(orderId: Int, discountId: String): Unit = suspendTransaction {
        OrderDiscountsTable.insertIgnore {
            it[order] = orderId
            it[discount] = discountId
        }
    }

    override suspend fun removeDiscountFromOrder(orderId: Int, discountId: String): Unit = suspendTransaction {
        OrderDiscountsTable.deleteWhere {
            (order eq orderId) and (discount eq discountId)
        }
    }
}
