package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.database.workers.WorkerEntity
import by.cyberpunkfandom.data.mappers.WorkerMapper
import by.cyberpunkfandom.domain.models.Worker
import by.cyberpunkfandom.domain.repository.WorkersRepository

class WorkersRepositoryImpl(
    private val workerMapper: WorkerMapper,
) : WorkersRepository {

    override suspend fun getWorkers(): List<Worker> = suspendTransaction {
        WorkerEntity.all()
            .map { workerMapper.getDomain(it) }
    }

    override suspend fun addWorker(name: String): Worker = suspendTransaction {
        val newEntity = WorkerEntity.new {
            this.name = name
        }
        workerMapper.getDomain(newEntity)
    }

    override suspend fun updateWorker(id: Int, name: String?, isOnLine: Boolean?): Worker? = suspendTransaction {
        val entity = WorkerEntity.findByIdAndUpdate(id = id) { worker ->
            name?.let { worker.name = it }
            isOnLine?.let { worker.isOnLine = it }
        }
        entity?.let { workerMapper.getDomain(it) }
    }

    override suspend fun deleteWorker(id: Int): Unit = suspendTransaction {
        WorkerEntity.findById(id)?.delete()
    }
}
