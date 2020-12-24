package com.vango.tictactoe.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.vango.tictactoe.MainActivity
import com.vango.tictactoe.R
import com.vango.tictactoe.SettingsFragment
import com.vango.tictactoe.databinding.MainFragmentBinding
import com.vango.tictactoe.ui.singlePlayer.SinglePlayerFragment

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.main_fragment, container, false
            )
        binding.buttonSingle.setOnClickListener {
            (activity as MainActivity?)?.replaceFragment(SinglePlayerFragment::class.java)
        }
        binding.buttonSetting.setOnClickListener {
            (activity as MainActivity?)?.replaceFragment(SettingsFragment::class.java)
        }
        return binding.root
    }

}