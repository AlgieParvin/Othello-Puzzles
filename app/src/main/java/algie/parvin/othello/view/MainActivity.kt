package algie.parvin.othello

import algie.parvin.othello.presenter.Presenter
import algie.parvin.othello.presenter.ViewInterface
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.GridLayout
import androidx.core.view.setPadding
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat


interface PresenterInterface {
    fun getBoardSize(): Int
    fun handleMove(square: Int)
    fun getWhite(): List<Int>
    fun getBlack(): List<Int>

}


class MainActivity : AppCompatActivity(), ViewInterface {

    private lateinit var presenter: PresenterInterface
    private var animateBoard = true

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

    override fun setChipsOnBoard(white: List<Int>, black: List<Int>) {
        black.map {
            (board.getChildAt(it) as ImageView).setImageResource(R.drawable.black_to_white_avd) }
        white.map {
            (board.getChildAt(it) as ImageView).setImageResource(R.drawable.white_to_black_avd) }
    }

    private fun removeActionAndStatusBars() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }

    private fun attachListenersToSquares() {
        for (i in 0 until board.childCount) {
            board.getChildAt(i).setOnClickListener { presenter.handleMove(i)}
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = Presenter(this, application)

        removeActionAndStatusBars()
        initializedSquaresOnGrid()

        attachListenersToSquares()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (animateBoard) {
            animateBoardCreation()
            setChipsOnBoard(presenter.getWhite(), presenter.getBlack())
            animateBoard = false
        }
        super.onWindowFocusChanged(hasFocus)
    }
}
