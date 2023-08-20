package com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.viewModel

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiplayerGameStateViewModel @Inject constructor(private val gameStateRepo: MultiplayerGameStateRepository): ViewModel()  {

    val fireStoreManager = FirestoreManager(Firebase.firestore)
    var shouldExecuteSetScoreToZero = true

    fun loadGameState(savedInstanceState: Bundle?): LiveData<MultiplayerGameState> = gameStateRepo.loadGameState(savedInstanceState)
    fun saveGameState(savedInstanceState: Bundle, multiplayerGameState: MultiplayerGameState) = gameStateRepo.saveGameState(savedInstanceState,multiplayerGameState)

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
    fun showInterstitialAds(activity: FragmentActivity, adsManager: AdsManager, myName: String, myFriendName: String, myScore: Int, myFriendScore:Int){ adsManager.showInterstitialAds(activity,myName,myFriendName,myScore,myFriendScore) }

}