package algie.parvin.othello.db

import algie.parvin.othello.model.Chip
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
    var variants: String,
    val movesCounter: Int = 1) : Cloneable {

    fun getInitialPosition(): Array<Array<Chip>> {
        val position = Array(boardSize) { Array(boardSize) { Chip.NONE} }
        blackChips.forEach { position[it[0]][it[1]] = Chip.BLACK }
        whiteChips.forEach { position[it[0]][it[1]] = Chip.WHITE }
        return position
    }

    @Ignore lateinit var position: Array<Array<Chip>>
    @Ignore var movesLeft: Int = movesCounter

    public override fun clone(): Any {
        return Puzzle(
            -1,
            boardSize,
            blackChips.map { it.copyOf() },
            whiteChips.map { it.copyOf() },
            solved,
            variants,
            movesCounter
        ).also { p ->
            p.position = this.position.map { m -> m.copyOf() }.toTypedArray()
            p.movesLeft = movesLeft
        }
    }
}