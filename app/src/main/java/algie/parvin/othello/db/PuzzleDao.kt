package algie.parvin.othello.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface PuzzleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(puzzle: Puzzle)

    @Delete
    fun delete(puzzle: Puzzle)

    @Query("UPDATE puzzle SET opened = :opened WHERE id = :id")
    fun update(opened: Boolean, id: Int)

    @Query("DELETE FROM puzzle")
    fun deleteAllPuzzles()

    @Query("SELECT * FROM puzzle")
    fun getAllPuzzles(): Single<List<Puzzle>>

    @Query("SELECT MAX(id) FROM puzzle")
    fun getMaxId(): Single<Int>

    @Query("SELECT * FROM puzzle where id = :id")
    fun getPuzzle(id: Int): Single<Puzzle>

    @Query("SELECT * FROM puzzle WHERE id=(SELECT MAX(id) FROM puzzle WHERE opened=1)")
    fun getDefaultPuzzle(): Single<Puzzle>
}