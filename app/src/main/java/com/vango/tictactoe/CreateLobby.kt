package com.vango.tictactoe

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.vango.tictactoe.databinding.CreateLobbyFragmentBinding

class CreateLobby : Fragment() {

    companion object {
        fun newInstance() = CreateLobby()
    }
    private lateinit var binding: CreateLobbyFragmentBinding
    private val viewModel: CreateLobbyViewModel by viewModels()

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
        return binding.root
    }

}