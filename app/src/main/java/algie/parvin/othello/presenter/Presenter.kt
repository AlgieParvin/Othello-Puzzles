package algie.parvin.othello.presenter

import algie.parvin.othello.PresenterInterface
import algie.parvin.othello.model.*
import algie.parvin.othello.model.db.PuzzleRepository
import android.app.Application


interface ViewInterface {
    fun setChipsOnBoard(white: List<Int>, black: List<Int>)
    fun animateBoardCreation()
    fun reverseChips(chipIndices: List<Int>, reverseToWhite: Boolean)
}


class Presenter(activity: ViewInterface, app: Application) : PresenterInterface {

    private var view: ViewInterface = activity
    private val game: Game = Game(repository = PuzzleRepository(app))

    override fun getBoardSize() : Int {
        return game.boardSize
    }

    override fun handleMove(square: Int) {
        val row = square / game.boardSize
        val column = square % game.boardSize
        val isWhiteMove = game.turn == WHITE

        if (!game.isSquareFree(row, column) or !game.isMoveValid(row, column)) {
            return
        }
        val chips = game.makeMove(row, column)
        val indices = chips.map { it[0] * game.boardSize + it[1] }

        if (isWhiteMove) {
            view.setChipsOnBoard(listOf(square), listOf())
        } else {
            view.setChipsOnBoard(listOf(), listOf(square))
        }

        view.reverseChips(indices, isWhiteMove)
    }

    override fun getWhite(): List<Int> {
        return game.whiteChips().map { it[0] * game.boardSize + it[1] }
    }

    override fun getBlack(): List<Int> {
        return game.blackChips().map { it[0] * game.boardSize + it[1] }
    }
}
