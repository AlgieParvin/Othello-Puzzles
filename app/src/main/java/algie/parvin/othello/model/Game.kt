package algie.parvin.othello.model

class Game {

    var boardSize = 8
    var turn = 'W'

    var initialPosition: Array<CharArray>
    var position: Array<CharArray>

    private fun changeTurn() = if (turn == 'W') turn = 'B' else turn = 'W'

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
            if (!(position[i][column] in "WB")) {
                break
            }
            if (position[i][column] == turn) {
                reverseChipsInColumn(row, i, column)
            }
        }

        for (i in row - 1 downTo 0 step 1) {
            if (!(position[i][column] in "WB")) {
                break
            }
            if (position[i][column] == turn) {
                reverseChipsInColumn(i, row, column)
            }
        }
    }

    private fun updateChipsInMainDiagonal(row: Int, column: Int) {
        var i = row + 1
        var j = column + 1
        while (i < boardSize && j < boardSize) {
            if (!(position[i][j] in "WB")) {
                break
            }
            if (position[i][j] == turn) {
                reverseChipsInMainDiagonal(row, i, column, j)
            }
            i++
            j++
        }

        i = row - 1
        j = column - 1
        while (i > 0 && j > 0) {
            if (!(position[i][j] in "WB")) {
                break
            }
            if (position[i][j] == turn) {
                reverseChipsInMainDiagonal(i, row, j, column)
            }
            i--
            j--
        }
    }

    private fun updateChipsInAntiDiagonal(row: Int, column: Int) {
        var i = row + 1
        var j = column - 1
        while (i < boardSize && j >= 0) {
            if (!(position[i][j] in "WB")) {
                break
            }
            if (position[i][j] == turn) {
                reverseChipsInAntiDiagonal(row, i, column, j)
            }
            i++
            j--
        }

        i = row - 1
        j = column + 1
        while (i >= 0 && j < boardSize) {
            if (!(position[i][j] in "WB")) {
                break
            }
            if (position[i][j] == turn) {
                reverseChipsInAntiDiagonal(i, row, j, column)
            }
            i--
            j++
        }
    }

    private fun updateChipsInRow(row: Int, column: Int) {
        for (i in column + 1 until boardSize) {
            if (!(position[row][i] in "WB")) {
                break
            }
            if (position[row][i] == turn) {
                reverseChipsInRow(row, column, i)
            }
        }

        for (i in column - 1 downTo 0 step 1) {
            if (!(position[row][i] in "WB")) {
                break
            }
            if (position[row][i] == turn) {
                reverseChipsInRow(row, i, column)
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
                if (position[i][j] == 'W') {
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
                if (position[i][j] == 'B') {
                    black.add(intArrayOf(i, j))
                }
            }
        }
        return black
    }

    fun makeMove(row: Int, column: Int) {
        position[row][column] = turn
        updateChips(row, column)
        changeTurn()
    }

    constructor() {
        initialPosition = Array(8, {CharArray(8)})
        initialPosition[1][1] = 'W'
        initialPosition[2][2] = 'W'
        initialPosition[1][2] = 'B'
        initialPosition[2][1] = 'B'

        position = initialPosition.map { it.clone() }.toTypedArray()
    }
}