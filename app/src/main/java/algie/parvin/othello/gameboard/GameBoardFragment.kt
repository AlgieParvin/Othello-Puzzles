package algie.parvin.othello.gameboard


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import algie.parvin.othello.R
import algie.parvin.othello.model.Chip
import algie.parvin.othello.model.Field
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Handler
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.synthetic.main.fragment_game_board.*


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

    private fun resetBoard() {
        for (index in 0 until board.childCount) {
            (board.getChildAt(index) as ImageView).setImageResource(0)
        }
        freezeBoard = false
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

    private fun animateBoardCreation() {
        val drawable = board.background
        if (drawable is AnimatedVectorDrawable) {
            drawable.start()
        }
    }

    override fun showNewPuzzle(chipsByRowsAndColumns: List<List<Field>>, boardSize: Int) {
        val handler = Handler()
        presenter.getMovesObservable().observe(viewLifecycleOwner, Observer { moves ->
            if (moves == 0) {
                movesCounter.text = ""
            } else {
                movesCounter.text = moves.toString()
            }
        })
        resetBoard()
        animateBoardCreation()

        for (i in chipsByRowsAndColumns.indices) {
            handler.postDelayed({
                chipsByRowsAndColumns[i].forEach {
                    val drawable = if (it.chip == Chip.BLACK)
                        AnimatedVectorDrawableCompat.create(activity!!, R.drawable.spawn_black) else
                        AnimatedVectorDrawableCompat.create(activity!!, R.drawable.spawn_white)
                    (board.getChildAt(it.row * boardSize + it.column) as ImageView).setImageDrawable(drawable)
                    drawable!!.start()
                }
            }, i.toLong() * 100 + 100)
        }
    }

    override fun reverseChips(chipIndices: List<Int>, reverseToWhite: Boolean) {
        chipIndices.map { i -> reverseChip(i, reverseToWhite) }
        if (reverseToWhite) {
            Handler().postDelayed({
                presenter.receiveOpponentMove()
                freezeBoard = true
            }, 600)
        } else {
            Handler().postDelayed({
                freezeBoard = false
            }, 600)
        }
    }

    override fun setMovesCounter(moves: Int) {
        movesCounter.text = moves.toString()
    }

    override fun onPlayerWin() {
        Toast.makeText(activity, R.string.player_wins, Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            presenter.loadNextPuzzle()
        }, 1000)
    }

    override fun onPlayerLose() {
        Toast.makeText(activity, R.string.player_lost, Toast.LENGTH_SHORT).show()
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

        if (arguments == null) {
            presenter.loadDefaultPuzzle()
        } else {
            val id = arguments!!.getInt("ID", -1)
            if (id == -1) {
                presenter.loadDefaultPuzzle()
            } else {
                presenter.loadPuzzle(id)
            }
        }
    }
}
