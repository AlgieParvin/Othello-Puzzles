package algie.parvin.othello.model

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

        for (m in root.next!!) {
            if (m.move[0] == playerMove[0] && m.move[1] == playerMove[1]) {
                root = m
                break
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
                if (root.next == null) {
                    root.next = mutableListOf()
                }
                root.next!!.add(0, Move(
                    intArrayOf(
                        Character.getNumericValue(variants[index + 1]),
                        Character.getNumericValue(variants[index + 2])
                    ),
                    intArrayOf(
                        Character.getNumericValue(variants[index + 4]),
                        Character.getNumericValue(variants[index + 5])
                    ),
                    previous = root)
                )
                index += 6
                root = root.next!!.get(0)
            }

            else if (variants[index] == ')') {
                index++
                root = root.previous!!
            } else {
                index++
            }

        }
    }
}