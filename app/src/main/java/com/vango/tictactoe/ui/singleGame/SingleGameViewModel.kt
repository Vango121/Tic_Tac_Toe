package com.vango.tictactoe.ui.singleGame

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vango.tictactoe.GFG
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

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
    var game = true
    var countint = 0
    var board = arrayOf(
        charArrayOf('_', '_', '_'),
        charArrayOf('_', '_', '_'),
        charArrayOf('_', '_', '_'))
    var gameAi = false
    fun boardOnClick(view: View){
        if(game) {
            countint++
            when (circle) {
                true -> {
                    //val tag = view.getTag().toString()
//                addValToBoard(tag,'o')
                    circleList.add(view.getTag().toString())
                    if (checkIfWon(circleList)) {
                        _gameResult.postValue(1)
                        game=false
                    }
                }
                false -> {
                    //val tag = view.getTag().toString()
//                addValToBoard(tag,'x')
                    crossList.add(view.getTag().toString())
                    if (checkIfWon(crossList)) {
                        _gameResult.postValue(2)
                        game=false
                    }
                }
            }
            val valueToPass = Pair(view as ImageView, circle)
            Log.i(
                "val pass",
                valueToPass.first.getTag().toString() + " " + valueToPass.second.toString()
            )
            _boardClickedImg.postValue(valueToPass)
            circle = !circle
            if (countint == 9) {
                game=false
                _gameResult.postValue(0)
            }
        }
    }

    fun findmove(): Int{
        var move = Random.nextInt(0,9)
        if(countint<9){
        while(crossList.contains(move.toString()) || circleList.contains(move.toString())){
            move = Random.nextInt(0,9)
            Log.i("move",move.toString())
        }
        }
//        crossList.add(move.toString())
//        circle=true
//        countint++
//        if(checkIfWon(crossList)){
//            _gameResult.postValue(2)
//        }
        return move
    }
    fun setGameType(type: String?){
        if (type != null) {
            if(type=="AI"){
                gameAi=true
            }
        }
    }
    fun getGameType(): Boolean = gameAi
    fun addValToBoard(tag : String, type : Char){
        val tagvalue = tag.toInt()
        if(tagvalue<3){
            board[0][tagvalue] = type
            Log.i("board 0 row ",tag+ " " + type)
        }
        else if(tagvalue>=3&& tagvalue<6){
            board[1][tagvalue-3] = type
            Log.i("board 1 row ",tag+ " " + type)
        }
        else{
            board[2][tagvalue-6] = type
            Log.i("board 2 row ",tag+ " " + type)
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