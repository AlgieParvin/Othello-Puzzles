package algie.parvin.othello.model.db

import algie.parvin.othello.model.DBRepository
import android.app.Application
import android.os.AsyncTask

class PuzzleRepository(app: Application) : DBRepository {
    private val dao: PuzzleDao
    private val puzzles: List<Puzzle>

    private inner class UpdatePuzzleAsyncTask : AsyncTask<Puzzle, Unit, Unit>() {
        override fun doInBackground(vararg params: Puzzle) {
            params[0].let { dao.update(params[0]) }
        }
    }

    init {
        val database =
            AppDatabase.getInstance(app.applicationContext)
        dao = database.puzzleDao()
        puzzles = dao.getAllPuzzles()
    }

    override fun getAllPuzzles(): List<Puzzle> {
        return puzzles
    }

    override fun updatePuzzle(puzzle: Puzzle) {
        UpdatePuzzleAsyncTask().execute()
    }

}