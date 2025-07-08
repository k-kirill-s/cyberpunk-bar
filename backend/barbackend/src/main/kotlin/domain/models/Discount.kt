package by.cyberpunkfandom.domain.models

/**
 * @property value Discount coefficient in terms of [0, 1], where 0.2 ~ 20%
 */
class Discount(
    val id: String,
    val name: String,
    val value: Float,
)
