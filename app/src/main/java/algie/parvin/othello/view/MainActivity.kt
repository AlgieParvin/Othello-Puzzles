package algie.parvin.othello

import algie.parvin.othello.presenter.Presenter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var presenter: Presenter

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

    fun updateChips() {
        setChipsOnBoard(presenter.getWhite(), presenter.getBlack())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        removeActionAndStatusBars()

        presenter = Presenter(this)
        attachListenersToSquares()
        setChipsOnBoard(presenter.getWhite(), presenter.getBlack())
    }
}
