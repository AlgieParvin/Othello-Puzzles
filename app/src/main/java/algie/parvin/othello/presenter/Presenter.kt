package algie.parvin.othello.presenter

import algie.parvin.othello.PresenterInterface
import algie.parvin.othello.model.CHIPS
import algie.parvin.othello.model.Game
import algie.parvin.othello.model.Position


interface ViewInterface {
    fun updateChips()
    fun animateBoardCreation()
}


class Presenter(activity: ViewInterface) : PresenterInterface {

    private var view: ViewInterface = activity
    private val game: Game

    override fun getBoardSize() : Int {
        return game.boardSize
    }

    override fun handleMove(square: Int) {
        val row = square / game.boardSize
        val column = square % game.boardSize
        if (!game.isSquareFree(row, column) or !game.isMoveValid(row, column)) {
            return
        }
        game.makeMove(row, column)
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

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(2, 0))
        white.add(intArrayOf(7, 0))
        white.add(intArrayOf(1, 1))
        white.add(intArrayOf(0, 4))
        white.add(intArrayOf(5, 6))
        white.add(intArrayOf(7, 6))
        white.add(intArrayOf(6, 2))

        val black = ArrayList<IntArray>()
        black.add(intArrayOf(1, 0))
        black.add(intArrayOf(0, 1))
        black.add(intArrayOf(2, 1))
        black.add(intArrayOf(3, 1))
        black.add(intArrayOf(4, 1))
        black.add(intArrayOf(5, 1))
        black.add(intArrayOf(6, 1))
        black.add(intArrayOf(0, 2))
        black.add(intArrayOf(3, 2))
        black.add(intArrayOf(4, 2))
        black.add(intArrayOf(5, 2))
        black.add(intArrayOf(0, 3))
        black.add(intArrayOf(4, 3))
        black.add(intArrayOf(1, 6))
        black.add(intArrayOf(4, 6))

        game.setNewPosition(Position(white, black))
        game.setMoveBlack()
    }
}
