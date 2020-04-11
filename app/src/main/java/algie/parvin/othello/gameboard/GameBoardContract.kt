package algie.parvin.othello.gameboard

class GameBoardContract {

    interface PresenterInterface {
        fun getBoardSize(): Int
        fun handlePlayerMove(square: Int)
        fun loadPuzzle(id: Int)
        fun receiveOpponentMove()
    }

    interface ViewInterface {
        fun setChipsOnBoard(
            white: List<Int>,
            black: List<Int>,
            freezeBoard: Boolean
        )
        fun animateBoardCreation()
        fun reverseChips(chipIndices: List<Int>, reverseToWhite: Boolean)
    }
}