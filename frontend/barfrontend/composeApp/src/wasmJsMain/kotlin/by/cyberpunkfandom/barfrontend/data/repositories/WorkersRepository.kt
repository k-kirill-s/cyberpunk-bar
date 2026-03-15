package by.cyberpunkfandom.barfrontend.data.repositories

import by.cyberpunkfandom.barfrontend.data.mappers.WorkerMapper
import by.cyberpunkfandom.barfrontend.data.services.MainService
import by.cyberpunkfandom.barfrontend.domain.Worker

class WorkersRepository(
    private val mainService: MainService,
    private val workerMapper: WorkerMapper,
) {

    suspend fun getWorkers(): List<Worker> {
        val dtoList = mainService.getWorkers()
        return dtoList.map { workerMapper.getDomain(it) }
    }

    suspend fun setWorkerIsOnLine(workerId: Int, isOnLine: Boolean): Worker {
        val dto = mainService.setWorkerIsOnLine(workerId, isOnLine)
        return workerMapper.getDomain(dto)
    }

    suspend fun createWorker(name: String): Worker {
        val dto = mainService.createWorker(name)
        return workerMapper.getDomain(dto)
    }

    suspend fun updateWorker(
        workerId: Int,
        name: String?,
        isOnLine: Boolean?,
    ): Worker {
        val dto = mainService.updateWorker(
            workerId = workerId,
            name = name,
            isOnLine = isOnLine,
        )
        return workerMapper.getDomain(dto)
    }

    suspend fun deleteWorker(workerId: Int) {
        mainService.deleteWorker(workerId)
    }
}
