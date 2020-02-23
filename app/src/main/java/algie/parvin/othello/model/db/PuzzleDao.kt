package algie.parvin.othello.model.db

import androidx.room.*

@Dao
interface PuzzleDao {

    @Insert
    fun insert(puzzle: Puzzle)

    @Delete
    fun delete(puzzle: Puzzle)

    @Update
    fun update(puzzle: Puzzle)

    @Query("DELETE FROM puzzle")
    fun deleteAllPuzzles()

    @Query("SELECT * FROM puzzle")
    fun getAllPuzzles(): List<Puzzle>
}