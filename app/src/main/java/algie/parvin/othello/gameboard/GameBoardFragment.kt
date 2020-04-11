package algie.parvin.othello.gameboard


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import algie.parvin.othello.R
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Handler
import android.widget.GridLayout
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.synthetic.main.activity_gameboard.*


class GameBoardFragment : Fragment(), GameBoardContract.ViewInterface {

    private lateinit var presenter: GameBoardContract.PresenterInterface
    private var freezeBoard = false

    private fun initializedSquaresOnGrid() {
        val boardSize = presenter.getBoardSize()
        board.columnCount = boardSize
        board.rowCount = boardSize

        for (i in 0 until boardSize * boardSize) {
            val view = ImageView(context)
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
            val drawable = AnimatedVectorDrawableCompat.create(activity!!, R.drawable.black_chip)
            (board.getChildAt(it) as ImageView).setImageDrawable(drawable)
        }
        white.map {
            val drawable = AnimatedVectorDrawableCompat.create(activity!!, R.drawable.white_chip)
            (board.getChildAt(it) as ImageView).setImageDrawable(drawable)
        }
        this.freezeBoard = freezeBoard
    }

    private fun removeActionAndStatusBars() {
        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        activity!!.actionBar?.hide()
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
            val drawable = AnimatedVectorDrawableCompat.create(activity!!, R.drawable.black_to_white_avd)
            (board.getChildAt(index) as ImageView).setImageDrawable(drawable)
        } else {
            val drawable = AnimatedVectorDrawableCompat.create(activity!!, R.drawable.white_to_black_avd)
            (board.getChildAt(index) as ImageView).setImageDrawable(drawable)
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
        if (drawable is AnimatedVectorDrawable) {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = GameBoardPresenter(this, activity!!.application)

        removeActionAndStatusBars()
        initializedSquaresOnGrid()
        attachListenersToSquares()

        val id = arguments!!.getInt("ID", 1)
        presenter.loadPuzzle(id)
        animateBoardCreation()
    }
}
