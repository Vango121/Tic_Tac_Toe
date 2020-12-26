package com.vango.tictactoe

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateLobbyViewModel : ViewModel() {
    private var _bestOfValue = MutableLiveData("1")
    val bestOfValue: LiveData<String>
        get() = _bestOfValue
    private var _lobbyId = MutableLiveData<String>()
    val lobbyId: LiveData<String>
        get() = _lobbyId
   private var bestOfCount = 1
    init {
        generateLobbyId()
    }
    fun addToBestOf(view: View){
        if(bestOfCount<3){
            bestOfCount++
            _bestOfValue.value = bestOfCount.toString()
        }
    }
    fun subtractFromBestOf(view: View){
        if (bestOfCount>1){
            bestOfCount--
            _bestOfValue.value = bestOfCount.toString()
        }
    }
    fun generateLobbyId(){
        val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val lobbyIdString= (1..7)
            .map { chars.random() }
            .joinToString("")
        _lobbyId.postValue(lobbyIdString)
    }
}