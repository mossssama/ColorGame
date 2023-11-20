package com.newOs.colorCraze.ui.mainMode.gamePlay.repository

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.newOs.colorCraze.ui.mainMode.gamePlay.model.GameState

interface GameStateRepository {
    fun loadGameState(savedInstanceState: Bundle?): MutableLiveData<GameState>
    fun saveGameState(savedInstanceState: Bundle, gameState: GameState)
}
