package algie.parvin.othello

import algie.parvin.othello.db.Puzzle
import algie.parvin.othello.model.*
import io.reactivex.Flowable
import org.junit.Test
import org.junit.Assert.*


class ModelGameChipReverseUnitTest {

    private fun mockRepository(puzzleList: List<Puzzle>) : DBRepository {
       return object : DBRepository {
           override fun getPuzzle(id: Int): Flowable<Puzzle> {
               return Flowable.just(puzzleList[0])
           }

           override fun updatePuzzle(puzzle: Puzzle) { }

            override fun getAllPuzzles(): Flowable<List<Puzzle>> {
                return Flowable.just(puzzleList)
            }
        }
    }

    private fun assertEqualChips(boardFromGame: Array<Array<Chip>>, boardExpected: Array<Array<Chip>>) {
        assertEquals(boardExpected.size, boardFromGame.size)
        boardExpected.indices.map {
                row -> assertArrayEquals(boardExpected[row], boardFromGame[row])
        }
    }

    private fun testRealBoardIsEqualToTestBoard(
        white: List<IntArray>, black: List<IntArray>, rowMove: Int, columnMove: Int, move: Chip
    ) {
        val puzzleList = listOf(Puzzle(1, 8, black, white, false, ""))
        val repo = mockRepository(puzzleList)
        val board = Array(puzzleList[0].boardSize) { Array(puzzleList[0].boardSize) { Chip.NONE } }
        white.map { array -> board[array[0]][array[1]] = move }
        black.map { array -> board[array[0]][array[1]] = move }
        board[rowMove][columnMove] = move

        val game = Game(boardSize = puzzleList[0].boardSize, repository = repo)

        game.setNewPosition(repo.getPuzzle(0).blockingFirst())
        if (move == Chip.BLACK) game.setMoveBlack() else game.setMoveWhite()
        game.makePlayerMove(rowMove, columnMove)
        assertEqualChips(game.puzzle.position, board)
    }

/*
        B  B  B
         W W W
       B W * W B
         W W W
        B  B  B
*/
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

        testRealBoardIsEqualToTestBoard(white, black, 4, 3, Chip.BLACK)
    }

/*
        W  W  W
         B B B
       W B * B W
         B B B
        W  W  W
 */
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

        testRealBoardIsEqualToTestBoard(white, black, 4, 3, Chip.WHITE)
    }

/*
               W
               B
               B
               B
       W B B B * B B W
               B
               B
               W
 */
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

        testRealBoardIsEqualToTestBoard(white, black, 4, 4, Chip.WHITE)
    }

/*
       W
         B           W
           B       B
             B   B
               *
             B   B
           B       B
         W           W
*/
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

        testRealBoardIsEqualToTestBoard(white, black, 4, 4, Chip.WHITE)
    }

/*
       W         W
         B       B
           W     W
             B   B
               B B
                 *
                 B
                 W
 */
    @Test
    fun white_BlackChipsAfterWhiteWontReverse() {
        val boardSize = 8
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

        val board = Array(boardSize) { Array(boardSize) { Chip.NONE } }
        (2 until boardSize).map { i -> board[i][5] = Chip.WHITE }
        board[0][5] = Chip.WHITE
        board[2][2] = Chip.WHITE
        board[0][0] = Chip.WHITE
        board[3][3] = Chip.WHITE
        board[4][4] = Chip.WHITE
        board[1][5] = Chip.BLACK
        board[1][1] = Chip.BLACK

        val puzzle = Puzzle(1, 8, black, white, false, "")
        val puzzleList = listOf(puzzle)
        val repo = mockRepository(puzzleList)

        val game = Game(boardSize = boardSize, repository = repo)
        game.setNewPosition(puzzle)
        game.makePlayerMove(5, 5)
        assertEqualChips(game.puzzle.position, board)
    }

/*
       * B B B B B B W
       B B
       B   B
       B     B
       B       B
       B         B
       B           B
       W             W
 */
    @Test
    fun white_reverseFromLeftUpperCorner() {
        val boardSize = 8
        val black = ArrayList<IntArray>()
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(i, 0)) }
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(0, i)) }
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(i, i)) }

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(0, 7))
        white.add(intArrayOf(7, 0))
        white.add(intArrayOf(7, 7))

        testRealBoardIsEqualToTestBoard(white, black, 0, 0, Chip.WHITE)
    }

/*
       W B B B B B B *
                   B B
                 B   B
               B     B
             B       B
           B         B
         B           B
       W             W
 */
    @Test
    fun white_reverseFromRightUpperCorner() {
        val boardSize = 8
        val black = ArrayList<IntArray>()
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(i, boardSize - 1)) }
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(0, i)) }
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(i, boardSize - 1 - i)) }

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(7, 7))
        white.add(intArrayOf(0, 0))
        white.add(intArrayOf(7, 0))

        testRealBoardIsEqualToTestBoard(white, black, 0, 7, Chip.WHITE)
    }

/*
       * B B B B B B W
       B B
       B   B
       B     B
       B       B
       B         B
       B           B
       W             W
*/
    @Test
    fun white_reverseFromRightLowerCorner() {
        val boardSize = 8
        val black = ArrayList<IntArray>()
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(i, boardSize - 1)) }
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(boardSize - 1, i)) }
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(i, i)) }

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(0, 7))
        white.add(intArrayOf(7, 0))
        white.add(intArrayOf(0, 0))

        testRealBoardIsEqualToTestBoard(white, black, 7, 7, Chip.WHITE)
    }

/*
   W             W
     B           B
       B         B
         B       B
           B     B
             B   B
               B B
   W B B B B B B *
*/
    @Test
    fun white_reverseFromLeftLowerCorner() {
        val boardSize = 8
        val black = ArrayList<IntArray>()
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(i, 0)) }
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(boardSize - 1, i)) }
        (1 until boardSize - 1).map { i -> black.add(intArrayOf(i, boardSize - 1 - i)) }

        val white = ArrayList<IntArray>()
        white.add(intArrayOf(0, 0))
        white.add(intArrayOf(7, 7))
        white.add(intArrayOf(0, 7))

        testRealBoardIsEqualToTestBoard(white, black, 7, 0, Chip.WHITE)
    }
}