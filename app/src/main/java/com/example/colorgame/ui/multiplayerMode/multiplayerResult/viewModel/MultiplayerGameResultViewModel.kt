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
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.repository.MultiplayerGameResultRepositoryImpl
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MultiplayerGameResultViewModel :ViewModel(){

    private var firebaseDataLoaded = false
    val fireStoreManager = FirestoreManager(Firebase.firestore)

    private val getMultiplayerResultRepo: MultiplayerGameResultRepository = MultiplayerGameResultRepositoryImpl()
    private val saveMultiplayerResultRepo: MultiplayerGameResultRepository = MultiplayerGameResultRepositoryImpl()
    private val loadMultiplayerResultRepo: MultiplayerGameResultRepository = MultiplayerGameResultRepositoryImpl()

    fun getMultiplayerGameResult(fireStoreManager: FirestoreManager, myUserName:String, myFriendUserName:String, context: Context, lifecycleScope: CoroutineScope,activity:FragmentActivity): MutableLiveData<MultiplayerGameResult> {
        return if(firebaseDataLoaded) getMultiplayerResultRepo.getMultiplayerGameResultFromDataStore(context, lifecycleScope)
        else {
            val infoMutableLiveData = MutableLiveData<MultiplayerGameResult>()
            val dataStoreManager = DataStoreManager.getInstance(context)
            getMultiplayerResultRepo.getMultiplayerGameResultFromFirebase(fireStoreManager,myUserName,myFriendUserName).observe(activity){
                lifecycleScope.launch { dataStoreManager.saveMultiplayerGameResult(MultiplayerGameResult(it.playerScore, it.oppositeScore)) }
                firebaseDataLoaded=true
                infoMutableLiveData.postValue(it)
            }
            infoMutableLiveData
        }

    }

    fun saveMultiplayerGameResult(savedInstanceState: Bundle, multiplayerGameResult: MultiplayerGameResult) = saveMultiplayerResultRepo.saveMultiplayerGameResult(savedInstanceState,multiplayerGameResult)

    fun loadMultiplayerGameResult(savedInstanceState: Bundle?) = loadMultiplayerResultRepo.loadMultiplayerGameResult(savedInstanceState)


    fun rePlayInit(myUserName: String){ resetScore(myUserName); setStartPlayingValue(myUserName,true) }
    fun returnInit(myUserName: String){ setStartPlayingValue(myUserName,false) }
    fun getBannerText(myScore: Int,myFriendScore: Int):String = if (myScore >= myFriendScore) "Winner" else "Loser"
    private fun resetScore(myUserName: String){ fireStoreManager.setScoreToZero(myUserName, onSuccess = {}, onFailure = {}) }
    private fun setStartPlayingValue(myUserName: String,value:Boolean){ fireStoreManager.setStartPlaying(myUserName,value, onSuccess = {}, onFailure = {}) }


}
