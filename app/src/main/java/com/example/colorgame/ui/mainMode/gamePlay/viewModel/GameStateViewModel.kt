package com.example.colorgame.ui.mainMode.gamePlay.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorgame.ui.mainMode.gamePlay.model.GameState
import com.example.colorgame.ui.mainMode.gamePlay.repository.GameStateRepository
import com.example.colorgame.ui.mainMode.gamePlay.repository.GameStateRepositoryImpl

class GameStateViewModel:ViewModel() {
    private val loadGameStateRepo: GameStateRepository = GameStateRepositoryImpl()
    fun loadGameState(savedInstanceState: Bundle?): LiveData<GameState> = loadGameStateRepo.loadGameState(savedInstanceState)

    private val saveGameStateRepo: GameStateRepository = GameStateRepositoryImpl()
    fun saveGameState(savedInstanceState: Bundle,gameState: GameState) = saveGameStateRepo.saveGameState(savedInstanceState,gameState)
}