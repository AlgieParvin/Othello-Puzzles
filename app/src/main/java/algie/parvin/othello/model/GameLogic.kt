package algie.parvin.othello.model

import algie.parvin.othello.db.Puzzle
import kotlin.math.min

class GameLogic {

    fun isMoveValidInRange(turn: Char, chips: List<Char>) : Boolean {
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

    fun reverseChipsInRange(puzzle: Puzzle, turn: Char, chips: List<IntArray>) : List<IntArray> {
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

    fun updateChips(puzzle: Puzzle, turn: Char, row: Int, column: Int) : List<IntArray> {
        val chips = ArrayList<IntArray>()
        if (isMoveValidInRange(turn, (column-1 downTo 0).map { puzzle.position[row][it] })) {
            chips.addAll(reverseChipsInRange(
                puzzle, turn,
                (column-1 downTo 0).map { intArrayOf(row, it) })
            )
        }
        if (isMoveValidInRange(turn, (column+1 until puzzle.boardSize).map { puzzle.position[row][it] })) {
            chips.addAll(reverseChipsInRange(
                puzzle, turn,
                (column+1 until puzzle.boardSize).map { intArrayOf(row, it) })
            )
        }
        if (isMoveValidInRange(turn, (row+1 until puzzle.boardSize).map { puzzle.position[it][column] })) {
            chips.addAll(reverseChipsInRange(
                puzzle, turn,
                (row+1 until puzzle.boardSize).map { intArrayOf(it, column) })
            )
        }
        if (isMoveValidInRange(turn, (row-1 downTo 0).map { puzzle.position[it][column] })) {
            chips.addAll(reverseChipsInRange(
                puzzle, turn, (row-1 downTo 0).map { intArrayOf(it, column) })
            )
        }
        if (isMoveValidInRange(turn, (1..min(row, column)).map { puzzle.position[row - it][column - it] })) {
            chips.addAll(reverseChipsInRange(
                puzzle, turn,
                (1..min(row, column)).map { intArrayOf(row - it, column - it) })
            )
        }
        if (isMoveValidInRange(
                turn,
                (1..min(puzzle.boardSize - 1 - row, puzzle.boardSize - 1 - column)).map { puzzle.position[row + it][column + it] })
        ) {
            chips.addAll(reverseChipsInRange(
                puzzle, turn,
                (1..min(puzzle.boardSize - 1 - row, puzzle.boardSize - 1 - column)).map { intArrayOf(row + it, column + it) })
            )
        }
        if (isMoveValidInRange(turn, (1..min(puzzle.boardSize - row - 1, column)).map { puzzle.position[row + it][column - it] })) {
            chips.addAll(reverseChipsInRange(
                puzzle, turn,
                (1..min(puzzle.boardSize - row - 1, column)).map { intArrayOf(row + it, column - it) })
            )
        }
        if (isMoveValidInRange(turn, (1..min(row, puzzle.boardSize - 1 - column)).map { puzzle.position[row - it][column + it] })) {
            chips.addAll(reverseChipsInRange(
                puzzle, turn,
                (1..min(row, puzzle.boardSize - 1 - column)).map { intArrayOf(row - it, column + it) })
            )
        }

        return chips
    }

}