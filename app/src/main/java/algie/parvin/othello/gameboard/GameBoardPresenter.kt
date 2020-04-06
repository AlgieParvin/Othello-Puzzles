package algie.parvin.othello.gameboard

import algie.parvin.othello.model.*
import algie.parvin.othello.db.PuzzleRepository
import android.app.Application
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class GameBoardPresenter(activity: GameBoardContract.ViewInterface, app: Application) :
    GameBoardContract.PresenterInterface {

    private var view: GameBoardContract.ViewInterface = activity
    private val game: Game = Game(repository = PuzzleRepository(app))

    override fun getBoardSize() : Int {
        return game.boardSize
    }

    override fun receiveOpponentMove() {
        val position = game.getOpponentMove()
        handlePlayerMove(position[0] * game.boardSize + position[1])
    }

    override fun handlePlayerMove(square: Int) {
        val row = square / game.boardSize
        val column = square % game.boardSize
        val isWhiteMove = game.turn == WHITE

        if (!game.isSquareFree(row, column) or !game.isMoveValid(row, column)) {
            return
        }
        val chips = game.makePlayerMove(row, column)
        val indices = chips.map { it[0] * game.boardSize + it[1] }

        if (isWhiteMove) {
            view.setChipsOnBoard(listOf(square), listOf(), true)
        } else {
            view.setChipsOnBoard(listOf(), listOf(square), false)
        }

        view.reverseChips(indices, isWhiteMove)
    }

    override fun loadPuzzles() {
        game.puzzleObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val white = ArrayList<Int>()
                val black = ArrayList<Int>()
                for (i in 0 until game.boardSize) {
                    for (j in 0 until game.boardSize) {
                        if (game.puzzle.position[i][j] == WHITE) {
                            white.add(i * game.boardSize + j)
                        }
                        if (game.puzzle.position[i][j] == BLACK) {
                            black.add(i * game.boardSize + j)
                        }
                    }
                }
                view.setChipsOnBoard(white, black, false)
            }
    }
}
