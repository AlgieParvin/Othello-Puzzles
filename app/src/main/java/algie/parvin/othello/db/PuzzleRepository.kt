package algie.parvin.othello.db

import algie.parvin.othello.model.DBRepository
import android.app.Application
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PuzzleRepository(app: Application) : DBRepository {
    private val dao: PuzzleDao

    init {
        val database =
            AppDatabase.getInstance(app.applicationContext)
        dao = database.puzzleDao()
    }

    override fun getPuzzle(id: Int): Single<Puzzle> {
        return dao.getPuzzle(id)
    }

    override fun getDefaultPuzzle(): Single<Puzzle> {
        return dao.getDefaultPuzzle()
    }

    override fun updatePuzzle(puzzle: Puzzle) {
        Completable.fromAction { dao.insert(puzzle) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}