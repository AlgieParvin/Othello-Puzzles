package algie.parvin.othello.gameboard

import algie.parvin.othello.R
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.GridLayout
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat


class GameBoardActivity : AppCompatActivity(), GameBoardContract.ViewInterface {

    private lateinit var presenter: GameBoardContract.PresenterInterface
    private var animateBoard = true
    private var freezeBoard = false

    private fun initializedSquaresOnGrid() {
        val boardSize = presenter.getBoardSize()
        board.columnCount = boardSize
        board.rowCount = boardSize

        for (i in 0 until boardSize * boardSize) {
            var view = ImageView(this)
            board.addView(view, i)
            val param = GridLayout.LayoutParams(
                GridLayout.spec(
                    GridLayout.UNDEFINED, GridLayout.FILL, 1f
                ),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            )
            param.width = 0
            param.height = 0
            view.layoutParams = param
        }
    }

    override fun setChipsOnBoard(
        white: List<Int>,
        black: List<Int>,
        freezeBoard: Boolean
    ) {
        black.map {
            (board.getChildAt(it) as ImageView).setImageResource(R.drawable.black_to_white_avd) }
        white.map {
            (board.getChildAt(it) as ImageView).setImageResource(R.drawable.white_to_black_avd) }
        this.freezeBoard = freezeBoard
    }

    private fun removeActionAndStatusBars() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }

    private fun attachListenersToSquares() {
        for (i in 0 until board.childCount) {
            board.getChildAt(i).setOnClickListener {
                if (!freezeBoard) {
                    presenter.handlePlayerMove(i)
                }
            }
        }
    }

    private fun reverseChip(index: Int, changeToWhite: Boolean) {
        if (changeToWhite) {
            (board.getChildAt(index) as ImageView).setImageResource(R.drawable.black_to_white_avd)
        } else {
            (board.getChildAt(index) as ImageView).setImageResource(R.drawable.white_to_black_avd)
        }

        val drawable = (board.getChildAt(index) as ImageView).drawable
        if (drawable is AnimatedVectorDrawableCompat) {
            drawable.start()
        }
        else if (drawable is AnimatedVectorDrawable) {
            drawable.start()
        }
    }

    override fun animateBoardCreation() {
        val drawable = board.background
        if (drawable is AnimatedVectorDrawableCompat) {
            drawable.start()
        }
        else if (drawable is AnimatedVectorDrawable) {
            drawable.start()
        }
    }

    override fun reverseChips(chipIndices: List<Int>, reverseToWhite: Boolean) {
        chipIndices.map { i -> reverseChip(i, reverseToWhite) }
        if (reverseToWhite) {
            Handler().postDelayed({
                presenter.receiveOpponentMove()
                freezeBoard = true
            }, 800)
        } else {
            Handler().postDelayed({
                freezeBoard = false
            }, 750)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = GameBoardPresenter(this, application)

        removeActionAndStatusBars()
        initializedSquaresOnGrid()

        attachListenersToSquares()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (animateBoard) {
            presenter.loadPuzzles()
            animateBoard = false
            animateBoardCreation()
        }
        super.onWindowFocusChanged(hasFocus)
    }
}
