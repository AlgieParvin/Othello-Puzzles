package algie.parvin.othello.db

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface PuzzleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(puzzle: Puzzle)

    @Delete
    fun delete(puzzle: Puzzle)

    @Update
    fun update(puzzle: Puzzle)

    @Query("DELETE FROM puzzle")
    fun deleteAllPuzzles()

    @Query("SELECT * FROM puzzle")
    fun getAllPuzzles(): Flowable<List<Puzzle>>
}