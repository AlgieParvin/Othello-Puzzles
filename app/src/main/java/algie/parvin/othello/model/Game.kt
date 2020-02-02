package algie.parvin.othello.model

import kotlin.math.max
import kotlin.math.min

const val BLACK = 'B'
const val WHITE = 'W'
val CHIPS = charArrayOf(BLACK, WHITE)


class Game(val boardSize: Int = 8) {

    private var turn = WHITE
    var initialPosition: Array<CharArray>
    var position: Array<CharArray>

    init {
        initialPosition = Array(boardSize) { CharArray(boardSize) }
        position = Array(boardSize) { CharArray(boardSize) }
    }

    private fun oppositeColor(color: Char) : Char {
        return (if (turn == WHITE) BLACK else WHITE)
    }

    private fun changeTurn() {
        turn = oppositeColor(turn)
    }


    // refactor this shit !!!
    fun isSquareFree(row: Int, column: Int): Boolean {
        if (row >= boardSize || column >= boardSize) {
            return false
        }
        return position[row][column] !in CHIPS
    }

    private fun reverseChipsInColumn(rowStart: Int, rowEnd: Int, column: Int) {
        for (i in rowStart until rowEnd) {
            position[i][column] = turn
        }
    }

    private fun reverseChipsInRow(row: Int, columnStart: Int, columnEnd: Int) {
        for (i in columnStart until columnEnd) {
            position[row][i] = turn
        }
    }

    private fun reverseChipsInMainDiagonal(rowStart: Int, rowEnd: Int, colStart: Int, colEnd: Int) {
        var i = rowStart + 1
        var j = colStart + 1
        while (i < rowEnd && j < colEnd) {
            position[i][j] = turn
            i++
            j++
        }
    }

    private fun reverseChipsInAntiDiagonal(rowStart: Int, rowEnd: Int, colStart: Int, colEnd: Int) {
        var i = rowStart + 1
        var j = colStart - 1
        while (i < rowEnd && j > colEnd) {
            position[i][j] = turn
            i++
            j--
        }
    }

    private fun updateChipsInColumn(row: Int, column: Int) {
        for (i in row + 1 until boardSize) {
            if (!(position[i][column] in CHIPS)) {
                break
            }
            if (position[i][column] == turn) {
                reverseChipsInColumn(row, i, column)
                break
            }
        }

        for (i in row - 1 downTo 0 step 1) {
            if (!(position[i][column] in CHIPS)) {
                break
            }
            if (position[i][column] == turn) {
                reverseChipsInColumn(i, row, column)
                break
            }
        }
    }

    private fun updateChipsInMainDiagonal(row: Int, column: Int) {
        var i = row + 1
        var j = column + 1
        while (i < boardSize && j < boardSize) {
            if (!(position[i][j] in CHIPS)) {
                break
            }
            if (position[i][j] == turn) {
                reverseChipsInMainDiagonal(row, i, column, j)
                break
            }
            i++
            j++
        }

        i = row - 1
        j = column - 1
        while (i >= 0 && j >= 0) {
            if (!(position[i][j] in CHIPS)) {
                break
            }
            if (position[i][j] == turn) {
                reverseChipsInMainDiagonal(i, row, j, column)
                break
            }
            i--
            j--
        }
    }

    private fun updateChipsInAntiDiagonal(row: Int, column: Int) {
        var i = row + 1
        var j = column - 1
        while (i < boardSize && j >= 0) {
            if (!(position[i][j] in CHIPS)) {
                break
            }
            if (position[i][j] == turn) {
                reverseChipsInAntiDiagonal(row, i, column, j)
                break
            }
            i++
            j--
        }

        i = row - 1
        j = column + 1
        while (i >= 0 && j < boardSize) {
            if (!(position[i][j] in CHIPS)) {
                break
            }
            if (position[i][j] == turn) {
                reverseChipsInAntiDiagonal(i, row, j, column)
                break
            }
            i--
            j++
        }
    }

    private fun updateChipsInRow(row: Int, column: Int) {
        for (i in column + 1 until boardSize) {
            if (!(position[row][i] in CHIPS)) {
                break
            }
            if (position[row][i] == turn) {
                reverseChipsInRow(row, column, i)
                break
            }
        }

        for (i in column - 1 downTo 0 step 1) {
            if (!(position[row][i] in CHIPS)) {
                break
            }
            if (position[row][i] == turn) {
                reverseChipsInRow(row, i, column)
                break
            }
        }
    }

    private fun updateChips(row: Int, column: Int) {
        updateChipsInColumn(row, column)
        updateChipsInRow(row, column)
        updateChipsInMainDiagonal(row, column)
        updateChipsInAntiDiagonal(row, column)
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

    fun isMoveValid(row: Int, column: Int) : Boolean {
        if (!isSquareFree(row, column)) {
            return false
        }

        for (i in column + 2 until boardSize) {
            if (position[row][i] !in CHIPS || position[row][i - 1] !in CHIPS) {
                break
            }
            if (position[row][i] == turn) {
                if (position[row][i - 1] == oppositeColor(turn)) {
                    return true
                }
                break
            }
        }
        for (i in column - 2 downTo 0) {
            if (position[row][i] !in CHIPS || position[row][i + 1] !in CHIPS) {
                break
            }
            if (position[row][i] == turn) {
                if (position[row][i + 1] == oppositeColor(turn)) {
                    return true
                }
                break
            }
        }

        for (i in row + 2 until boardSize) {
            if (position[i][column] !in CHIPS || position[i - 1][column] !in CHIPS) {
                break
            }
            if (position[i][column] == turn) {
                if (position[i - 1][column] == oppositeColor(turn)) {
                    return true
                }
                break
            }
        }
        for (i in row - 2 downTo 0) {
            if (position[i][column] !in CHIPS || position[i + 1][column] !in CHIPS) {
                break
            }
            if (position[i][column] == turn) {
                if (position[i + 1][column] == oppositeColor(turn)) {
                    return true
                }
                break
            }
        }

        for (i in 2..min(row, column)) {
            if (position[row - i][column - i] !in CHIPS ||
                position[row - i + 1][column - i + 1] !in CHIPS) {
                break
            }
            if (position[row - i][column - i] == turn) {
                if (position[row - i + 1][column - i + 1] == oppositeColor(turn)) {
                    return true
                }
                break
            }
        }

        for (i in -2 downTo max(row, column) - (boardSize - 1)) {
            if (position[row - i][column - i] !in CHIPS ||
                position[row - i - 1][column - i - 1] !in CHIPS) {
                break
            }
            if (position[row - i][column - i] == turn) {
                if (position[row - i - 1][column - i - 1] == oppositeColor(turn)) {
                    return true
                }
                break
            }
        }

        var i = row + 2
        var j = column - 2
        while (i < boardSize && j >= 0) {
            if (position[i][j] !in CHIPS || position[i - 1][j + 1] !in CHIPS) {
                break
            }
            if (position[i][j] == turn) {
                if (position[i - 1][j + 1] == oppositeColor(turn)) {
                    return true
                }
                break
            }
            i++
            j--
        }

        i = row - 2
        j = column + 2
        while (i >= 0 && j < boardSize) {
            if (position[i][j] !in CHIPS || position[i + 1][j - 1] !in CHIPS) {
                break
            }
            if (position[i][j] == turn) {
                if (position[i + 1][j - 1] == oppositeColor(turn)) {
                    return true
                }
                break
            }
            i--
            j++
        }
        return false
    }

    fun makeMove(row: Int, column: Int) {
        position[row][column] = turn
        updateChips(row, column)
        changeTurn()
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