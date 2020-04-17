package algie.parvin.othello.gameboard

import algie.parvin.othello.model.Field
import androidx.lifecycle.LiveData

class GameBoardContract {

    interface PresenterInterface {
        fun getBoardSize(): Int
        fun handlePlayerMove(square: Int)
        fun receiveOpponentMove()
        fun getMovesObservable(): LiveData<Int>

        fun loadPuzzle(id: Int)
        fun loadDefaultPuzzle()
        fun loadNextPuzzle()
        fun resetPuzzle()
    }

    interface ViewInterface {
        fun setChipsOnBoard(
            white: List<Int>,
            black: List<Int>,
            freezeBoard: Boolean
        )
        fun reverseChips(chipIndices: List<Int>, reverseToWhite: Boolean)
        fun setMovesCounter(moves: Int)

        fun showNewPuzzle(chipsByRowsAndColumns: List<List<Field>>, boardSize: Int)

        fun onPlayerWin()
        fun onPlayerLose()
    }
}