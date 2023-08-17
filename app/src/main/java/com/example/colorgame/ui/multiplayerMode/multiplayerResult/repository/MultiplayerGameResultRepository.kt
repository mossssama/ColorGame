package com.example.colorgame.ui.multiplayerMode.multiplayerResult.repository

import android.os.Bundle
import androidx.lifecycle.LiveData
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult

interface MultiplayerGameResultRepository {
    fun saveMultiplayerGameResult(savedInstanceState: Bundle, multiplayerGameResult: MultiplayerGameResult)

    fun loadMultiplayerGameResultFromBundle(savedInstanceState: Bundle?): LiveData<MultiplayerGameResult>
    fun loadMultiplayerGameResultFromFirebase(fireStoreManager: FirestoreManager,myUserName: String, myFriendName: String): LiveData<MultiplayerGameResult>
}