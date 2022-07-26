package com.shahriyor.tic_tac_toe.data


data class GameDetails(
    var roomID: String? = null,
    var firstPlayerName: String? = null,
    var secondPlayerName: String? = null,
    var firstPlayerScore: Int? = 0,
    var secondPlayerScore: Int? = 0,
    var firstPlayerChar: String? = null,
    var secondPlayerChar: String? = null,
    var firstPlayerMove: Boolean? = null,
    var secondPlayerMove: Boolean? = null,
    var firstPlayerStatus: String = "NA",
    var secondPlayerStatus: String = "NA",
    var firstPlayerArray: MutableList<Int> = mutableListOf(10),
    var secondPlayerArray: MutableList<Int> = mutableListOf(10),
    var gameList: MutableList<String> = mutableListOf("A","A","A","A","A","A","A","A","A")
)