package algie.parvin.othello.db

import algie.parvin.othello.model.Chip
import algie.parvin.othello.model.Field
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
            val puzzles = listOf(
                Puzzle(
                    1,
                    8,
                    listOf(Field(6, 4, Chip.BLACK), Field(6, 6, Chip.BLACK),
                        Field(7, 6, Chip.BLACK), Field(4, 7, Chip.BLACK),
                        Field(5, 7, Chip.BLACK), Field(6, 7, Chip.BLACK),
                        Field(3, 3, Chip.WHITE), Field(5, 3, Chip.WHITE),
                        Field(7, 3, Chip.WHITE), Field(5, 5, Chip.WHITE),
                        Field(1, 7, Chip.WHITE), Field(3, 7, Chip.WHITE)),
                    true,
                    "(7,7-4,2)(7,5-2,7)",
                    2
                ),
                Puzzle(
                    2,
                    8,
                    listOf(Field(4, 2, Chip.BLACK), Field(1, 3, Chip.BLACK),
                        Field(2, 4, Chip.BLACK), Field(4, 4, Chip.BLACK),
                        Field(5, 3, Chip.WHITE), Field(3, 5, Chip.WHITE),
                        Field(5, 7, Chip.WHITE)),
                    false,
                    "(3,1-6,2(7,1-4,6)(0,2-2,0)(6,4-4,6))(0,2-6,2(7,1-6,4))",
                    3
                ),
                Puzzle(
                    3,
                    8,
                    listOf(Field(3, 4, Chip.BLACK), Field(1, 6, Chip.BLACK),
                        Field(3, 6, Chip.BLACK), Field(5, 7, Chip.BLACK),
                        Field(4, 3, Chip.WHITE), Field(2, 5, Chip.WHITE),
                        Field(1,7, Chip.WHITE),Field(2, 7, Chip.WHITE),
                        Field(4, 7, Chip.WHITE)),
                    false,
                    "(0,7-3,7(6,7-1,4(0,3-5,2)(4,5-5,6))(4,5-1,4(6,7-5,6)(2,3-3,2)))",
                    4
                ),
                Puzzle(
                    4,
                    8,
                    listOf(Field(1, 1, Chip.BLACK), Field(1, 2, Chip.BLACK),
                        Field(2, 4, Chip.BLACK), Field(1, 6, Chip.BLACK),
                        Field(1, 0, Chip.WHITE), Field(1, 7, Chip.WHITE),
                        Field(2, 5, Chip.WHITE), Field(4, 5, Chip.WHITE),
                        Field(2, 7, Chip.WHITE)),
                    false,
                    "(1,3-0,2(2,3-3,5(*-2,0))" +
                                    "(1,4-3,4(*-2,0))" +
                                    "(0,5-2,6(*-2,0))" +
                                    "(1,5-2,2(*-2,0))" +
                                    "(0,7-2,2(*-2,0)))" +
                            "(2,3-3,4(1,3-0,1(*-1,4))" +
                                    "(2,2-2,1(0,1-4,3)(*-5,6))" +
                                    "(0,1-2,2(3,2-1,3)(*-0,2))" +
                                    "(4,4-4,3(0,1-5,4)(*-5,6))" +
                                    "(2,6-3,6(4,7-4,6)(*-5,4))" +
                                    "(1,5-0,7(*-3,7))" +
                                    "(0,5-0,7(*-3,7)))" +
                            "(0,5-2,6(2,3-3,4(0,1-1,3)(*-5,6))" +
                                    "(3,4-4,7(4,3-4,6)(2,3-5,6)(*-0,7))" +
                                    "(*-0,7(*-3,7)))",
                    4
                )
            )
            Completable.fromAction {
                puzzles.forEach { db.puzzleDao().insert(it) }
            }
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