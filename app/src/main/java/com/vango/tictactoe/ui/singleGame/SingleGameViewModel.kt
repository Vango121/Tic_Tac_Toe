package com.vango.tictactoe.ui.singleGame

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.vango.tictactoe.GFG
import com.vango.tictactoe.models.Game
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class SingleGameViewModel @ViewModelInject constructor(val repository: SingleGameRepository) :
    ViewModel() {
    var circle = true // if true circle false cross
    private var _boardClickedImg = MutableLiveData<Pair<ImageView, Boolean>>()
    val boardClickedImg: LiveData<Pair<ImageView, Boolean>>
        get() = _boardClickedImg
    private var _gameResult = MutableLiveData<Int>()
    val gameResult: LiveData<Int>
        get() = _gameResult
    var crossList: MutableList<String> = ArrayList()
    var circleList: MutableList<String> = ArrayList()
    private var _nextMove = MutableLiveData<String>()
    val nextMove: LiveData<String>
        get() = _nextMove
    private var game = true
    private var countint = 0
    private lateinit var dbGame: Game
    private var board = arrayOf(
        charArrayOf('_', '_', '_'),
        charArrayOf('_', '_', '_'),
        charArrayOf('_', '_', '_')
    )
    private var gameAi = false
    private var online = false
    private var host = false
    private lateinit var lobbyId: String
    var level: String = "1"
        set(value) {
            field = value
        }

    fun boardOnClick(view: View) {
        if (game) {
            countint++
            when (circle) {
                true -> {

                    if(!circleList.contains(view.getTag().toString())) circleList.add(view.getTag().toString())
                    if(online){
                        repository.passCircleList(circleList)
                    }
                    if (checkIfWon(circleList)) {
                        _gameResult.value = (1)
                        game = false
                    }
                }
                false -> {
                    if(!crossList.contains(view.getTag().toString())) crossList.add(view.getTag().toString())
                    if(online){
                        repository.passCrossList(crossList)
                    }
                    if (checkIfWon(crossList)) {
                        _gameResult.value = (2)
                        game = false
                    }
                }
            }
            val valueToPass = Pair(view as ImageView, circle)
            _boardClickedImg.postValue(valueToPass)
            circle = !circle
            if (countint == 9 && _gameResult.value != 1 && _gameResult.value != 2) {
                game = false
                _gameResult.value = (0)
            }
        }
    }

    fun findmove(): Int {
        when (level) {
            "1" -> return level1Move()
            "2" -> return level2Move()
            "3" -> return level3Move()
            else -> return 0
        }
    }

    fun level1Move(): Int {
        var move = Random.nextInt(0, 9)
        if (countint < 9) {
            while (crossList.contains(move.toString()) || circleList.contains(move.toString())) {
                move = Random.nextInt(0, 9)
            }
        }
        return move
    }

    fun level2Move(): Int {
        val conditions = arrayOf(
            arrayOf("0", "1", "2"),
            arrayOf("3", "4", "5"),
            arrayOf("6", "7", "8"),
            arrayOf("0", "3", "6"),
            arrayOf("1", "4", "7"),
            arrayOf("2", "5", "8"),
            arrayOf("0", "4", "8"),
            arrayOf("2", "4", "6")
        )
        var i = 0
        var list = arrayOf("")
        run loop@{
            conditions.forEach {
                it.forEach { cond ->
                    if (crossList.contains(cond)) {
                        i++
                    }
                    if (i == 2) {
                        list = it
                        return@loop
                    }
                }
                i = 0
            }
        }
        if (list.size == 3) {
            list.forEach {
                if (!crossList.contains(it) && !circleList.contains(it)) {
                    return it.toInt()
                }
            }
        }
        return level1Move()
    }

    fun level3Move(): Int {
        val conditions = arrayOf(
            arrayOf("0", "1", "2"),
            arrayOf("3", "4", "5"),
            arrayOf("6", "7", "8"),
            arrayOf("0", "3", "6"),
            arrayOf("1", "4", "7"),
            arrayOf("2", "5", "8"),
            arrayOf("0", "4", "8"),
            arrayOf("2", "4", "6")
        )
        var i = 0
        var list = arrayOf("")
        run loop@{
            conditions.forEach {
                it.forEachIndexed { id, cond ->
                    if (circleList.contains(cond)) {
                        i++
                    }
                    if (i == 2) {
                        conditions.drop(id)
                        list = it
                        return@loop
                    }
                }
                i = 0
            }
        }
        Log.i("size 3lv", list.size.toString())
        if (list.size == 3) {
            list.forEach {
                if (!crossList.contains(it) && !circleList.contains(it)) {
                    return it.toInt()
                }
            }
        }
        return level2Move()
    }

    @ExperimentalCoroutinesApi
    fun setGameType(type: String?) {
        if (type != null) {
            var subType: String = ""
            if(type.length>7){
                subType = type.substring(0,7)
            }
            if (type == "AI") {
                gameAi = true
            } else if (subType == "online;") {
                online = true
                lobbyId = type.substring(7)
                repository.initReference(lobbyId)
                fetchGame()
            } else if (subType == "onlineh"){
                online = true
                host = true
                lobbyId = type.substring(7)
                repository.initReference(lobbyId)
                fetchGame()
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun fetchGame() {
        viewModelScope.launch {
            repository.getMoves().collect {
                when {
                    it.isSuccess -> {
                        val game = it.getOrNull()
                        if (game!=null){
                            if(!crossList.containsAll(game.crossList) || !circleList.containsAll(game.circleList)) {
                                crossList = game.crossList
                                circleList = game.circleList
                                dbGame = game
                                if (host) {
                                    if (game.hostCircle && crossList.size > 0 && !circle) _nextMove.value =
                                        crossList.last()
                                    else if (!game.hostCircle && circleList.size > 0 && circle) _nextMove.value =
                                        circleList.last()
                                } else {
                                    if (game.hostCircle && circleList.size > 0 && circle) _nextMove.value =
                                        circleList.last()
                                    if (!game.hostCircle && crossList.size > 0 && !circle) _nextMove.value =
                                        crossList.last()
                                }
                            }
                        }
                    }
                    it.isFailure -> {

                    }
                }
            }
        }
    }

    fun getGameType(): Boolean = gameAi
    fun checkIfOnline(): Boolean = online
    fun checkIfHost(): Boolean = host
    fun addValToBoard(tag: String, type: Char) {
        val tagvalue = tag.toInt()
        if (tagvalue < 3) {
            board[0][tagvalue] = type
            Log.i("board 0 row ", tag + " " + type)
        } else if (tagvalue >= 3 && tagvalue < 6) {
            board[1][tagvalue - 3] = type
            Log.i("board 1 row ", tag + " " + type)
        } else {
            board[2][tagvalue - 6] = type
            Log.i("board 2 row ", tag + " " + type)
        }
    }

    fun checkIfWon(list: MutableList<String>): Boolean {
        list.sort()
        val conditions = arrayOf(
            arrayOf("0", "1", "2"),
            arrayOf("3", "4", "5"),
            arrayOf("6", "7", "8"),
            arrayOf("0", "3", "6"),
            arrayOf("1", "4", "7"),
            arrayOf("2", "5", "8"),
            arrayOf("0", "4", "8"),
            arrayOf("2", "4", "6")
        )
        conditions.forEach { if (list.containsAll(it.toList())) return true }
        return false
    }

    fun restartButton(view: View) {
        circleList.clear()
        crossList.clear()
        circle = true
        game = true
        countint = 0
        _gameResult.postValue(3)
    }
}