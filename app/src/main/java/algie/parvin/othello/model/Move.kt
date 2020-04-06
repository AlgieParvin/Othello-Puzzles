package algie.parvin.othello.model

class Move (
    val move: IntArray,
    val responseMove: IntArray?,
    var next: MutableList<Move>? = null,
    var previous: Move? = null
)