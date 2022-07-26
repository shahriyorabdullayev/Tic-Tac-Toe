package com.shahriyor.tic_tac_toe.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shahriyor.tic_tac_toe.data.GameDetails
import com.shahriyor.tic_tac_toe.utils.Constants.PATH_FIREBASE_DATABASE

class CreateViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance().getReference(PATH_FIREBASE_DATABASE)
    private var key: String? = null

    private val _keyDB = MutableLiveData<String?>()
    val keyDB: LiveData<String?>
        get() = _keyDB

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    private val _join = MutableLiveData<GameDetails?>()
    val join: LiveData<GameDetails?>
        get() = _join

    fun createRoom(name: String, gameDetails: GameDetails) {
        gameDetails.firstPlayerName = name
        key = db.push().key!!
        _keyDB.value = key
        gameDetails.roomID = key!!.takeLast(5)
        val rand = (0..1).random()
        if (rand == 1) {
            gameDetails.firstPlayerChar = "X"
            gameDetails.firstPlayerMove = true
            gameDetails.secondPlayerMove = false
            gameDetails.secondPlayerChar = "O"
        } else {
            gameDetails.firstPlayerChar = "O"
            gameDetails.firstPlayerMove = false
            gameDetails.secondPlayerMove = true
            gameDetails.secondPlayerChar = "X"
        }


        db.child(key!!).setValue(gameDetails)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }


    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val d = snapshot.getValue(GameDetails::class.java)
            _join.value = d
        }

        override fun onCancelled(error: DatabaseError) {}

    }

    fun realtimeUpdate() {
        db.child(key!!).addValueEventListener(valueEventListener)
    }

    override fun onCleared() {
        super.onCleared()
        db.removeEventListener(valueEventListener)
    }
}