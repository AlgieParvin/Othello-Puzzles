package algie.parvin.othello.model

import algie.parvin.othello.model.db.Puzzle
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

    lateinit var puzzleList: List<Puzzle>
    lateinit var puzzle: Puzzle
    var turn = WHITE
        private set

    var puzzleObservable: Flowable<Puzzle> = repository.getAllPuzzles()
        .map {
            puzzleList = it
            puzzle = it[0]
            puzzle.position = puzzle.getInitialPosition().map { it.clone() }.toTypedArray()
            return@map puzzle
        }

    private fun oppositeColor() : Char {
        return (if (turn == WHITE) BLACK else WHITE)
    }

    private fun changeTurn() {
        turn = oppositeColor()
    }

    private fun reverseChipsInRange(chips: List<IntArray>) : List<IntArray> {
        val reversedChips = ArrayList<IntArray>()
        for (indices in chips) {
            if (puzzle.position[indices[0]][indices[1]] == turn) {
                break
            }
            puzzle.position[indices[0]][indices[1]] = turn
            reversedChips.add(indices)
        }
        return reversedChips
    }

    private fun updateChips(row: Int, column: Int) : List<IntArray> {
        val chips = ArrayList<IntArray>()
        if (isMoveValidInRange(row, column, (column-1 downTo 0).map { puzzle.position[row][it] })) {
            chips.addAll(
                reverseChipsInRange((column-1 downTo 0).map { intArrayOf(row, it) }))
        }
        if (isMoveValidInRange(row, column, (column+1 until boardSize).map { puzzle.position[row][it] })) {
            chips.addAll(
                reverseChipsInRange((column+1 until boardSize).map { intArrayOf(row, it) }))
        }
        if (isMoveValidInRange(row, column, (row+1 until boardSize).map { puzzle.position[it][column] })) {
            chips.addAll(
                reverseChipsInRange((row+1 until boardSize).map { intArrayOf(it, column) }))
        }
        if (isMoveValidInRange(row, column, (row-1 downTo 0).map { puzzle.position[it][column] })) {
            chips.addAll(
                reverseChipsInRange((row-1 downTo 0).map { intArrayOf(it, column) }))
        }
        if (isMoveValidInRange(row, column, (1..min(row, column)).map { puzzle.position[row - it][column - it] })) {
            chips.addAll(
                reverseChipsInRange((1..min(row, column)).map { intArrayOf(row - it, column - it) }))
        }
        if (isMoveValidInRange(row, column, (1..min(boardSize - 1 - row, boardSize - 1 - column)).map { puzzle.position[row + it][column + it] })) {
            chips.addAll(
                reverseChipsInRange((1..min(boardSize - 1 - row, boardSize - 1 - column)).map { intArrayOf(row + it, column + it) }))
        }
        if (isMoveValidInRange(row, column, (1..min(boardSize - row - 1, column)).map { puzzle.position[row + it][column - it] })) {
            chips.addAll(
                reverseChipsInRange((1..min(boardSize - row - 1, column)).map { intArrayOf(row + it, column - it) }))
        }
        if (isMoveValidInRange(row, column, (1..min(row, boardSize - 1 - column)).map { puzzle.position[row - it][column + it] })) {
            chips.addAll(
                reverseChipsInRange((1..min(row, boardSize - 1 - column)).map { intArrayOf(row - it, column + it) }))
        }

        return chips
    }

    fun isSquareFree(row: Int, column: Int): Boolean {
        if (row < 0 || row >= boardSize || column < 0 || column >= boardSize) {
            return false
        }
        return puzzle.position[row][column] !in CHIPS
    }

    private fun isMoveValidInRange(row: Int, column: Int, chips: List<Char>) : Boolean {
        if (chips.getOrNull(0) == turn) {
            return false
        }
        for (chip in chips) {
            if (chip !in CHIPS) {
                return false
            }
            if (chip == turn) {
                return true
            }
        }
        return false
    }

    fun isMoveValid(row: Int, column: Int) : Boolean {
        if (row >= boardSize || row < 0 || column >= boardSize || column < 0) {
            return false
        }

        if (!isSquareFree(row, column)) {
            return false
        }

        return isMoveValidInRange(row, column, (column+1 until boardSize).map { puzzle.position[row][it] })
                || (isMoveValidInRange(row, column, (column-1 downTo 0).map { puzzle.position[row][it] }))
                || (isMoveValidInRange(row, column, (row+1 until boardSize).map { puzzle.position[it][column] }))
                || (isMoveValidInRange(row, column, (row-1 downTo 0).map { puzzle.position[it][column] }))
                || (isMoveValidInRange(row, column, (1..min(row, column)).map { puzzle.position[row - it][column - it] }))
                || (isMoveValidInRange(row, column, (1..min(boardSize - 1 - row, boardSize - 1 - column)).map { puzzle.position[row + it][column + it] }))
                || (isMoveValidInRange(row, column, (1..min(boardSize - row - 1, column)).map { puzzle.position[row + it][column - it] }))
                || (isMoveValidInRange(row, column, (1..min(row, boardSize - 1 - column)).map { puzzle.position[row - it][column + it] }))
    }

    fun makeMove(row: Int, column: Int): List<IntArray> {
        puzzle.position[row][column] = turn
        val chips = updateChips(row, column)
        changeTurn()
        return chips
    }

    fun setNewPosition(index: Int) {
//        puzzle = puzzleList[index]
        puzzle.position = puzzle.getInitialPosition().map { it.clone() }.toTypedArray()
    }

    fun setMoveWhite() {
        turn = WHITE
    }

    fun setMoveBlack() {
        turn = BLACK
    }
}