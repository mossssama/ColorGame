package com.example.colorgame.ui.multiplayerMode.multiplayerResult.viewModel

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.repository.MultiplayerGameResultRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiplayerGameResultViewModel @Inject constructor(private val multiplayerResultRepo: MultiplayerGameResultRepository):ViewModel(){

    private var firebaseDataLoaded = false
    val fireStoreManager = FirestoreManager(Firebase.firestore)

    fun getMultiplayerGameResult(fireStoreManager: FirestoreManager, myUserName:String, myFriendUserName:String, context: Context, lifecycleScope: CoroutineScope,activity:FragmentActivity): MutableLiveData<MultiplayerGameResult> {
        return if(firebaseDataLoaded) multiplayerResultRepo.getMultiplayerGameResultFromDataStore(context, lifecycleScope)
        else {
            val infoMutableLiveData = MutableLiveData<MultiplayerGameResult>()
            val dataStoreManager = DataStoreManager.getInstance(context)
            multiplayerResultRepo.getMultiplayerGameResultFromFirebase(fireStoreManager,myUserName,myFriendUserName).observe(activity){
                lifecycleScope.launch { dataStoreManager.saveMultiplayerGameResult(MultiplayerGameResult(it.playerScore, it.oppositeScore)) }
                firebaseDataLoaded=true
                infoMutableLiveData.postValue(it)
            }
            infoMutableLiveData
        }

    }

    fun saveMultiplayerGameResult(savedInstanceState: Bundle, multiplayerGameResult: MultiplayerGameResult) = multiplayerResultRepo.saveMultiplayerGameResult(savedInstanceState,multiplayerGameResult)

    fun loadMultiplayerGameResult(savedInstanceState: Bundle?) = multiplayerResultRepo.loadMultiplayerGameResult(savedInstanceState)


    fun rePlayInit(myUserName: String){ resetScore(myUserName); setStartPlayingValue(myUserName,true) }
    fun returnInit(myUserName: String){ setStartPlayingValue(myUserName,false) }
    fun getBannerText(myScore: Int,myFriendScore: Int):String = if (myScore >= myFriendScore) "Winner" else "Loser"
    private fun resetScore(myUserName: String){ fireStoreManager.setScoreToZero(myUserName, onSuccess = {}, onFailure = {}) }
    private fun setStartPlayingValue(myUserName: String,value:Boolean){ fireStoreManager.setStartPlaying(myUserName,value, onSuccess = {}, onFailure = {}) }


}
