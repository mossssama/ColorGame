package com.example.colorgame.ui.multiplayerMode.multiplayerResult.repository

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MultiplayerGameResultRepositoryImpl : MultiplayerGameResultRepository {

    private val playerScore = "playerScore"
    private val oppositeScore = "oppositeScore"

    override fun saveMultiplayerGameResult(savedInstanceState: Bundle, multiplayerGameResult: MultiplayerGameResult) {
        savedInstanceState.putInt(playerScore, multiplayerGameResult.playerScore)
        savedInstanceState.putInt(oppositeScore, multiplayerGameResult.oppositeScore)
    }

    override fun loadMultiplayerGameResult(savedInstanceState: Bundle?): MutableLiveData<MultiplayerGameResult> {
        val infoMutableLiveData = MutableLiveData<MultiplayerGameResult>()

        val myScore = savedInstanceState?.getInt(playerScore, 0) ?:0
        val opponentScore = savedInstanceState?.getInt(oppositeScore, 0) ?: 0

        val multiplayerGameResult = MultiplayerGameResult(myScore,opponentScore)

        infoMutableLiveData.postValue(multiplayerGameResult)

        return infoMutableLiveData
    }

    override fun getMultiplayerGameResultFromDataStore(context: Context,lifecycleScope: CoroutineScope): MutableLiveData<MultiplayerGameResult> {
        val infoMutableLiveData = MutableLiveData<MultiplayerGameResult>()
        val dataStoreManager = DataStoreManager.getInstance(context)

        lifecycleScope.launch { infoMutableLiveData.postValue(dataStoreManager.readMultiplayerGameResult()) }

        return infoMutableLiveData
    }

    override fun getMultiplayerGameResultFromFirebase(fireStoreManager: FirestoreManager, myUserName: String, myFriendName: String): MutableLiveData<MultiplayerGameResult> {
        val infoMutableLiveData = MutableLiveData<MultiplayerGameResult>()

        fireStoreManager.readScore(myUserName, onSuccess = { myScore ->
            fireStoreManager.readScore(myFriendName, onSuccess = { myFriendScore ->
                infoMutableLiveData.postValue(MultiplayerGameResult(myScore,myFriendScore))
            }, onFailure = {
                infoMutableLiveData.postValue(MultiplayerGameResult(myScore,0))
            })
        }, onFailure = {
            infoMutableLiveData.postValue(MultiplayerGameResult(0,0))
        })

        return infoMutableLiveData
    }

}
