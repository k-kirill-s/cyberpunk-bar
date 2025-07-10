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

    val workersRepository by inject<WorkersRepository>()

    val workerDtoMapper by inject<WorkerDtoMapper>()

    routing {
        get("workers") {
            val workersDto = workersRepository.getWorkers()
                .map { workerDtoMapper.getDto(it) }
            call.respond(workersDto)
        }

        post("workers") {
            val formParameters = call.receiveParameters()
            val name = requireNotNull(formParameters["name"])
            val workerDto = workersRepository.addWorker(name = name)
                .let { workerDtoMapper.getDto(it) }
            call.respond(workerDto)
        }

        patch("workers/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val isOnLine = formParameters["is_on_line"]?.toBoolean()
            val workerDto = workersRepository.updateWorker(
                id = id,
                name = name,
                isOnLine = isOnLine,
            )?.let { workerDtoMapper.getDto(it) }
            if (workerDto != null) {
                call.respond(workerDto)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("workers/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            workersRepository.deleteWorker(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
