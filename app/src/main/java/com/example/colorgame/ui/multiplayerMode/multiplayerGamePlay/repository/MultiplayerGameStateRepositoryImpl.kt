package com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.repository

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.model.MultiplayerGameState

class MultiplayerGameStateRepositoryImpl:MultiplayerGameStateRepository {

    private val countDown = "countDownValue"
    private val playerScore = "playerScore"

    override fun loadGameState(savedInstanceState: Bundle?): MutableLiveData<MultiplayerGameState> {
        val infoMutableLiveData = MutableLiveData<MultiplayerGameState>()

        val savedCountDownValue = savedInstanceState?.getLong(countDown, 100L) ?:100L
        val correctScore = savedInstanceState?.getInt(playerScore, 0) ?: 0

        val multiplayerGameState = MultiplayerGameState(savedCountDownValue, correctScore)

        infoMutableLiveData.postValue(multiplayerGameState)

        return infoMutableLiveData
    }

    override fun saveGameState(savedInstanceState: Bundle, multiplayerGameState: MultiplayerGameState) {
        savedInstanceState.putLong(countDown, multiplayerGameState.countDownValue)
        savedInstanceState.putInt(playerScore, multiplayerGameState.playerScore)
    }

}