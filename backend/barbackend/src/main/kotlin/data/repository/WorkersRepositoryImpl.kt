package by.cyberpunkfandom.data.repository

import by.cyberpunkfandom.data.database.suspendTransaction
import by.cyberpunkfandom.data.database.workers.WorkerEntity
import by.cyberpunkfandom.data.mappers.WorkerMapper
import by.cyberpunkfandom.domain.exceptions.ExceptionCodes
import by.cyberpunkfandom.domain.exceptions.GeneralException
import by.cyberpunkfandom.domain.models.Worker
import by.cyberpunkfandom.domain.repository.WorkersRepository

class WorkersRepositoryImpl(
    private val workerMapper: WorkerMapper,
) : WorkersRepository {

    override suspend fun getWorkers(): List<Worker> = suspendTransaction {
        WorkerEntity.all()
            .sortedBy { it.name }
            .map { workerMapper.getDomain(it) }
    }

    override suspend fun addWorker(
        name: String,
        canBeCashier: Boolean,
        canBeBartender: Boolean,
    ): Worker = suspendTransaction {
        validateRoles(canBeCashier = canBeCashier, canBeBartender = canBeBartender)

        val newEntity = WorkerEntity.new {
            this.name = name
            this.canBeCashier = canBeCashier
            this.canBeBartender = canBeBartender
        }
        workerMapper.getDomain(newEntity)
    }

    override suspend fun updateWorker(
        id: Int,
        name: String?,
        isOnLine: Boolean?,
        canBeCashier: Boolean?,
        canBeBartender: Boolean?,
    ): Worker = suspendTransaction {
        val currentWorker = WorkerEntity.findById(id) ?: throw GeneralException(ExceptionCodes.WORKER_NOT_FOUND)
        validateRoles(
            canBeCashier = canBeCashier ?: currentWorker.canBeCashier,
            canBeBartender = canBeBartender ?: currentWorker.canBeBartender,
        )

        val entity = WorkerEntity.findByIdAndUpdate(id = id) { worker ->
            name?.let { worker.name = it }
            isOnLine?.let { worker.isOnLine = it }
            canBeCashier?.let { worker.canBeCashier = it }
            canBeBartender?.let { worker.canBeBartender = it }
        }!!
        workerMapper.getDomain(entity)
    }

    override suspend fun deleteWorker(id: Int): Unit = suspendTransaction {
        WorkerEntity.findById(id)?.delete()
    }

    private fun validateRoles(
        canBeCashier: Boolean,
        canBeBartender: Boolean,
    ) {
        if (!canBeCashier && !canBeBartender) {
            throw GeneralException(ExceptionCodes.WORKER_MUST_HAVE_ROLE)
        }
    }
}
