package algie.parvin.othello

import algie.parvin.othello.model.BLACK
import algie.parvin.othello.model.Game
import algie.parvin.othello.model.Position
import algie.parvin.othello.model.WHITE
import org.junit.Test
import org.junit.Assert.*


class ModelGameChipsReverseUnitTest {

    private var game: Game = Game(boardSize = 8)

    private fun assertEqualChips(boardFromGame: Array<CharArray>, boardExpected: Array<CharArray>) {
        assertEquals(boardExpected.size, boardFromGame.size)

        boardExpected.indices.map {
                row -> assertArrayEquals(boardExpected[row], boardFromGame[row])
        }
    }

    @Test
    fun black_reverseOneChipInAllDirections() {
        val white = ArrayList<IntArray>()
        white.add(intArrayOf(3, 2))
        white.add(intArrayOf(3, 3))
        white.add(intArrayOf(3, 4))
        white.add(intArrayOf(4, 2))
        white.add(intArrayOf(4, 4))
        white.add(intArrayOf(5, 2))
        white.add(intArrayOf(5, 3))
        white.add(intArrayOf(5, 4))

        val black = ArrayList<IntArray>()
        black.add(intArrayOf(2, 1))
        black.add(intArrayOf(2, 3))
        black.add(intArrayOf(2, 5))
        black.add(intArrayOf(4, 1))
        black.add(intArrayOf(4, 5))
        black.add(intArrayOf(6, 1))
        black.add(intArrayOf(6, 3))
        black.add(intArrayOf(6, 5))

        val board = Array(game.boardSize) { CharArray(game.boardSize) }
        white.map { array -> board[array[0]][array[1]] = BLACK }
        black.map { array -> board[array[0]][array[1]] = BLACK }
        board[4][3] = BLACK

        game.setNewPosition(Position(white, black))
        game.setMoveBlack()
        game.makeMove(4, 3)
        assertEqualChips(game.position, board)
    }

    @Test
    fun white_reverseOneChipInAllDirections() {
        val black = ArrayList<IntArray>()
        black.add(intArrayOf(3, 2))
        black.add(intArrayOf(3, 3))
        black.add(intArrayOf(3, 4))
        black.add(intArrayOf(4, 2))
        black.add(intArrayOf(4, 4))
        black.add(intArrayOf(5, 2))
        black.add(intArrayOf(5, 3))
        black.add(intArrayOf(5, 4))

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(2, 1))
        white.add(intArrayOf(2, 3))
        white.add(intArrayOf(2, 5))
        white.add(intArrayOf(4, 1))
        white.add(intArrayOf(4, 5))
        white.add(intArrayOf(6, 1))
        white.add(intArrayOf(6, 3))
        white.add(intArrayOf(6, 5))

        val board = Array(game.boardSize) { CharArray(game.boardSize) }
        black.map { array -> board[array[0]][array[1]] = WHITE }
        white.map { array -> board[array[0]][array[1]] = WHITE }
        board[4][3] = WHITE

        game.setNewPosition(Position(white, black))
        game.setMoveWhite()
        game.makeMove(4, 3)
        assertEqualChips(game.position, board)
    }

    @Test
    fun white_reverseManyChipsInRowsAndColumns() {
        val black = ArrayList<IntArray>()
        black.add(intArrayOf(1, 4))
        black.add(intArrayOf(2, 4))
        black.add(intArrayOf(3, 4))
        black.add(intArrayOf(5, 4))
        black.add(intArrayOf(6, 4))
        black.add(intArrayOf(4, 1))
        black.add(intArrayOf(4, 2))
        black.add(intArrayOf(4, 3))
        black.add(intArrayOf(4, 5))
        black.add(intArrayOf(4, 6))

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(4, 0))
        white.add(intArrayOf(4, 7))
        white.add(intArrayOf(0, 4))
        white.add(intArrayOf(7, 4))

        val board = Array(game.boardSize) { CharArray(game.boardSize) }
        black.map { array -> board[array[0]][array[1]] = WHITE }
        white.map { array -> board[array[0]][array[1]] = WHITE }
        board[4][4] = WHITE

        game.setNewPosition(Position(white, black))
        game.setMoveWhite()
        game.makeMove(4, 4)
        assertEqualChips(game.position, board)
    }

    @Test
    fun white_reverseManyChipsInDiagonals() {
        val black = ArrayList<IntArray>()
        black.add(intArrayOf(1, 1))
        black.add(intArrayOf(2, 2))
        black.add(intArrayOf(3, 3))
        black.add(intArrayOf(5, 5))
        black.add(intArrayOf(6, 6))
        black.add(intArrayOf(6, 2))
        black.add(intArrayOf(5, 3))
        black.add(intArrayOf(3, 5))
        black.add(intArrayOf(2, 6))

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(0, 0))
        white.add(intArrayOf(7, 1))
        white.add(intArrayOf(1, 7))
        white.add(intArrayOf(7, 7))

        val board = Array(game.boardSize) { CharArray(game.boardSize) }
        black.map { array -> board[array[0]][array[1]] = WHITE }
        white.map { array -> board[array[0]][array[1]] = WHITE }
        board[4][4] = WHITE

        game.setNewPosition(Position(white, black))
        game.setMoveWhite()
        game.makeMove(4, 4)
        assertEqualChips(game.position, board)
    }

    @Test
    fun white_BlackChipsAfterWhiteWontReverse() {
        val black = ArrayList<IntArray>()
        black.add(intArrayOf(1, 1))
        black.add(intArrayOf(3, 3))
        black.add(intArrayOf(4, 4))
        black.add(intArrayOf(1, 5))
        black.add(intArrayOf(3, 5))
        black.add(intArrayOf(4, 5))
        black.add(intArrayOf(6, 5))

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(0, 0))
        white.add(intArrayOf(2, 2))
        white.add(intArrayOf(0, 5))
        white.add(intArrayOf(2, 5))
        white.add(intArrayOf(7, 5))

        val board = Array(game.boardSize) { CharArray(game.boardSize) }
        (2 until game.boardSize).map { i -> board[i][5] = WHITE }
        board[0][5] = WHITE
        board[2][2] = WHITE
        board[0][0] = WHITE
        board[3][3] = WHITE
        board[4][4] = WHITE
        board[1][5] = BLACK
        board[1][1] = BLACK
        game.setNewPosition(Position(white, black))
        game.setMoveWhite()
        game.makeMove(5, 5)
        assertEqualChips(game.position, board)
    }

    @Test
    fun white_reverseFromLeftUpperCorner() {
        val black = ArrayList<IntArray>()
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(i, 0)) }
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(0, i)) }
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(i, i)) }

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(0, 7))
        white.add(intArrayOf(7, 0))
        white.add(intArrayOf(7, 7))

        val board = Array(game.boardSize) { CharArray(game.boardSize) }
        black.map { array -> board[array[0]][array[1]] = WHITE }
        white.map { array -> board[array[0]][array[1]] = WHITE }
        board[0][0] = WHITE

        game.setNewPosition(Position(white, black))
        game.setMoveWhite()
        game.makeMove(0, 0)
        assertEqualChips(game.position, board)
    }

    @Test
    fun white_reverseFromRightUpperCorner() {
        val black = ArrayList<IntArray>()
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(i, game.boardSize - 1)) }
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(0, i)) }
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(i, game.boardSize - 1 - i)) }

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(7, 7))
        white.add(intArrayOf(0, 0))
        white.add(intArrayOf(7, 0))

        val board = Array(game.boardSize) { CharArray(game.boardSize) }
        black.map { array -> board[array[0]][array[1]] = WHITE }
        white.map { array -> board[array[0]][array[1]] = WHITE }
        board[0][7] = WHITE

        game.setNewPosition(Position(white, black))
        game.setMoveWhite()
        game.makeMove(0, 7)
        assertEqualChips(game.position, board)
    }

    @Test
    fun white_reverseFromRightLowerCorner() {
        val black = ArrayList<IntArray>()
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(i, game.boardSize - 1)) }
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(game.boardSize - 1, i)) }
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(i, i)) }

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(0, 7))
        white.add(intArrayOf(7, 0))
        white.add(intArrayOf(0, 0))

        val board = Array(game.boardSize) { CharArray(game.boardSize) }
        black.map { array -> board[array[0]][array[1]] = WHITE }
        white.map { array -> board[array[0]][array[1]] = WHITE }
        board[7][7] = WHITE

        game.setNewPosition(Position(white, black))
        game.setMoveWhite()
        game.makeMove(7, 7)
        assertEqualChips(game.position, board)
    }

    @Test
    fun white_reverseFromLeftLowerCorner() {
        val black = ArrayList<IntArray>()
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(i, 0)) }
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(game.boardSize - 1, i)) }
        (1 until game.boardSize - 1).map { i -> black.add(intArrayOf(i, game.boardSize - 1 - i)) }

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(0, 0))
        white.add(intArrayOf(7, 7))
        white.add(intArrayOf(0, 7))

        val board = Array(game.boardSize) { CharArray(game.boardSize) }
        black.map { array -> board[array[0]][array[1]] = WHITE }
        white.map { array -> board[array[0]][array[1]] = WHITE }
        board[7][0] = WHITE

        game.setNewPosition(Position(white, black))
        game.setMoveWhite()
        game.makeMove(7, 0)
        assertEqualChips(game.position, board)
    }
}