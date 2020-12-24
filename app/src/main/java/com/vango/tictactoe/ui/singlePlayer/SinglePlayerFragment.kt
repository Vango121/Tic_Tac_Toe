package com.vango.tictactoe.ui.singlePlayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.vango.tictactoe.MainActivity
import com.vango.tictactoe.R
import com.vango.tictactoe.databinding.SinglePlayerFragmentBinding
import com.vango.tictactoe.ui.singleGame.SingleGame

class SinglePlayerFragment : Fragment() {

    companion object {
        fun newInstance() = SinglePlayerFragment()
    }

    private lateinit var binding: SinglePlayerFragmentBinding
    private val viewModel: SinglePlayerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.single_player_fragment, container, false
            )
        binding.buttonFriend.setOnClickListener {
            (activity as MainActivity?)?.replaceFragment(SingleGame::class.java,"single")
        }
        binding.buttonAi.setOnClickListener {
            (activity as MainActivity?)?.replaceFragment(SingleGame::class.java,"AI")
        }
        return binding.root
    }

}