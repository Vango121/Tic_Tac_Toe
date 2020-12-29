package com.vango.tictactoe.ui.onlineFragment

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OnlineFragmentViewModel @ViewModelInject constructor(val repository: OnlineFragmentRepository): ViewModel() {
    private var _gameActive = MutableLiveData<Boolean>()
    val gameActive: LiveData<Boolean>
        get() = _gameActive
    @ExperimentalCoroutinesApi
    fun connectToLobby(lobbyId: String){

        viewModelScope.launch {
            repository.initializeGameInDB(lobbyId).collect {
                _gameActive.postValue(it)
            }
        }
    }
}