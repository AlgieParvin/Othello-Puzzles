package algie.parvin.othello.puzzlelist

import algie.parvin.othello.model.DBRepository

class PuzzleListContract {

    interface  PresenterInterface {
        fun onLoadPuzzleGrid()
    }

    interface ViewInterface {
        fun provideRepository(): DBRepository
        fun initializePuzzleGrid(maxId: Int, openedThreshold: Int)
    }
}