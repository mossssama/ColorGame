package com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.repository

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.model.MultiplayerGameState

interface MultiplayerGameStateRepository {
    fun loadGameState(savedInstanceState: Bundle?): MutableLiveData<MultiplayerGameState>
    fun saveGameState(savedInstanceState: Bundle, multiplayerGameState: MultiplayerGameState)
}