package com.vango.tictactoe.ui.createLobby

import android.util.Log
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.vango.tictactoe.models.Game
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CreateLobbyViewModel @ViewModelInject constructor(val repository: CreateLobbyRepository) :
    ViewModel() {
    private var _bestOfValue = MutableLiveData("1")
    val bestOfValue: LiveData<String>
        get() = _bestOfValue
//    private var _lobbyId = MutableLiveData<String>()
//    val lobbyId: LiveData<String>
//        get() = _lobbyId
    private var lobbyId = ""
    private var _activeGame = MutableLiveData<Int>()
    val activeGame: LiveData<Int>
        get() = _activeGame
    private var bestOfCount = 1
    var i = 0 // transitions to game fragment count

    init {
        generateLobbyId()
    }

    fun addToBestOf(view: View) {
        if (bestOfCount < 3) {
            bestOfCount++
            _bestOfValue.value = bestOfCount.toString()
        }
    }

    fun subtractFromBestOf(view: View) {
        if (bestOfCount > 1) {
            bestOfCount--
            _bestOfValue.value = bestOfCount.toString()
        }
    }

    @ExperimentalCoroutinesApi
    fun generateLobbyId() {
        val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val lobbyIdString = (1..7)
            .map { chars.random() }
            .joinToString("")
        lobbyId = lobbyIdString
        repository.initializeGameInDB(lobbyIdString,_bestOfValue.value?.toInt()!!)
        fetchGame()
    }

    fun getLobbyId(): String = lobbyId
    @ExperimentalCoroutinesApi
    fun fetchGame() {
        viewModelScope.launch {
            repository.getMoves().collect {
                when {
                    it.isSuccess -> {
                        val game = it.getOrNull()
                        val toreturn = game?.active
                        if(toreturn==1 && i<1){
                            i++
                            _activeGame.postValue(toreturn)
                        }

                    }
                    it.isFailure -> {

                    }
                }
            }
        }
    }

}