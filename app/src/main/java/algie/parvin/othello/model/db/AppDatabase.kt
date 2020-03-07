package algie.parvin.othello.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers


@Database(entities = [Puzzle::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun puzzleDao(): PuzzleDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun initializeDatabase(db: AppDatabase) {
            val puzzle = Puzzle(
                1,
                8,
                listOf(intArrayOf(3, 1), intArrayOf(2, 2)),
                listOf(intArrayOf(1, 2), intArrayOf(2, 1)),
                false)
            Completable.fromAction {db.puzzleDao().insert(puzzle) }
                .subscribeOn(Schedulers.io())
                .subscribe()
        }

        private fun buildDatabase(context: Context) : AppDatabase {
            val db = Room.databaseBuilder(context, AppDatabase::class.java, "puzzle.db")
                .fallbackToDestructiveMigration()
                .build()
            initializeDatabase(db)
            return db
        }
    }
}