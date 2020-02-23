package algie.parvin.othello.model.db

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Puzzle::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun puzzleDao(): PuzzleDao

    private class PopulateDbAsyncTask(val dbInstance: AppDatabase) : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg params: Unit?) {
            dbInstance.puzzleDao()
                .insert(Puzzle(1, 8, listOf(intArrayOf(3, 3), intArrayOf(4, 4)),
                    listOf(intArrayOf(3, 4), intArrayOf(4, 3)), false))
        }

    }

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) : AppDatabase {
            val db = Room.databaseBuilder(context, AppDatabase::class.java, "puzzle.db")
                .fallbackToDestructiveMigration()
//                .addCallback(object : RoomDatabase.Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        instance ?: PopulateDbAsyncTask(instance!!).execute()
//                    }
//                })
                .allowMainThreadQueries()
                .build()

//                db.puzzleDao().insert(
//                    Puzzle(1, 8, listOf(intArrayOf(1, 1), intArrayOf(2, 2)), listOf(intArrayOf(1, 2), intArrayOf(2, 1)), false))
                return db
        }
    }
}