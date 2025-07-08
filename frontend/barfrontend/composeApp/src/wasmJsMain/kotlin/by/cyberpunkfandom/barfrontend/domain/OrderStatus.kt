package by.cyberpunkfandom.barfrontend.domain

enum class OrderStatus(val isActive: Boolean) {
    CREATED(false),
    FORMED(true),
    STARTED(true),
    FINISHED(true),
    GIVEN(false),
    DECLINED(false),
}
