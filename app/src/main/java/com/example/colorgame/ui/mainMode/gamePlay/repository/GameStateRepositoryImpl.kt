package com.example.colorgame.ui.mainMode.gamePlay.repository

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.colorgame.ui.mainMode.gamePlay.model.GameState


class GameStateRepositoryImpl : GameStateRepository {

    private val countDown = "countDownValue"
    private val correctScore = "correctScore"
    private val inCorrectScore = "inCorrectScore"

    override fun loadGameState(savedInstanceState: Bundle?): MutableLiveData<GameState> {
        val infoMutableLiveData = MutableLiveData<GameState>()

        val savedCountDownValue = savedInstanceState?.getLong(countDown, 100L) ?:100L
        val correctScore = savedInstanceState?.getInt(correctScore, 0) ?: 0
        val inCorrectScore = savedInstanceState?.getInt(inCorrectScore,0)?:0

        val gameState = GameState(savedCountDownValue, correctScore,inCorrectScore)

        infoMutableLiveData.postValue(gameState)

        return infoMutableLiveData
    }

    override fun saveGameState(savedInstanceState: Bundle, gameState: GameState) {
        savedInstanceState.putLong(countDown, gameState.countDownValue)
        savedInstanceState.putInt(correctScore, gameState.correctScore)
        savedInstanceState.putInt(inCorrectScore,gameState.inCorrectScore)
    }

}
