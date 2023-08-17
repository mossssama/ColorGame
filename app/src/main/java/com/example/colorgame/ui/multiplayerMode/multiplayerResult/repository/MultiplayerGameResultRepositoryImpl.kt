package com.example.colorgame.ui.multiplayerMode.multiplayerResult.repository

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult

class MultiplayerGameResultRepositoryImpl : MultiplayerGameResultRepository {

    private val playerScore = "playerScore"
    private val oppositeScore = "oppositeScore"

    override fun saveMultiplayerGameResult(savedInstanceState: Bundle, multiplayerGameResult: MultiplayerGameResult) {
        savedInstanceState.putInt(playerScore, multiplayerGameResult.playerScore)
        savedInstanceState.putInt(oppositeScore, multiplayerGameResult.oppositeScore)
    }

    override fun loadMultiplayerGameResultFromBundle(savedInstanceState: Bundle?): LiveData<MultiplayerGameResult> {
        val infoMutableLiveData = MutableLiveData<MultiplayerGameResult>()

        val myScore = savedInstanceState?.getInt(playerScore, 0) ?:0
        val opponentScore = savedInstanceState?.getInt(oppositeScore, 0) ?: 0
        infoMutableLiveData.postValue(MultiplayerGameResult(myScore, opponentScore))

        return infoMutableLiveData
    }

    override fun loadMultiplayerGameResultFromFirebase(fireStoreManager: FirestoreManager,myUserName: String, myFriendName: String): MutableLiveData<MultiplayerGameResult> {
        val infoMutableLiveData = MutableLiveData<MultiplayerGameResult>()
        fireStoreManager.readScore(myUserName, onSuccess = { myScore ->
            fireStoreManager.readScore(myFriendName, onSuccess = { myFriendScore -> infoMutableLiveData.postValue(MultiplayerGameResult(myScore,myFriendScore)) }
                                                   , onFailure = { infoMutableLiveData.postValue(MultiplayerGameResult(myScore,0)) }) }
                                             , onFailure = { infoMutableLiveData.postValue(MultiplayerGameResult(0,0)) }
        )
        return infoMutableLiveData
    }

}
