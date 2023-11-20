package com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.repository

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.model.MultiplayerGameState

interface MultiplayerGameStateRepository {
    fun loadGameState(savedInstanceState: Bundle?): MutableLiveData<MultiplayerGameState>
    fun saveGameState(savedInstanceState: Bundle, multiplayerGameState: MultiplayerGameState)
}