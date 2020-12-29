package com.vango.tictactoe.ui.onlineFragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vango.tictactoe.MainActivity
import com.vango.tictactoe.R
import com.vango.tictactoe.databinding.OnlineFragmentBinding
import com.vango.tictactoe.ui.createLobby.CreateLobby
import com.vango.tictactoe.ui.main.MainFragment
import com.vango.tictactoe.ui.singleGame.SingleGame
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class Online_fragment : Fragment() {

    companion object {
        fun newInstance() = Online_fragment()
    }
    private lateinit var binding: OnlineFragmentBinding
    private val  viewModel: OnlineFragmentViewModel by viewModels()
    private lateinit var lobbyId: String

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.online_fragment, container, false
            )
        binding.buttonLobby.setOnClickListener {
            (activity as MainActivity?)?.replaceFragment(CreateLobby::class.java)
        }
        binding.buttonJoin.setOnClickListener {
            dialog()
        }
        viewModel.gameActive.observe(viewLifecycleOwner){
            if(it){
                (activity as MainActivity?)?.replaceFragment(SingleGame::class.java,"online;"+lobbyId)
            }
        }
        return binding.root
    }

    @ExperimentalCoroutinesApi
    fun dialog(){
        val editText = EditText(context)
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setView(editText)
        alertDialog.setMessage(resources.getString(R.string.enter_lobby_id))
        alertDialog.setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
            connectToLobby(editText.text.toString())
        }
        alertDialog.setNegativeButton(resources.getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int ->
        }
        alertDialog.show()
    }
    @ExperimentalCoroutinesApi
    fun connectToLobby(lobbyId : String){
        this.lobbyId=lobbyId
        viewModel.connectToLobby(lobbyId)
    }

}