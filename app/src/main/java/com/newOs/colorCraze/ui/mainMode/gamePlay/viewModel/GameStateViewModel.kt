package com.newOs.colorCraze.ui.mainMode.gamePlay.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.newOs.colorCraze.ui.mainMode.gamePlay.model.GameState
import com.newOs.colorCraze.ui.mainMode.gamePlay.repository.GameStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameStateViewModel @Inject constructor(private val gameStateRepo: GameStateRepository):ViewModel() {

    fun loadGameState(savedInstanceState: Bundle?): LiveData<GameState> = gameStateRepo.loadGameState(savedInstanceState)
    fun saveGameState(savedInstanceState: Bundle,gameState: GameState) = gameStateRepo.saveGameState(savedInstanceState,gameState)
    fun getCurrentGameMode(startGameMode: String,returnedGameMode: String,returnedGameModeTwo: String): String = if(startGameMode==""){ if(returnedGameMode=="") returnedGameModeTwo else returnedGameMode } else startGameMode

}