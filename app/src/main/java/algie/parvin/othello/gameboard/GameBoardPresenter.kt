package algie.parvin.othello.gameboard

import algie.parvin.othello.model.*
import algie.parvin.othello.db.PuzzleRepository
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class GameBoardPresenter(viewFragment: GameBoardContract.ViewInterface, app: Application) :
    GameBoardContract.PresenterInterface {

    private var view: GameBoardContract.ViewInterface = viewFragment
    private val game: Game = Game(repository = PuzzleRepository(app))
    private val movesObservable = MutableLiveData<Int>()

    override fun getMovesObservable(): LiveData<Int> {
        return movesObservable
    }

    override fun getBoardSize() : Int {
        return game.boardSize
    }

    override fun receiveOpponentMove() {
        val position = game.getOpponentMove()
        if (position.isNotEmpty() && game.isMoveValid(position[0], position[1])) {
            handlePlayerMove(position[0] * game.boardSize + position[1])
        }
    }

    override fun handlePlayerMove(square: Int) {
        val row = square / game.boardSize
        val column = square % game.boardSize
        val isWhiteMove = game.turn == Chip.WHITE

        if (!game.isSquareFree(row, column) or !game.isMoveValid(row, column)) {
            return
        }
        val chips = game.makePlayerMove(row, column)
        val indices = chips.map { it[0] * game.boardSize + it[1] }

        if (isWhiteMove) {
            movesObservable.postValue(game.puzzle.movesLeft)
            view.setChipsOnBoard(listOf(square), listOf(), true)
        } else {
            view.setChipsOnBoard(listOf(), listOf(square), false)
        }

        view.reverseChips(indices, isWhiteMove)

        if (game.hasPlayerWon()) {
            view.onPlayerWin()
        }
        if (game.hasPlayerLost()) {
            view.onPlayerLose()
        }
    }

    override fun loadPuzzle(id: Int) {
        val subscribe = game.repository.getPuzzle(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                game.setNewPosition(it)
                val white = ArrayList<Int>()
                val black = ArrayList<Int>()
                for (i in 0 until game.boardSize) {
                    for (j in 0 until game.boardSize) {
                        if (game.puzzle.position[i][j] == Chip.WHITE) {
                            white.add(i * game.boardSize + j)
                        }
                        if (game.puzzle.position[i][j] == Chip.BLACK) {
                            black.add(i * game.boardSize + j)
                        }
                    }
                }
                view.setChipsOnBoard(white, black, false)
                movesObservable.postValue(game.puzzle.movesCounter)
            }
    }
}
