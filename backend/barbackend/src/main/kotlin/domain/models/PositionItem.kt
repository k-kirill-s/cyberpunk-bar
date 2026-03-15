package by.cyberpunkfandom.domain.models

class PositionItem(
    val id: Int,
    val position: Position,
    val positionVariant: PositionVariant,
    val isCompleted: Boolean,
) {

    val price: Float = positionVariant.price
}
