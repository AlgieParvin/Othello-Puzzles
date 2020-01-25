package algie.parvin.othello.model

class Position {
    val whiteChips: Array<IntArray>
    val blackChips: Array<IntArray>

    constructor(white: Array<IntArray>, black: Array<IntArray>) {
        whiteChips = white
        blackChips = black
    }
}