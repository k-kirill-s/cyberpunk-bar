package by.cyberpunkfandom.barfrontend.data.services

class AdminSession {
    private var credentials: AdminCredentials? = null

    fun setCredentials(
        username: String,
        password: String,
    ) {
        credentials = AdminCredentials(
            username = username,
            password = password,
        )
    }

    fun clear() {
        credentials = null
    }

    fun currentCredentials(): AdminCredentials? = credentials

    fun isAuthorized(): Boolean = credentials != null
}

data class AdminCredentials(
    val username: String,
    val password: String,
)
