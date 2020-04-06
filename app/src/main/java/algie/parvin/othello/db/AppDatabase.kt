package algie.parvin.othello.db

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
                listOf(intArrayOf(4, 2), intArrayOf(1, 3), intArrayOf(2, 4), intArrayOf(4, 4)),
                listOf(intArrayOf(5, 3), intArrayOf(3, 5), intArrayOf(5, 7)),
                false,
                "(31-62(71-46)(02-20)(64-46))(02-62(71-64))"
            )
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