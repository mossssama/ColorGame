package com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.model.MultiplayerGameState
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.repository.MultiplayerGameStateRepository
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.repository.MultiplayerGameStateRepositoryImpl

class MultiplayerGameStateViewModel: ViewModel()  {
    private val loadGameStateRepo: MultiplayerGameStateRepository = MultiplayerGameStateRepositoryImpl()
    fun loadGameState(savedInstanceState: Bundle?): LiveData<MultiplayerGameState> = loadGameStateRepo.loadGameState(savedInstanceState)

    private val saveGameStateRepo: MultiplayerGameStateRepository = MultiplayerGameStateRepositoryImpl()
    fun saveGameState(savedInstanceState: Bundle, multiplayerGameState: MultiplayerGameState) = saveGameStateRepo.saveGameState(savedInstanceState,multiplayerGameState)
}