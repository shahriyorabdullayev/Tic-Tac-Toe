package com.shahriyor.tic_tac_toe.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shahriyor.tic_tac_toe.data.GameDetails
import com.shahriyor.tic_tac_toe.utils.Constants.PATH_FIREBASE_DATABASE

class JoinViewModel : ViewModel() {

    private var _gameDetails = MutableLiveData<GameDetails>()
    val gameDetails: LiveData<GameDetails>
        get() = _gameDetails
    private val _keyDB = MutableLiveData<String?>()
    val keyDB: LiveData<String?>
        get() = _keyDB


    fun getRoomDetails(gameDetails: GameDetails, name: String, etrRoomId: String) {
        val db = FirebaseDatabase.getInstance().getReference(PATH_FIREBASE_DATABASE)

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (room in snapshot.children) {
                        if (room.key?.takeLast(5).toString() == etrRoomId) {
                            gameDetails.roomID = etrRoomId
                            val key = room.key!!
                            _keyDB.value = key
                            val d: GameDetails? = room.getValue(GameDetails::class.java)
                            Log.d("dsss", d.toString())
                            if (d != null) {
                                d.secondPlayerName = name
                            }
                            _gameDetails.value = d!!
                            db.child(key).setValue(d)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })


    }
}