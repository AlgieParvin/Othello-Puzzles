package algie.parvin.othello.gameboard

class GameBoardContract {

    interface PresenterInterface {
        fun getBoardSize(): Int
        fun handleMove(square: Int)
        fun loadPuzzles()
    }

    interface ViewInterface {
        fun setChipsOnBoard(white: List<Int>, black: List<Int>)
        fun animateBoardCreation()
        fun reverseChips(chipIndices: List<Int>, reverseToWhite: Boolean)
    }
}