package algie.parvin.othello

import algie.parvin.othello.model.DBRepository
import algie.parvin.othello.model.Game
import algie.parvin.othello.model.Position
import algie.parvin.othello.model.db.Puzzle
import io.reactivex.Flowable
import org.junit.Assert
import org.junit.Test
import java.util.logging.Logger


class ModelGameValidMovesUnitTest {

    private val logger = Logger.getLogger(ModelGameValidMovesUnitTest::class.java.name)

    object dummyRepo : DBRepository {
        override fun updatePuzzle(puzzle: Puzzle) { }

        override fun getAllPuzzles(): Flowable<List<Puzzle>> {
            val white = ArrayList<IntArray>()
            white.add(intArrayOf(2, 0))
            white.add(intArrayOf(7, 0))
            white.add(intArrayOf(1, 1))
            white.add(intArrayOf(0, 4))
            white.add(intArrayOf(5, 6))
            white.add(intArrayOf(7, 6))
            white.add(intArrayOf(6, 2))

            val black = ArrayList<IntArray>()
            black.add(intArrayOf(1, 0))
            black.add(intArrayOf(0, 1))
            black.add(intArrayOf(2, 1))
            black.add(intArrayOf(3, 1))
            black.add(intArrayOf(4, 1))
            black.add(intArrayOf(5, 1))
            black.add(intArrayOf(6, 1))
            black.add(intArrayOf(0, 2))
            black.add(intArrayOf(3, 2))
            black.add(intArrayOf(4, 2))
            black.add(intArrayOf(5, 2))
            black.add(intArrayOf(0, 3))
            black.add(intArrayOf(4, 3))
            black.add(intArrayOf(1, 6))
            black.add(intArrayOf(4, 6))

            return Flowable.just(listOf(Puzzle(1, 8, black, white, false)))
        }
    }

    private var game: Game

    private var validWhiteMoves: ArrayList<IntArray>
    private var invalidWhiteMoves: ArrayList<IntArray>
    private var validBlackMoves: ArrayList<IntArray>
    private var invalidBlackMoves: ArrayList<IntArray>

    init {
        game = Game(boardSize = 8, repository = dummyRepo)
        game.puzzleObservable.subscribe {
            game.setNewPosition(0)
        }

        validWhiteMoves = ArrayList()
        validWhiteMoves.add(intArrayOf(0, 0))
        validWhiteMoves.add(intArrayOf(2, 2))
        validWhiteMoves.add(intArrayOf(3, 6))
        validWhiteMoves.add(intArrayOf(3, 4))
        validWhiteMoves.add(intArrayOf(4, 0))
        validWhiteMoves.add(intArrayOf(5, 3))
        validWhiteMoves.add(intArrayOf(6, 0))
        validWhiteMoves.add(intArrayOf(7, 1))

        invalidWhiteMoves = ArrayList()
        invalidWhiteMoves.add(intArrayOf(3, 0))
        invalidWhiteMoves.add(intArrayOf(5, 0))
        (0 until game.boardSize - 1).map { i -> invalidWhiteMoves.add(intArrayOf(i, 1)) }
        invalidWhiteMoves.add(intArrayOf(1, 2))
        invalidWhiteMoves.add(intArrayOf(7, 2))
        (0..2).map { i -> invalidWhiteMoves.add(intArrayOf(i, 4)) }
        (4..game.boardSize).map { i -> invalidWhiteMoves.add(intArrayOf(i, 4)) }
        (0..game.boardSize).map { i -> invalidWhiteMoves.add(intArrayOf(i, 5)) }
        (0..game.boardSize).map { i -> invalidWhiteMoves.add(intArrayOf(i, 7)) }

        validBlackMoves =  ArrayList()
        validBlackMoves.add(intArrayOf(3, 0))
        validBlackMoves.add(intArrayOf(1, 2))
        validBlackMoves.add(intArrayOf(7, 2))
        validBlackMoves.add(intArrayOf(6, 3))
        validBlackMoves.add(intArrayOf(7, 3))
        validBlackMoves.add(intArrayOf(0, 5))
        validBlackMoves.add(intArrayOf(6, 6))

        invalidBlackMoves = ArrayList()
        (0..2).union(4..game.boardSize).map { invalidBlackMoves.add(intArrayOf(it, 0)) }
        (0 until game.boardSize).map { invalidBlackMoves.add(intArrayOf(it, 1)) }
        invalidBlackMoves.add(intArrayOf(2, 2))
        (0..5).map { invalidBlackMoves.add(intArrayOf(it, 3)) }
        (0 until game.boardSize).map { invalidBlackMoves.add(intArrayOf(it, 4)) }
        (1 until game.boardSize).map { invalidBlackMoves.add(intArrayOf(it, 5)) }
        (0 until 6).map { invalidBlackMoves.add(intArrayOf(it, 6)) }
        (0 until game.boardSize).map { invalidBlackMoves.add(intArrayOf(it, 7)) }
    }

    @Test
    fun whiteMoves_validMovesAreRecognisedAsValid() {
        game.setMoveWhite()
        validWhiteMoves.map {
            logger.info("Check for valid white move: " + it[0].toString() + " " + it[1].toString())
            Assert.assertEquals(game.isMoveValid(it[0], it[1]), true)
        }
    }

    @Test
    fun whiteMoves_invalidMovesAreNotRecognisedAsValid() {
        game.setMoveWhite()
        invalidWhiteMoves.map {
            logger.info("Check for invalid white move: " + it[0].toString() + " " + it[1].toString())
            Assert.assertEquals(game.isMoveValid(it[0], it[1]), false)
        }
    }

    @Test
    fun blackMoves_validMovesAreRecognisedAsValid() {
        game.setMoveBlack()
        validBlackMoves.map {
            logger.info("Check for invalid black move: " + it[0].toString() + " " + it[1].toString())
            Assert.assertEquals(game.isMoveValid(it[0], it[1]), true)
        }
    }

    @Test
    fun blackMoves_invalidMovesAreNotRecognisedAsValid() {
        game.setMoveBlack()
        invalidBlackMoves.map {
            logger.info("Check for invalid black move: " + it[0].toString() + " " + it[1].toString())
            Assert.assertEquals(game.isMoveValid(it[0], it[1]), false)
        }
    }
}