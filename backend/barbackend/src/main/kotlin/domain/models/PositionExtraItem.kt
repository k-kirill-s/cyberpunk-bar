package by.cyberpunkfandom.domain.models

class PositionExtraItem(
    val id: Int,
    val positionExtra: PositionExtra,
) {

    val price: Float = positionExtra.price
}
