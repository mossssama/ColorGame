package com.example.colorgame.ui.multiplayerMode.multiplayerResult.repository

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult
import kotlinx.coroutines.CoroutineScope

interface MultiplayerGameResultRepository {
    fun saveMultiplayerGameResult(savedInstanceState: Bundle, multiplayerGameResult: MultiplayerGameResult)
    fun loadMultiplayerGameResult(savedInstanceState: Bundle?): MutableLiveData<MultiplayerGameResult>

    fun getMultiplayerGameResultFromDataStore(context: Context,lifecycleScope: CoroutineScope): MutableLiveData<MultiplayerGameResult>
    fun getMultiplayerGameResultFromFirebase(fireStoreManager: FirestoreManager, myUserName: String, myFriendName: String): MutableLiveData<MultiplayerGameResult>
}