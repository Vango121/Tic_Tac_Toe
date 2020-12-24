package com.vango.tictactoe.ui.singleGame

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SingleGameViewModel : ViewModel() {
    var circle = true // if true circle false cross
    private var _boardClickedImg = MutableLiveData<Pair<ImageView,Boolean>>()
    val boardClickedImg: LiveData<Pair<ImageView,Boolean>>
        get() = _boardClickedImg
    private var _gameResult = MutableLiveData<Int>()
    val gameResult: LiveData<Int>
        get() = _gameResult
    val crossList: MutableList<String> = ArrayList()
    val circleList: MutableList<String> = ArrayList()
    var count = 0
    fun boardOnClick(view: View){
        count++
        when(circle){
            true -> {
                circleList.add(view.getTag().toString())
                if(checkIfWon(circleList)){
                    _gameResult.postValue(1)
                }
            }
            false -> {
                crossList.add(view.getTag().toString())
                if(checkIfWon(crossList)){
                    _gameResult.postValue(2)
                }
            }
        }
        val valueToPass = Pair(view as ImageView,circle)
        _boardClickedImg.postValue(valueToPass)
        circle=!circle
        if(count==9){
            _gameResult.postValue(0)
        }
    }
    fun checkIfWon(list : MutableList<String>): Boolean{
        list.sort()
        val conditions = arrayOf(arrayOf("0","1","2"),
            arrayOf("3","4","5"),
            arrayOf("6","7","8"),
            arrayOf("0","3","6"),
            arrayOf("1","4","7"),
            arrayOf("2","5","8"),
            arrayOf("0","4","8"),
            arrayOf("2","4","6"))
        conditions.forEach { if(list.containsAll(it.toList())) return true }
        return false
    }
}