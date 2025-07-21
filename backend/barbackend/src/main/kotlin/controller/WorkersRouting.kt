package by.cyberpunkfandom.controller

import by.cyberpunkfandom.controller.mappers.WorkerDtoMapper
import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException
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
            val workers = workersRepository.getWorkers()

            val workersDto = workers.map { workerDtoMapper.getDto(it) }
            call.respond(workersDto)
        }

        post("workers") {
            val formParameters = call.receiveParameters()
            val name = formParameters["name"] ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            val worker = workersRepository.addWorker(name = name)
            val workerDto = workerDtoMapper.getDto(worker)
            call.respond(workerDto)
        }

        patch("workers/{id}") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))
            val formParameters = call.receiveParameters()
            val name = formParameters["name"]
            val isOnLine = formParameters["is_on_line"]?.toBoolean()

            val worker = workersRepository.updateWorker(
                id = id,
                name = name,
                isOnLine = isOnLine,
            )

            val workerDto = workerDtoMapper.getDto(worker)
            call.respond(workerDto)
        }

        delete("workers/{id}") {
            val id = call.parameters["id"]?.toInt() ?: error(GeneralException(ExceptionCodes.MISSING_PARAMETER))

            workersRepository.deleteWorker(id)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
