package algie.parvin.othello.presenter

import algie.parvin.othello.MainActivity
import algie.parvin.othello.model.Game


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
    }
}
