package algie.parvin.othello.model

import kotlin.math.min

class VariantsTree {

    private lateinit var root: Move

    fun resetTree() {
        while (root.previous != null) {
            root = root.previous!!
        }
    }

    fun hasMoreMoves(): Boolean {
        return root.next != null
    }

    fun onPlayerMove(playerMove: IntArray): IntArray? {
        if (root.next == null || root.next!!.isEmpty()) {
            return null
        }

        var found = false
        for (m in root.next!!) {
            if (m.move[0] == playerMove[0] && m.move[1] == playerMove[1]) {
                root = m
                found = true
                break
            }
        }

        if (!found) {
            for (m in root.next!!) {
                if (m.move[0] == -1 && m.move[1] == -1) {
                    root = m
                    break
                }
            }
        }

        return root.responseMove
    }

    fun buildFromString(variants: String) {
        root = Move(
            intArrayOf(),
            intArrayOf()
        )

        var index = 0
        while (index < variants.length) {
            if (variants[index] == '(') {
                val endOfMove = variants.substring(index + 1).indexOf(')')
                val nextMove = variants.substring(index + 1).indexOf('(')
                var end = min(endOfMove, nextMove)
                if (nextMove == -1) {
                    end = endOfMove
                }
                addMoveToTree(variants, index + 1, end)
                index = index + end - 1
            }
            else if (variants[index] == ')') {
                root = root.previous!!
            }
            index++
        }
    }

    private fun addMoveToTree(variants: String, start: Int, end: Int) {
        if (root.next == null) {
            root.next = mutableListOf()
        }
        val moves = variants.substring(start, start + end).split('-')

        val opponentMove = moves[1].split(',')
        val opponentMoveRow = opponentMove[0].toInt()
        val opponentMoveColumn = opponentMove[1].toInt()

        val playerMove = moves[0].split(',')
        if (playerMove[0].startsWith('*')) {
            root.next!!.add(0, Move(
                intArrayOf(-1, -1), intArrayOf(opponentMoveRow, opponentMoveColumn), previous=root
            ))
        }
        else {
            val playerMoveRow = playerMove[0].toInt()
            val playerMoveColumn = playerMove[1].toInt()
            root.next!!.add(0, Move(
                intArrayOf(playerMoveRow, playerMoveColumn),
                intArrayOf(opponentMoveRow, opponentMoveColumn),
                previous=root
            ))
        }

        root = root.next!![0]
    }
}