package algie.parvin.othello.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun stringToArray(chipsString: String) : List<IntArray> {
        val list = mutableListOf<IntArray>()
        (0 until chipsString.length - 1 step 2).forEach {
            val row = Character.getNumericValue(chipsString[it])
            val column = Character.getNumericValue(chipsString[it + 1])
            list.add(intArrayOf(row, column))
        }
        return list
    }

    @TypeConverter
    fun arrayToString(chipsList: List<IntArray>) : String {
        val sb = StringBuilder()
        chipsList.forEach { sb.append(it[0]); sb.append(it[1]) }
        return sb.toString()
    }
}