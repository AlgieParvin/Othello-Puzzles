package algie.parvin.othello.gameboard

import androidx.lifecycle.LiveData

class GameBoardContract {

    interface PresenterInterface {
        fun getBoardSize(): Int
        fun handlePlayerMove(square: Int)
        fun loadPuzzle(id: Int)
        fun receiveOpponentMove()
        fun getMovesObservable(): LiveData<Int>
    }

    interface ViewInterface {
        fun setChipsOnBoard(
            white: List<Int>,
            black: List<Int>,
            freezeBoard: Boolean
        )
        fun animateBoardCreation()
        fun reverseChips(chipIndices: List<Int>, reverseToWhite: Boolean)
        fun onPlayerWin()
        fun onPlayerLose()
        fun setMovesCounter(moves: Int)
    }
}