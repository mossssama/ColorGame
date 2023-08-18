package com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorgame.ads.AdsManager
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.model.MultiplayerGameState
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.repository.MultiplayerGameStateRepository
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.repository.MultiplayerGameStateRepositoryImpl
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MultiplayerGameStateViewModel: ViewModel()  {

    val fireStoreManager = FirestoreManager(Firebase.firestore)
    var shouldExecuteSetScoreToZero = true

    private val loadGameStateRepo: MultiplayerGameStateRepository = MultiplayerGameStateRepositoryImpl()
    fun loadGameState(savedInstanceState: Bundle?): LiveData<MultiplayerGameState> = loadGameStateRepo.loadGameState(savedInstanceState)

    private val saveGameStateRepo: MultiplayerGameStateRepository = MultiplayerGameStateRepositoryImpl()
    fun saveGameState(savedInstanceState: Bundle, multiplayerGameState: MultiplayerGameState) = saveGameStateRepo.saveGameState(savedInstanceState,multiplayerGameState)


    override fun onCleared() {
        super.onCleared()
        shouldExecuteSetScoreToZero = true
        fireStoreManager.removeAllScoreListeners()
        fireStoreManager.removeAllCountDownListeners()
    }

    fun setScoreToZero(myUserName: String){ fireStoreManager.setScoreToZero(myUserName, onSuccess = {}, onFailure = {}) }
    fun setCountDownToHundred(myUserName: String){ fireStoreManager.updateCountDown(myUserName,100, onSuccess = {}, onFailure = {}) }
    fun setGameOverToFalse(dataStoreManager: DataStoreManager) { GlobalScope.launch { dataStoreManager.saveGameOver(false) } }
    fun loadInterstitialAds(adsManager: AdsManager){ adsManager.loadInterstitialAds() }


}