package by.cyberpunkfandom

import by.cyberpunkfandom.controller.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    adminRouting()
    positionsRouting()
    positionVariantsRouting()
    ordersRouting()
    positionItemsRouting()
    workersRouting()
}
