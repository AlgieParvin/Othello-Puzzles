package algie.parvin.othello

import algie.parvin.othello.presenter.Presenter
import algie.parvin.othello.presenter.ViewInterface
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.GridLayout
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

    private fun setChipsOnBoard(white: List<Int>, black: List<Int>) {
        black.map {
            (board.getChildAt(it) as ImageView).setBackgroundResource(R.drawable.black_chip) }
        white.map {
            (board.getChildAt(it) as ImageView).setBackgroundResource(R.drawable.white_chip) }
    }

    private fun removeActionAndStatusBars() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }

    private fun attachListenersToSquares() {
        val squareCount = board.getChildCount()
        for (i in 0 until squareCount) {
            board.getChildAt(i).setOnClickListener({ v -> presenter.handleMove(i)} )
        }
    }

    override fun updateChips() {
        setChipsOnBoard(presenter.getWhite(), presenter.getBlack())
    }

    override fun animateBoardCreation() {
        val avd = board.background as AnimatedVectorDrawable
        avd.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = Presenter(this)

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
