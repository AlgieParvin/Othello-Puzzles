package algie.parvin.othello.puzzlelist


import algie.parvin.othello.R
import algie.parvin.othello.db.PuzzleRepository
import algie.parvin.othello.model.DBRepository
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_puzzle_list.board
import kotlin.math.ceil
import kotlin.math.round


class PuzzleListFragment : Fragment(), PuzzleListContract.ViewInterface {

    private lateinit var navController: NavController
    private lateinit var presenter: PuzzleListContract.PresenterInterface

    private fun dpToPx(dp: Int, context: Context): Int {
        return round(dp * context.resources.displayMetrics.density).toInt()
    }

    override fun initializePuzzleGrid(maxId: Int, openedThreshold: Int) {
        board.columnCount = 5
        val d = ceil(maxId.toDouble() / board.columnCount).toInt()
        board.rowCount = d

        for (i in 1..maxId) {
            val view = Button(context)
            board.addView(view, i - 1)
            if (i <= openedThreshold) {
                view.setBackgroundResource(R.drawable.puzzle_opened_button)
                view.setTextColor(ContextCompat.getColor(activity!!.baseContext, R.color.mainColor))
            } else {
                view.setBackgroundResource(R.drawable.puzzle_closed_button)
                view.setTextColor(ContextCompat.getColor(activity!!.baseContext, R.color.secondaryColor))
                view.isEnabled = false
            }
            view.setTypeface(view.typeface, Typeface.BOLD)
            view.text = i.toString()
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

            val params = view.layoutParams
            params.height = dpToPx(50, view.context)
            params.width = dpToPx(50, view.context)
            view.layoutParams = params

            view.setOnClickListener {
                val bundle = Bundle().apply { putInt("ID", i) }
                navController.navigate(R.id.action_puzzleListFragment_to_gameBoardFragment, bundle)
            }
        }
    }

    override fun provideRepository(): DBRepository {
        return PuzzleRepository(activity!!.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_puzzle_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        presenter = PuzzleListPresenter(this)

        presenter.onLoadPuzzleGrid()

        val buttons = emptyList<Button>()
        buttons.forEach {
            button -> button.setOnClickListener {
                val id = button.text.toString().toInt()
                val bundle = Bundle().apply { putInt("ID", id) }
                navController.navigate(R.id.action_puzzleListFragment_to_gameBoardFragment, bundle)
            }
        }
    }
}
