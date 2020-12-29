package com.vango.tictactoe.ui.createLobby

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vango.tictactoe.MainActivity
import com.vango.tictactoe.R
import com.vango.tictactoe.databinding.CreateLobbyFragmentBinding
import com.vango.tictactoe.ui.main.MainFragment
import com.vango.tictactoe.ui.singleGame.SingleGame
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class CreateLobby : Fragment() {

    companion object {
        fun newInstance() = CreateLobby()
    }
    private lateinit var database: DatabaseReference
    private lateinit var binding: CreateLobbyFragmentBinding
    @ExperimentalCoroutinesApi
    private val viewModel: CreateLobbyViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.create_lobby_fragment, container, false
            )
        binding.viewmodel = viewModel
        binding.setLifecycleOwner { lifecycle }
        binding.imageViewCopy.setOnClickListener {
            val clipboard = (context?.getSystemService(Context.CLIPBOARD_SERVICE)) as? ClipboardManager
            val clipData = ClipData.newPlainText("lobby id",binding.textViewId.text)
            clipboard?.setPrimaryClip(clipData)
            Toast.makeText(context,resources.getString(R.string.text_copied),Toast.LENGTH_SHORT).show()
        }
        viewModel.activeGame.observe(viewLifecycleOwner){
            if(it==1){
                (activity as MainActivity?)?.replaceFragment(SingleGame::class.java,"onlineh"+viewModel.getLobbyId())
            }
        }
        return binding.root
    }

}