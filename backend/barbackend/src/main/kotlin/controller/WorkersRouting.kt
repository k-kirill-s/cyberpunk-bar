package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.WorkerDtoMapper
import by.cyberpunkfandom.domain.repository.WorkersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@Suppress("DuplicatedCode")
fun Application.workersRouting() {
    val adminCredentials = loadAdminCredentials()

    val workersRepository by inject<WorkersRepository>()

    val workerDtoMapper by inject<WorkerDtoMapper>()

    routing {
        get("workers") {
            val workers = workersRepository.getWorkers()

            val workersDto = workers.map { workerDtoMapper.getDto(it) }
            call.respond(workersDto)
        }

        post("workers") {
            call.requireAdminAccess(adminCredentials)
            val formParameters = call.receiveParameters()
            val name = formParameters["name"].requiredParameter()
            val canBeCashier = formParameters["can_be_cashier"]?.toBoolean() ?: true
            val canBeBartender = formParameters["can_be_bartender"]?.toBoolean() ?: true

            val worker = workersRepository.addWorker(
                name = name,
                canBeCashier = canBeCashier,
                canBeBartender = canBeBartender,
            )
            val workerDto = workerDtoMapper.getDto(worker)
            call.respond(workerDto)
        }

        patch("workers/{id}") {
            val id = call.parameters["id"].requiredIntParameter()
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val isOnLine = formParameters["is_on_line"]?.toBoolean()
            val canBeCashier = formParameters["can_be_cashier"]?.toBoolean()
            val canBeBartender = formParameters["can_be_bartender"]?.toBoolean()

            if (name != null || canBeCashier != null || canBeBartender != null) {
                call.requireAdminAccess(adminCredentials)
            }

            val worker = workersRepository.updateWorker(
                id = id,
                name = name,
                isOnLine = isOnLine,
                canBeCashier = canBeCashier,
                canBeBartender = canBeBartender,
            )

            val workerDto = workerDtoMapper.getDto(worker)
            call.respond(workerDto)
        }

        delete("workers/{id}") {
            call.requireAdminAccess(adminCredentials)
            val id = call.parameters["id"].requiredIntParameter()

            workersRepository.deleteWorker(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
