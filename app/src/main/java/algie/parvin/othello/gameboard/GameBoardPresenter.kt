package algie.parvin.othello.gameboard

import algie.parvin.othello.model.*
import algie.parvin.othello.db.PuzzleRepository
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class GameBoardPresenter(viewFragment: GameBoardContract.ViewInterface, app: Application) :
    GameBoardContract.PresenterInterface {

    private var view: GameBoardContract.ViewInterface = viewFragment
    private val game: Game = Game(repository = PuzzleRepository(app))
    private val movesObservable = MutableLiveData<Int>()
    private lateinit var puzzleSubscriber: Disposable

    override fun getMovesObservable(): LiveData<Int> {
        return movesObservable
    }

    override fun getBoardSize() : Int {
        return game.boardSize
    }

    override fun receiveOpponentMove() {
        val field = game.getOpponentMove()
        if (game.isMoveValid(field.row, field.column)) {
            handlePlayerMove(field.row * game.boardSize + field.column)
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
        val indices = chips.map { it.row * game.boardSize + it.column }

        if (isWhiteMove) {
            movesObservable.postValue(game.puzzle.movesLeft)
            view.setChipsOnBoard(listOf(square), listOf(), true)
        } else {
            view.setChipsOnBoard(listOf(), listOf(square), false)
        }

        view.reverseChips(indices, isWhiteMove)

        if (game.hasPlayerWon()) {
            game.savePuzzleAsSolved()
            view.onPlayerWin()
        }
        if (game.hasPlayerLost()) {
            view.onPlayerLose()
        }
    }

    override fun loadPuzzle(id: Int) {
        puzzleSubscriber = game.repository.getPuzzle(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { puzzle ->
                game.setNewPosition(puzzle)
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
                view.showNewPuzzle(white, black)
                movesObservable.postValue(game.puzzle.movesCounter)
            }
    }

    override fun loadDefaultPuzzle() {
        puzzleSubscriber = game.repository.getDefaultPuzzle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { puzzle ->
                game.setNewPosition(puzzle)
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
                view.showNewPuzzle(white, black)
                movesObservable.postValue(game.puzzle.movesCounter)
            }
    }
}
