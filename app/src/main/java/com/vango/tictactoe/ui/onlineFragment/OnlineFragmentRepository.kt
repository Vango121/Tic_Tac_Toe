package com.vango.tictactoe.ui.onlineFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vango.tictactoe.models.Game
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class OnlineFragmentRepository @Inject constructor(@ApplicationContext val context: Context) {
    private var database: FirebaseDatabase
    private lateinit var gameReference: DatabaseReference

    init {
        database = Firebase.database
    }

    @ExperimentalCoroutinesApi
    fun initializeGameInDB(lobbyId: String): Flow<Boolean> = callbackFlow {

        val livedata = MutableLiveData<Boolean>()
        val myRef = database.getReference("/$lobbyId").child("active")

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    myRef.parent?.setValue(Game(1))
                    this@callbackFlow.sendBlocking(true)
                } else {
                    this@callbackFlow.sendBlocking(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                this@callbackFlow.sendBlocking(false)
            }
        }
        myRef.addValueEventListener(listener)
        awaitClose {
            myRef
                .removeEventListener(listener)
        }
    }
}