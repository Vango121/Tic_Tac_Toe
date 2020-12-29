package com.vango.tictactoe.ui.singleGame


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.vango.tictactoe.GFG
import com.vango.tictactoe.R
import com.vango.tictactoe.databinding.SingleGameFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleGame : Fragment() {

    companion object {
        fun newInstance() = SingleGame()
    }

    private lateinit var binding: SingleGameFragmentBinding
    private val viewModel: SingleGameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.single_game_fragment, container, false
            )
        binding.viewmodel = viewModel
        getDataFromPreviousFragment()
        viewModel.boardClickedImg.observe(viewLifecycleOwner, {
            when (it.second) {
                true -> {
                    it.first.setImageResource(R.drawable.circle)
                    if(viewModel.getGameType()){ // check if this is ai type
                        val move = viewModel.findmove()
                        val view: ImageView = binding.board.get(move) as ImageView
                        view.callOnClick()
                    }
                    }
                false -> it.first.setImageResource(R.drawable.cross)
            }
            it.first.isEnabled = false
        })
        viewModel.nextMove.observe(viewLifecycleOwner,{
            val view: ImageView = binding.board.get(it.toInt()) as ImageView
            view.callOnClick()
        })
        viewModel.gameResult.observe(viewLifecycleOwner, {
            disableBoard()
            when (it) {
                0 -> Toast.makeText(context, "Remis", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(context, "wygralo kolko", Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(context, "wygral krzyzyk", Toast.LENGTH_SHORT).show()
                3 -> restartBoard()
            }
        })

        viewModel.level = PreferenceManager.getDefaultSharedPreferences(context)
            .getString("list_preference_level", "1")
            .toString()
        return binding.root
    }

    fun disableBoard() {
        for (view in binding.board) {
            view.isEnabled = false
        }
        binding.buttonRestart.visibility = View.VISIBLE
    }
    fun restartBoard(){
        for (view in binding.board) {
            view.isEnabled = true
            val v = view as ImageView
            v.setImageResource(R.drawable.empty)
        }
        binding.buttonRestart.visibility = View.GONE
    }

    fun getDataFromPreviousFragment() {
        viewModel.setGameType(arguments?.getString("type"))
    }

}