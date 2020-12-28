package com.vango.tictactoe.ui.createLobby

import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.vango.tictactoe.models.Game
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateLobbyRepository @Inject constructor() {
    private var database: FirebaseDatabase
    private lateinit var gameReference: DatabaseReference
    init {
        database = Firebase.database

    }
    fun initializeGameInDB(lobbyId : String){
        gameReference = database.getReference("/$lobbyId")
        //myRef.setValue("Hello, World!")
        Log.i("initalize","yes")
        val game = Game(0)
        gameReference.setValue(game).addOnFailureListener {
            Log.i("blad",it.message.toString())
        }.addOnSuccessListener {
            Log.i("saved","")
        }.addOnCanceledListener {
            Log.i("canceled","")
        }
    }
    @ExperimentalCoroutinesApi
    fun getMoves(): Flow<Result<Game>> = callbackFlow{
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val game = dataSnapshot.getValue<Game>()
                Log.i("game",game?.active.toString())
                this@callbackFlow.sendBlocking(Result.success(game))
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                this@callbackFlow.sendBlocking(Result.failure<Exception>(databaseError.toException()))
                // ...
            }
        }
        gameReference.addValueEventListener(postListener)
        awaitClose {
            gameReference
                .removeEventListener(postListener)
        }
    }
}