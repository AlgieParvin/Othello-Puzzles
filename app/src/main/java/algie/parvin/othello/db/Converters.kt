package algie.parvin.othello.db

import algie.parvin.othello.model.Chip
import algie.parvin.othello.model.Field
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun stringToArray(chipsString: String) : List<Field> {
        val list = mutableListOf<Field>()
        (0 until chipsString.length - 2 step 3).forEach {
            val row = Character.getNumericValue(chipsString[it])
            val column = Character.getNumericValue(chipsString[it + 1])
            val chip = if (chipsString[it + 2] == 'W') Chip.WHITE else Chip.BLACK
            list.add(Field(row, column, chip))
        }
        return list
    }

    @TypeConverter
    fun arrayToString(chipsList: List<Field>) : String {
        val sb = StringBuilder()
        chipsList.forEach {
            sb.append(it.row);
            sb.append(it.column);
            if (it.chip == Chip.BLACK) {
                sb.append('B')
            } else if (it.chip == Chip.WHITE) {
                sb.append('W')
            }
        }
        return sb.toString()
    }
}