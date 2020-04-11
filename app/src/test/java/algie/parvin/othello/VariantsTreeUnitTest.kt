package algie.parvin.othello

import algie.parvin.othello.model.VariantsTree
import org.junit.Assert.assertEquals
import org.junit.Test


class VariantsTreeUnitTest {

    private val variants = "(3,1-6,2(7,1-4,6)(0,2-2,0)(6,4-4,6))(0,2-6,2(7,1-6,4))"

    @Test
    fun buildTreeCalls_treeBuildWithoutErrors() {
        val tree = VariantsTree()
        tree.buildFromString(variants)
    }

    private fun checkMoves(
        tree: VariantsTree,
        playerMoves: List<IntArray>,
        responseMoves: List<IntArray>
    ) {
        for (i in playerMoves.indices) {
            val responseMove = tree.onPlayerMove(playerMoves[i])
            assertEquals(responseMove!![0], responseMoves[i][0])
            assertEquals(responseMove[1], responseMoves[i][1])
        }
    }

    @Test
    fun whenValidMoveSendToTree_CorrectResponseMoveReturns() {
        val tree = VariantsTree()
        tree.buildFromString(variants)

        val playerMovesCollection = listOf(
            listOf(intArrayOf(3, 1), intArrayOf(7, 1)),
            listOf(intArrayOf(3, 1), intArrayOf(0, 2)),
            listOf(intArrayOf(3, 1), intArrayOf(6, 4)),
            listOf(intArrayOf(0, 2), intArrayOf(7, 1))
        )

        val responseMovesCollection = listOf(
            listOf(intArrayOf(6, 2), intArrayOf(4, 6)),
            listOf(intArrayOf(6, 2), intArrayOf(2, 0)),
            listOf(intArrayOf(6, 2), intArrayOf(4, 6)),
            listOf(intArrayOf(6, 2), intArrayOf(6, 4))
        )

        for (i in playerMovesCollection.indices) {
            tree.resetTree()
            checkMoves(tree, playerMovesCollection[i], responseMovesCollection[i])
        }
    }
}