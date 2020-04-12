package algie.parvin.othello.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

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
    fun getAllPuzzles(): Single<List<Puzzle>>

    @Query("SELECT * FROM puzzle where id = :id")
    fun getPuzzle(id: Int): Single<Puzzle>

    @Query("SELECT * FROM puzzle WHERE id=(SELECT MIN(id) FROM puzzle WHERE solved=0)")
    fun getDefaultPuzzle(): Single<Puzzle>
}