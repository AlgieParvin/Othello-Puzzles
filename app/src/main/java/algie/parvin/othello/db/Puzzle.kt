package algie.parvin.othello.db

import algie.parvin.othello.model.BLACK
import algie.parvin.othello.model.WHITE
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class Puzzle(
    @PrimaryKey var id: Int,
    var boardSize: Int,
    var blackChips: List<IntArray>,
    var whiteChips: List<IntArray>,
    var solved: Boolean,
    var variants: String) {

    fun getInitialPosition(): Array<CharArray> {
        val position = Array(boardSize) { CharArray(boardSize) }
        blackChips.forEach { position[it[0]][it[1]] = BLACK }
        whiteChips.forEach { position[it[0]][it[1]] = WHITE }
        return position
    }

    @Ignore
    lateinit var position: Array<CharArray>
}