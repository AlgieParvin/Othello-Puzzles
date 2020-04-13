package algie.parvin.othello


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_puzzle_list.*


class PuzzleListFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_puzzle_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val buttons = listOf(openPuzzleButton1, openPuzzleButton2, openPuzzleButton3, openPuzzleButton4)
        buttons.forEach {
            button -> button.setOnClickListener {
                val id = button.text.toString().toInt()
                val bundle = Bundle().apply { putInt("ID", id) }
                navController.navigate(R.id.action_puzzleListFragment_to_gameBoardFragment, bundle)
            }
        }
    }

}
