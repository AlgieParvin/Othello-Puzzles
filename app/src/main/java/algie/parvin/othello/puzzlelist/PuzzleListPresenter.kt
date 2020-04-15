package algie.parvin.othello.puzzlelist

import algie.parvin.othello.db.Puzzle
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers


class PuzzleListPresenter(private val view: PuzzleListContract.ViewInterface):
    PuzzleListContract.PresenterInterface {

    private val repository = view.provideRepository()

    private lateinit var puzzlesListDisposable: Disposable

    override fun onLoadPuzzleGrid() {
        puzzlesListDisposable = Single.zip(
            repository.getMaxId(),
            repository.getDefaultPuzzle(),
            BiFunction<Int, Puzzle, IntArray> { maxId, defaultPuzzle ->
                intArrayOf(maxId, defaultPuzzle.id)
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response -> view.initializePuzzleGrid(response[0], response[1]) }
    }
}