package algie.parvin.othello.presenter

import algie.parvin.othello.PresenterInterface
import algie.parvin.othello.model.CHIPS
import algie.parvin.othello.model.Game
import algie.parvin.othello.model.Position


interface ViewInterface {
    fun updateChips()
}


class Presenter(activity: ViewInterface) : PresenterInterface {

    private var view: ViewInterface = activity
    private val game: Game

    private fun isSquareFree(square: Int): Boolean {
        return !(game.position[square / game.boardSize][square % game.boardSize] in CHIPS)
    }

    override fun getBoardSize() : Int {
        return game.boardSize
    }

    override fun handleMove(square: Int) {
        if (! isSquareFree(square)) {
            return
        }
        game.makeMove(square / game.boardSize, square % game.boardSize)
        view.updateChips()
    }

    override fun getWhite(): List<Int> {
        return game.whiteChips().map { it[0] * game.boardSize + it[1] }
    }

    override fun getBlack(): List<Int> {
        return game.blackChips().map { it[0] * game.boardSize + it[1] }
    }

    init {
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
