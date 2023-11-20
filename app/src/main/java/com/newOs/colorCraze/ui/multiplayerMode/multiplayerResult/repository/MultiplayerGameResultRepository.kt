package com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.repository

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult
import kotlinx.coroutines.CoroutineScope

interface MultiplayerGameResultRepository {
    fun saveMultiplayerGameResult(savedInstanceState: Bundle, multiplayerGameResult: MultiplayerGameResult)
    fun loadMultiplayerGameResult(savedInstanceState: Bundle?): MutableLiveData<MultiplayerGameResult>

    fun getMultiplayerGameResultFromDataStore(context: Context,lifecycleScope: CoroutineScope): MutableLiveData<MultiplayerGameResult>
    fun getMultiplayerGameResultFromFirebase(fireStoreManager: com.newOs.colorCraze.cloudFirestore.FirestoreManager, myUserName: String, myFriendName: String): MutableLiveData<MultiplayerGameResult>
}