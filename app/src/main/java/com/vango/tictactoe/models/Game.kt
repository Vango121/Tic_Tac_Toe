package com.vango.tictactoe.models

data class Game(
    var active: Int = 0,
    var bestOf: Int = 1,
    var crossList: MutableList<String> = ArrayList(),
    var circleList: MutableList<String> = ArrayList(),
    var hostCircle: Boolean = true
)
