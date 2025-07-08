package by.cyberpunkfandom.domain.models

class PositionItem(
    val id: Int,
    val position: Position,
    val extraItems: List<PositionExtraItem>,
) {

    val price: Float = position.price + extraItems.sumOf { it.price.toDouble() }.toFloat()
}
