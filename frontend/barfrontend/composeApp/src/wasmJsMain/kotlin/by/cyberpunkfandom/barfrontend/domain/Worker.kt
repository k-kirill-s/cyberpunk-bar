package by.cyberpunkfandom.barfrontend.domain

data class Worker(
    val id: Int,
    val name: String,
    val isOnLine: Boolean,
    val canBeCashier: Boolean,
    val canBeBartender: Boolean,
)
