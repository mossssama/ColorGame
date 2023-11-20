package com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.viewModel

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.newOs.colorCraze.R
import com.newOs.colorCraze.dataStore.DataStoreManager
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.repository.MultiplayerGameResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiplayerGameResultViewModel @Inject constructor(private val multiplayerResultRepo: MultiplayerGameResultRepository):ViewModel(){

    private var firebaseDataLoaded = false
    val fireStoreManager = com.newOs.colorCraze.cloudFirestore.FirestoreManager(Firebase.firestore)

    fun getMultiplayerGameResult(fireStoreManager: com.newOs.colorCraze.cloudFirestore.FirestoreManager, myUserName:String, myFriendUserName:String, context: Context, lifecycleScope: CoroutineScope, activity:FragmentActivity): MutableLiveData<MultiplayerGameResult> {
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
    fun getBannerText(context:Context,myScore: Int,myFriendScore: Int):String = if (myScore >= myFriendScore) context.getString(
        R.string.winner) else context.getString(R.string.loser)
    private fun resetScore(myUserName: String){ fireStoreManager.setScoreToZero(myUserName, onSuccess = {}, onFailure = {}) }
    private fun setStartPlayingValue(myUserName: String,value:Boolean){ fireStoreManager.setStartPlaying(myUserName,value, onSuccess = {}, onFailure = {}) }


}
