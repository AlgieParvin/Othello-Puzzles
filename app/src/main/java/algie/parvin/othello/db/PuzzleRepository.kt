package algie.parvin.othello.db

import algie.parvin.othello.model.DBRepository
import android.app.Application
import android.os.AsyncTask
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class PuzzleRepository(app: Application) : DBRepository {
    private val dao: PuzzleDao
    private val puzzles: Flowable<List<Puzzle>>

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

    override fun getAllPuzzles(): Flowable<List<Puzzle>> {
        return puzzles
    }

    override fun getPuzzle(id: Int): Flowable<Puzzle> {
        return dao.getPuzzle(id)
    }

    override fun updatePuzzle(puzzle: Puzzle) {
        Completable.fromAction { dao.insert(puzzle) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

}