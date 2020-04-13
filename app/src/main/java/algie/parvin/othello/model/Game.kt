package algie.parvin.othello.model

import algie.parvin.othello.db.Puzzle
import io.reactivex.Flowable
import io.reactivex.Single


enum class Chip {
    BLACK, WHITE, NONE
}


interface DBRepository {
    fun updatePuzzle(puzzle: Puzzle)
    fun getPuzzle(id: Int): Single<Puzzle>
    fun getDefaultPuzzle(): Single<Puzzle>
}


class Game(val boardSize: Int = 8, val repository: DBRepository) {

    private val gameLogic = GameLogic()
    lateinit var puzzle: Puzzle
    var turn = Chip.WHITE
        private set

    private lateinit var variantsTree: VariantsTree
    private lateinit var recentPlayerMove: Field

    private fun oppositeColor() : Chip {
        return if (turn == Chip.WHITE) Chip.BLACK else Chip.WHITE
    }

    private fun changeTurn() {
        turn = oppositeColor()
    }

    fun setMoveWhite() {
        turn = Chip.WHITE
    }

    fun setMoveBlack() {
        turn = Chip.BLACK
    }

    fun isSquareFree(row: Int, column: Int): Boolean {
        return gameLogic.isSquareFree(puzzle, row, column)
    }

    fun getOpponentMove() : Field {
        if (puzzle.movesLeft > 0) {
            val opponentMove = gameLogic.findBestMove(puzzle)
            return opponentMove.move
        }
        return Field(0, 0, Chip.BLACK)
    }

    fun isMoveValid(row: Int, column: Int) : Boolean {
        return gameLogic.isMoveValid(puzzle, turn, boardSize, row, column)
    }

    fun makePlayerMove(row: Int, column: Int): List<Field> {
        if (turn == Chip.WHITE) {
            puzzle.movesLeft--
        }
        puzzle.position[row][column] = turn
        val chips = gameLogic.updateChips(puzzle, turn, row, column)
        recentPlayerMove = Field(row, column, turn)
        changeTurn()
        return chips
    }

    fun setNewPosition(newPuzzle: Puzzle) {
        puzzle = newPuzzle
        setMoveWhite()
        puzzle.position = puzzle.getInitialPosition().map { it.clone() }.toTypedArray()
        variantsTree = VariantsTree().apply { buildFromString(puzzle.variants) }
    }

    fun hasPlayerWon(): Boolean {
        if (turn == Chip.BLACK) {
            puzzle.position.map { row -> row.map { if (it == Chip.BLACK) return false } }
            return true
        }
        return false
    }

    fun hasPlayerLost(): Boolean {
        if (turn == Chip.BLACK) {
            if (puzzle.movesLeft != 0) {
                return gameLogic.getAllValidOpponentMoves(puzzle).isEmpty()
            }
            return !hasPlayerWon()
        }
        return false
    }

    fun savePuzzleAsSolved() {
        puzzle.solved = true
        repository.updatePuzzle(puzzle)
    }
}