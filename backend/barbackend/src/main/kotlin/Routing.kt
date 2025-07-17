package by.cyberpunkfandom

import by.cyberpunkfandom.controller.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    positionsRouting()
    positionExtraRouting()
    ordersRouting()
    positionItemsRouting()
    positionExtraItemsRouting()
    workersRouting()
}
