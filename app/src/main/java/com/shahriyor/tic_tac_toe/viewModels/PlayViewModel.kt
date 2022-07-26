package com.shahriyor.tic_tac_toe.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shahriyor.tic_tac_toe.utils.Utils
import com.shahriyor.tic_tac_toe.data.GameDetails
import com.shahriyor.tic_tac_toe.utils.Constants.DRAW
import com.shahriyor.tic_tac_toe.utils.Constants.LOSE
import com.shahriyor.tic_tac_toe.utils.Constants.PATH_FIREBASE_DATABASE
import com.shahriyor.tic_tac_toe.utils.Constants.WIN

class PlayViewModel : ViewModel() {

    lateinit var key: String

    var playerFirstOrSecond: Int? = null

    private val database = FirebaseDatabase.getInstance().getReference(PATH_FIREBASE_DATABASE)

    private var _gameDetails = MutableLiveData<GameDetails>()
    val gameDetails: LiveData<GameDetails>
        get() = _gameDetails

    lateinit var gameDetailsClass: GameDetails

    private var _moves = MutableLiveData<MutableList<String>>()
    val moves: LiveData<MutableList<String>>
        get() = _moves

    private val _playerMove = MutableLiveData<Boolean>()
    val playerMove: LiveData<Boolean>
        get() = _playerMove

    private var _checkStatusGame = MutableLiveData<String?>()
    val checkStatusGame: LiveData<String?>
        get() = _checkStatusGame

    private var _scorePlayerFirst = MutableLiveData<Int?>()
    val scorePlayerFirst: LiveData<Int?>
        get() = _scorePlayerFirst

    private var _scorePlayerSecond = MutableLiveData<Int?>()
    val scorePlayerSecond: LiveData<Int?>
        get() = _scorePlayerSecond

    fun getRoomDetails(dbKey: String) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (room in snapshot.children) {
                        if (room.key == dbKey) {
                            key = room.key!!
                            val gameDetails: GameDetails? = room.getValue(GameDetails::class.java)
                            gameDetailsClass = gameDetails!!
                            _gameDetails.value = gameDetails!!
                            _moves.value = gameDetails.gameList
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val room = snapshot.getValue(GameDetails::class.java)
            gameDetailsClass = room!!
            _moves.value = room!!.gameList
            if (playerFirstOrSecond == 1) {
                _playerMove.value = room.firstPlayerMove!!
                _checkStatusGame.value = gameDetailsClass.firstPlayerStatus
            }
            if (playerFirstOrSecond == 2) {
                _playerMove.value = room.secondPlayerMove!!
                _checkStatusGame.value = gameDetailsClass.secondPlayerStatus
            }
            _scorePlayerFirst.value = gameDetailsClass.firstPlayerScore
            _scorePlayerSecond.value = gameDetailsClass.secondPlayerScore
        }

        override fun onCancelled(error: DatabaseError) {}

    }

    fun getRealtimeDetails(DBkey: String, player: Int) {
        playerFirstOrSecond = player
        database.child(DBkey).addValueEventListener(valueEventListener)
    }

    fun btnClick(btnPos: Int, player: Int) {
        when (btnPos) {
            1 -> add(player, 1)
            2 -> add(player, 2)
            3 -> add(player, 3)
            4 -> add(player, 4)
            5 -> add(player, 5)
            6 -> add(player, 6)
            7 -> add(player, 7)
            8 -> add(player, 8)
            9 -> add(player, 9)
        }
    }

    private fun add(player: Int, btn: Int) {
        if (player == 1) {
            gameDetailsClass.firstPlayerArray.add(btn)
            gameDetailsClass.gameList[btn - 1] = gameDetailsClass.firstPlayerChar!!
            gameDetailsClass.firstPlayerMove = false
            gameDetailsClass.secondPlayerMove = true

            gameDetailsClass.firstPlayerStatus =
                Utils.checkWin(gameDetailsClass.firstPlayerArray, gameDetailsClass.gameList)

            if (gameDetailsClass.firstPlayerStatus == DRAW) {
                gameDetailsClass.secondPlayerStatus = DRAW
            }
            if (gameDetailsClass.firstPlayerStatus == WIN) {
                gameDetailsClass.firstPlayerScore = gameDetailsClass.firstPlayerScore?.plus(1)
                gameDetailsClass.secondPlayerStatus = LOSE
            }
        } else {
            gameDetailsClass.secondPlayerArray.add(btn)
            gameDetailsClass.gameList[btn - 1] = gameDetailsClass.secondPlayerChar!!
            gameDetailsClass.secondPlayerMove = false
            gameDetailsClass.firstPlayerMove = true

            gameDetailsClass.secondPlayerStatus =
                Utils.checkWin(gameDetailsClass.secondPlayerArray, gameDetailsClass.gameList)

            if (gameDetailsClass.secondPlayerStatus == DRAW) {
                gameDetailsClass.firstPlayerStatus = DRAW
            }
            if (gameDetailsClass.secondPlayerStatus == WIN) {
                gameDetailsClass.secondPlayerScore = gameDetailsClass.secondPlayerScore?.plus(1)
                gameDetailsClass.firstPlayerStatus = LOSE
            }
        }
        database.child(key).setValue(gameDetailsClass)
    }

    fun playAgain() {
        gameDetailsClass.gameList = mutableListOf("A", "A", "A", "A", "A", "A", "A", "A", "A")
        gameDetailsClass.firstPlayerArray = mutableListOf(10)
        gameDetailsClass.secondPlayerArray = mutableListOf(10)

        gameDetailsClass.firstPlayerStatus = "NA"
        gameDetailsClass.secondPlayerStatus = "NA"

        database.child(key).setValue(gameDetailsClass)
    }
}