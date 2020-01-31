package algie.parvin.othello.presenter

import algie.parvin.othello.MainActivity
import algie.parvin.othello.model.Game
import algie.parvin.othello.model.Position


class Presenter {

    private lateinit var context: MainActivity
    private val game: Game

    private fun isSquareFree(square: Int): Boolean {
        return !(game.position[square / game.boardSize][square % game.boardSize] in "WB")
    }

    fun getBoardSize() : Int {
        return game.boardSize
    }

    fun calculateSquareSize(boardWidth: Int) : Int {
        return boardWidth / game.boardSize - 2 * (game.boardSize - 1)
    }

    fun handleMove(square: Int) {
        if (! isSquareFree(square)) {
            return
        }
        game.makeMove(square / game.boardSize, square % game.boardSize)
        context.updateChips()
    }

    fun getWhite(): List<Int> {
        return game.whiteChips().map { it[0] * game.boardSize + it[1] }
    }

    fun getBlack(): List<Int> {
        return game.blackChips().map { it[0] * game.boardSize + it[1] }
    }

    constructor(activity: MainActivity) {
        this.context = activity
        game = Game()
        val black = ArrayList<IntArray>()
        black.add(intArrayOf(1, 1))
        black.add(intArrayOf(3, 3))
        black.add(intArrayOf(4, 4))
        black.add(intArrayOf(1, 5))
        black.add(intArrayOf(3, 5))
        black.add(intArrayOf(4, 5))
        black.add(intArrayOf(6, 5))

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(0, 0))
        white.add(intArrayOf(2, 2))
        white.add(intArrayOf(0, 5))
        white.add(intArrayOf(2, 5))
        white.add(intArrayOf(7, 5))


        game.setNewPosition(Position(white, black))
    }
}
