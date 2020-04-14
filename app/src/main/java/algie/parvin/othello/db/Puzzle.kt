package algie.parvin.othello.db

import algie.parvin.othello.model.Chip
import algie.parvin.othello.model.Field
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class Puzzle(
    @PrimaryKey var id: Int,
    var boardSize: Int,
    var chips: List<Field>,
    var opened: Boolean,
    var variants: String,
    val movesCounter: Int = 1) : Cloneable {

    fun getInitialPosition(): Array<Array<Chip>> {
        val position = Array(boardSize) { Array(boardSize) { Chip.NONE} }
        chips.filter { field -> field.chip == Chip.WHITE }
            .forEach { position[it.row][it.column] = Chip.WHITE }
        chips.filter { field -> field.chip == Chip.BLACK }
            .forEach { position[it.row][it.column] = Chip.BLACK }
        return position
    }

    @Ignore lateinit var position: Array<Array<Chip>>
    @Ignore var movesLeft: Int = movesCounter

    public override fun clone(): Any {
        return Puzzle(
            -1,
            boardSize,
            chips,
            opened,
            variants,
            movesCounter
        ).also { p ->
            p.position = this.position.map { m -> m.copyOf() }.toTypedArray()
            p.movesLeft = movesLeft
        }
    }
}