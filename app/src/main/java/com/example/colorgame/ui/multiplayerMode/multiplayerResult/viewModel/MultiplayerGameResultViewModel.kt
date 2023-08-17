package com.example.colorgame.ui.multiplayerMode.multiplayerResult.viewModel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.repository.MultiplayerGameResultRepository
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.repository.MultiplayerGameResultRepositoryImpl

class MultiplayerGameResultViewModel :ViewModel(){

    private var firebaseDataLoaded = false

    private val loadMultiplayerResultRepo: MultiplayerGameResultRepository = MultiplayerGameResultRepositoryImpl()

    fun loadMultiplayerGameResult(savedInstanceState: Bundle?,fireStoreManager: FirestoreManager,myUserName:String,myFriendUserName:String): LiveData<MultiplayerGameResult> {
        return if(firebaseDataLoaded) loadMultiplayerResultRepo.loadMultiplayerGameResultFromBundle(savedInstanceState)
        else { firebaseDataLoaded=true; loadMultiplayerResultRepo.loadMultiplayerGameResultFromFirebase(fireStoreManager,myUserName,myFriendUserName) }
    }

    private val saveMultiplayerResultRepo: MultiplayerGameResultRepository = MultiplayerGameResultRepositoryImpl()
    fun saveMultiplayerGameResult(savedInstanceState: Bundle, multiplayerGameResult: MultiplayerGameResult) = saveMultiplayerResultRepo.saveMultiplayerGameResult(savedInstanceState,multiplayerGameResult)

}