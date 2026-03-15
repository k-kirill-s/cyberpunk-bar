package by.cyberpunkfandom.data.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: suspend Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun Transaction.acquireTransactionLock(key: Long) {
    exec("SELECT pg_advisory_xact_lock($key)") { }
}
