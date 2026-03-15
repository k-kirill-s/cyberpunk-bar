package by.cyberpunkfandom.barfrontend.domain.exceptions

enum class ExceptionCodes(val message: String) {
    MISSING_PARAMETER("Ошибка запроса. Пропущен параметр"),
    ORDER_NOT_FOUND("Ошибка запроса. Заказ не найден"),
    ORDER_IN_INCOMPATIBLE_STATUS("Ошибка запроса. Неподходящий статус заказа"),
    ORDER_MUST_HAVE_ITEMS("Нельзя создать пустой заказ"),
    UNKNOWN("Неизвестная ошибка"),
}
