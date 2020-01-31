package algie.parvin.othello.model

class Position {
    val whiteChips: List<IntArray>
    val blackChips: List<IntArray>

    constructor(white:  List<IntArray>, black: List<IntArray>) {
        whiteChips = white
        blackChips = black
    }
}