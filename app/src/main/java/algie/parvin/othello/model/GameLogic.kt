package algie.parvin.othello.model

import algie.parvin.othello.db.Puzzle
import kotlin.math.min

data class MoveOption(val move: IntArray, val playerWins: Boolean)

class GameLogic {

    private fun isMoveValidInRange(turn: Chip, chips: List<Chip>) : Boolean {
        if (chips.getOrNull(0) == turn) {
            return false
        }
        for (chip in chips) {
            if (chip == Chip.NONE) {
                return false
            }
            if (chip == turn) {
                return true
            }
        }
        return false
    }

    private fun getAllValidMoves(turn: Chip, puzzle: Puzzle): List<IntArray> {
        val validMoves = mutableListOf<IntArray>()
        for (row in 0 until puzzle.boardSize) {
            for (column in 0 until puzzle.boardSize) {
                if (isMoveValid(puzzle, turn, puzzle.boardSize, row, column)) {
                    validMoves.add(intArrayOf(row, column))
                }
            }
        }
        return validMoves
    }

    private fun getAllValidPlayerMoves(puzzle: Puzzle): List<IntArray> {
        return getAllValidMoves(Chip.WHITE, puzzle)
    }

    private fun getAllValidOpponentMoves(puzzle: Puzzle): List<IntArray> {
        return getAllValidMoves(Chip.BLACK, puzzle)
    }

    private fun playerWins(puzzle: Puzzle): Boolean {
        puzzle.position.forEach { row -> row.forEach {
            if (it == Chip.BLACK) {
                return false
            }
        } }
        return true
    }

    private fun findBestFinishingMove(puzzle: Puzzle): MoveOption {
        outer@ for (move in getAllValidOpponentMoves(puzzle)) {
            val p = puzzle.clone() as Puzzle
            p.position[move[0]][move[1]] = Chip.BLACK
            updateChips(p, Chip.BLACK, move[0], move[1])
            for (playerMove in getAllValidPlayerMoves(p)) {
                val p1 = p.clone() as Puzzle
                p1.position[playerMove[0]][playerMove[1]] = Chip.WHITE
                updateChips(p1, Chip.WHITE, playerMove[0], playerMove[1])
                if (playerWins(p1)) {
                    continue@outer
                }
            }
            return MoveOption(move, false)
        }
        return MoveOption(
            getAllValidOpponentMoves(puzzle).getOrElse(0) { intArrayOf(0, 0) },
            true)
    }

    fun findBestMove(puzzle: Puzzle): MoveOption {
        if (puzzle.movesLeft == 1) {
            return findBestFinishingMove(puzzle)
        } else {
            for (move in getAllValidOpponentMoves(puzzle)) {
                val p = puzzle.clone() as Puzzle
                p.position[move[0]][move[1]] = Chip.BLACK
                updateChips(p, Chip.BLACK, move[0], move[1])
                val opponentMoves = mutableListOf<MoveOption>()
                for (playerMove in getAllValidPlayerMoves(p)) {
                    val p1 = p.clone() as Puzzle
                    p1.movesLeft--
                    p1.position[playerMove[0]][playerMove[1]] = Chip.WHITE
                    updateChips(p1, Chip.WHITE, playerMove[0], playerMove[1])
                    opponentMoves.add(findBestMove(p1))
                }
                if (opponentMoves.none { !it.playerWins }) {
                    return MoveOption(move, false)
                }
            }
            return MoveOption(
                getAllValidOpponentMoves(puzzle).getOrElse(0) { intArrayOf(0, 0) },
                true)
        }
    }

    fun isSquareFree(puzzle: Puzzle, row: Int, column: Int): Boolean {
        if (row < 0 || row >= puzzle.boardSize || column < 0 || column >= puzzle.boardSize) {
            return false
        }
        return puzzle.position[row][column] == Chip.NONE
    }

    fun isMoveValid(puzzle: Puzzle, turn: Chip, boardSize: Int, row: Int, column: Int) : Boolean {
        if (puzzle.movesLeft == 0) {
            return false
        }

        if (row >= boardSize || row < 0 || column >= boardSize || column < 0) {
            return false
        }

        if (!isSquareFree(puzzle, row, column)) {
            return false
        }

        return isMoveValidInRange(turn, (column+1 until boardSize).map { puzzle.position[row][it] })
                || (isMoveValidInRange(turn, (column-1 downTo 0).map { puzzle.position[row][it] }))
                || (isMoveValidInRange(turn, (row+1 until boardSize).map { puzzle.position[it][column] }))
                || (isMoveValidInRange(turn, (row-1 downTo 0).map { puzzle.position[it][column] }))
                || (isMoveValidInRange(turn, (1..min(row, column)).map { puzzle.position[row - it][column - it] }))
                || (isMoveValidInRange(turn, (1..min(boardSize - 1 - row, boardSize - 1 - column)).map { puzzle.position[row + it][column + it] }))
                || (isMoveValidInRange(turn, (1..min(boardSize - row - 1, column)).map { puzzle.position[row + it][column - it] }))
                || (isMoveValidInRange(turn, (1..min(row, boardSize - 1 - column)).map { puzzle.position[row - it][column + it] }))
    }

    private fun reverseChipsInRange(puzzle: Puzzle, turn: Chip, chips: List<IntArray>) : List<IntArray> {
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

    fun updateChips(puzzle: Puzzle, turn: Chip, row: Int, column: Int) : List<IntArray> {
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