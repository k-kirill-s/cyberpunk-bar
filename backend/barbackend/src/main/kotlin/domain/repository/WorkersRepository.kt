package by.cyberpunkfandom.domain.repository

import by.cyberpunkfandom.domain.models.Worker

interface WorkersRepository {

    suspend fun getWorkers(): List<Worker>

    suspend fun addWorker(name: String): Worker

    suspend fun updateWorker(
        id: Int,
        name: String?,
        isOnLine: Boolean?,
    ): Worker

    suspend fun deleteWorker(id: Int)
}
