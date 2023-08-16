package com.example.colorgame.ui.mainMode.gamePlay.repository

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.colorgame.ui.mainMode.gamePlay.model.GameState

interface GameStateRepository {
    fun loadGameState(savedInstanceState: Bundle?): MutableLiveData<GameState>
    fun saveGameState(savedInstanceState: Bundle, gameState: GameState)
}
