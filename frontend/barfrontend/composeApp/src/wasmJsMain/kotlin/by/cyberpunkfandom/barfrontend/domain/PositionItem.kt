package by.cyberpunkfandom.barfrontend.domain

data class PositionItem(
    val id: Int,
    val position: Position,
    val extraItems: List<PositionExtraItem>,
    val price: Float,
)
