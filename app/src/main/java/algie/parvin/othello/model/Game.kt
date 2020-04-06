package algie.parvin.othello.model

import algie.parvin.othello.db.Puzzle
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.min

const val BLACK = 'B'
const val WHITE = 'W'
val CHIPS = charArrayOf(BLACK, WHITE)


interface DBRepository {
    fun updatePuzzle(puzzle: Puzzle)
    fun getAllPuzzles() : Flowable<List<Puzzle>>
}


class Game(val boardSize: Int = 8, val repository: DBRepository) {

    val gameLogic = GameLogic()
    lateinit var puzzleList: List<Puzzle>
    lateinit var puzzle: Puzzle
    var turn = WHITE
        private set

    private lateinit var variantsTree: VariantsTree
    private lateinit var recentPlayerMove: IntArray

    var puzzleObservable: Flowable<Puzzle> = repository.getAllPuzzles()
        .map {
            puzzleList = it
            puzzle = it[0]
            variantsTree = VariantsTree().apply { buildFromString(puzzle.variants) }
            puzzle.position = puzzle.getInitialPosition().map { it.clone() }.toTypedArray()
            return@map puzzle
        }

    private fun oppositeColor() : Char {
        return (if (turn == WHITE) BLACK else WHITE)
    }

    private fun changeTurn() {
        turn = oppositeColor()
    }

    fun isSquareFree(row: Int, column: Int): Boolean {
        if (row < 0 || row >= boardSize || column < 0 || column >= boardSize) {
            return false
        }
        return puzzle.position[row][column] !in CHIPS
    }

    fun getOpponentMove() : IntArray {
        val opponentMove = variantsTree.onPlayerMove(recentPlayerMove)
        if (opponentMove == null) {
            return intArrayOf(-1, -1)
        } else {
            return opponentMove
        }
    }

    fun isMoveValid(row: Int, column: Int) : Boolean {
        if (row >= boardSize || row < 0 || column >= boardSize || column < 0) {
            return false
        }

        if (!isSquareFree(row, column)) {
            return false
        }

        return gameLogic.isMoveValidInRange(turn, (column+1 until boardSize).map { puzzle.position[row][it] })
                || (gameLogic.isMoveValidInRange(turn, (column-1 downTo 0).map { puzzle.position[row][it] }))
                || (gameLogic.isMoveValidInRange(turn, (row+1 until boardSize).map { puzzle.position[it][column] }))
                || (gameLogic.isMoveValidInRange(turn, (row-1 downTo 0).map { puzzle.position[it][column] }))
                || (gameLogic.isMoveValidInRange(turn, (1..min(row, column)).map { puzzle.position[row - it][column - it] }))
                || (gameLogic.isMoveValidInRange(turn, (1..min(boardSize - 1 - row, boardSize - 1 - column)).map { puzzle.position[row + it][column + it] }))
                || (gameLogic.isMoveValidInRange(turn, (1..min(boardSize - row - 1, column)).map { puzzle.position[row + it][column - it] }))
                || (gameLogic.isMoveValidInRange(turn, (1..min(row, boardSize - 1 - column)).map { puzzle.position[row - it][column + it] }))
    }

    fun makePlayerMove(row: Int, column: Int): List<IntArray> {
        puzzle.position[row][column] = turn
        val chips = gameLogic.updateChips(puzzle, turn, row, column)
        recentPlayerMove = intArrayOf(row, column)
        changeTurn()
        return chips
    }

    fun makeOpponentMove(): List<IntArray> {
        val row = 2
        val column = 0
        puzzle.position[row][column] = turn
        val chips = gameLogic.updateChips(puzzle, turn, row, column)
        changeTurn()
        return chips
    }

    fun setNewPosition(index: Int) {
        puzzle.position = puzzle.getInitialPosition().map { it.clone() }.toTypedArray()
        variantsTree = VariantsTree().apply { buildFromString(puzzle.variants) }
    }

    fun setMoveWhite() {
        turn = WHITE
    }

    fun setMoveBlack() {
        turn = BLACK
    }
}