package algie.parvin.othello.model

import kotlin.math.min

const val BLACK = 'B'
const val WHITE = 'W'
val CHIPS = charArrayOf(BLACK, WHITE)


class Game(val boardSize: Int = 8) {

    var turn = WHITE
        private set
    var initialPosition = Array(boardSize) { CharArray(boardSize) }
    var position: Array<CharArray>

    init {
        position = Array(boardSize) { CharArray(boardSize) }
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
            if (position[indices[0]][indices[1]] == turn) {
                break
            }
            position[indices[0]][indices[1]] = turn
            reversedChips.add(indices)
        }
        return reversedChips
    }

    private fun updateChips(row: Int, column: Int) : List<IntArray> {
        val chips = ArrayList<IntArray>()
        if (isMoveValidInRange(row, column, (column-1 downTo 0).map { position[row][it] })) {
            chips.addAll(
                reverseChipsInRange((column-1 downTo 0).map { intArrayOf(row, it) }))
        }
        if (isMoveValidInRange(row, column, (column+1 until boardSize).map { position[row][it] })) {
            chips.addAll(
                reverseChipsInRange((column+1 until boardSize).map { intArrayOf(row, it) }))
        }
        if (isMoveValidInRange(row, column, (row+1 until boardSize).map { position[it][column] })) {
            chips.addAll(
                reverseChipsInRange((row+1 until boardSize).map { intArrayOf(it, column) }))
        }
        if (isMoveValidInRange(row, column, (row-1 downTo 0).map { position[it][column] })) {
            chips.addAll(
                reverseChipsInRange((row-1 downTo 0).map { intArrayOf(it, column) }))
        }
        if (isMoveValidInRange(row, column, (1..min(row, column)).map { position[row - it][column - it] })) {
            chips.addAll(
                reverseChipsInRange((1..min(row, column)).map { intArrayOf(row - it, column - it) }))
        }
        if (isMoveValidInRange(row, column, (1..min(boardSize - 1 - row, boardSize - 1 - column)).map { position[row + it][column + it] })) {
            chips.addAll(
                reverseChipsInRange((1..min(boardSize - 1 - row, boardSize - 1 - column)).map { intArrayOf(row + it, column + it) }))
        }
        if (isMoveValidInRange(row, column, (1..min(boardSize - row - 1, column)).map { position[row + it][column - it] })) {
            chips.addAll(
                reverseChipsInRange((1..min(boardSize - row - 1, column)).map { intArrayOf(row + it, column - it) }))
        }
        if (isMoveValidInRange(row, column, (1..min(row, boardSize - 1 - column)).map { position[row - it][column + it] })) {
            chips.addAll(
                reverseChipsInRange((1..min(row, boardSize - 1 - column)).map { intArrayOf(row - it, column + it) }))
        }

        return chips
    }

    fun isSquareFree(row: Int, column: Int): Boolean {
        if (row < 0 || row >= boardSize || column < 0 || column >= boardSize) {
            return false
        }
        return position[row][column] !in CHIPS
    }

    fun whiteChips(): List<IntArray> {
        val white = ArrayList<IntArray>()
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (position[i][j] == WHITE) {
                    white.add(intArrayOf(i, j))
                }
            }
        }
        return white
    }

    fun blackChips(): List<IntArray> {
        val black = ArrayList<IntArray>()
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (position[i][j] == BLACK) {
                    black.add(intArrayOf(i, j))
                }
            }
        }
        return black
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

        return isMoveValidInRange(row, column, (column+1 until boardSize).map { position[row][it] })
                || (isMoveValidInRange(row, column, (column-1 downTo 0).map { position[row][it] }))
                || (isMoveValidInRange(row, column, (row+1 until boardSize).map { position[it][column] }))
                || (isMoveValidInRange(row, column, (row-1 downTo 0).map { position[it][column] }))
                || (isMoveValidInRange(row, column, (1..min(row, column)).map { position[row - it][column - it] }))
                || (isMoveValidInRange(row, column, (1..min(boardSize - 1 - row, boardSize - 1 - column)).map { position[row + it][column + it] }))
                || (isMoveValidInRange(row, column, (1..min(boardSize - row - 1, column)).map { position[row + it][column - it] }))
                || (isMoveValidInRange(row, column, (1..min(row, boardSize - 1 - column)).map { position[row - it][column + it] }))
    }

    fun makeMove(row: Int, column: Int): List<IntArray> {
        position[row][column] = turn
        val chips = updateChips(row, column)
        changeTurn()
        return chips
    }

    fun setNewPosition(newPosition: Position) {
        newPosition.whiteChips.map { chip -> initialPosition[chip[0]][chip[1]] = WHITE }
        newPosition.blackChips.map { chip -> initialPosition[chip[0]][chip[1]] = BLACK }
        position = initialPosition.map { it.clone() }.toTypedArray()
    }

    fun setMoveWhite() {
        turn = WHITE
    }

    fun setMoveBlack() {
        turn = BLACK
    }
}