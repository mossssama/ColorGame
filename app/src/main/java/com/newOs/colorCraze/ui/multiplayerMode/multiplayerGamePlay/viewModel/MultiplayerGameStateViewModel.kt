package com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.viewModel

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.newOs.colorCraze.R
import com.newOs.colorCraze.datastore.DataStoreManager
import com.newOs.colorCraze.domain.GamePlayImpl
import com.newOs.colorCraze.firebase.FirestoreManager
import com.newOs.colorCraze.helpers.Constants
import com.newOs.colorCraze.helpers.Functions
import com.newOs.colorCraze.room.Score
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.model.MultiplayerGameState
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.repository.MultiplayerGameStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.fragment_game_play.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiplayerGameStateViewModel @Inject constructor(
    private val gameStateRepo: MultiplayerGameStateRepository,
    ): ViewModel()  {

    val fireStoreManager = FirestoreManager(Firebase.firestore)
    var shouldExecuteSetScoreToZero = true

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")        // Create a DataStore instance using preferencesDataStore extension

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
    fun setGameOverToFalse(dataStoreManager: DataStoreManager) { viewModelScope.launch { dataStoreManager.saveGameOver(false) } }
    fun showInterstitialAds(activity: FragmentActivity, adsManager: com.newOs.colorCraze.ads.AdsManager, myName: String, myFriendName: String, myScore: Int, myFriendScore:Int, unitId: String){ adsManager.showInterstitialAds(activity,myName,myFriendName,myScore,myFriendScore,unitId) }


    /** Used in MultiPlayerGamePlayFrag */
    fun setMultiplayerGamePlay(gameMode: String, playerName: String, binding: View, context: Context?, seconds: Long,gamePlay: GamePlayImpl){
        if(gameMode == Constants.HUNDRED_SEC_MODE) startMultiplayerCountDown(binding,context!!,seconds,playerName,gamePlay)
        multiPlayerModeBoxesOnClickListener(gameMode,playerName,binding,context!!,gamePlay)
    }
    private fun startMultiplayerCountDown(binding: View, context: Context, seconds: Long, playerName: String,gamePlay: GamePlayImpl) {
        val dataStoreManager = DataStoreManager.create(context.dataStore)                  // Pass the DataStore instance to DataStoreManager during initialization

        GamePlayImpl.countdownTimer?.cancel() // Cancel any existing timers
        fireStoreManager.setCountDownToHundred(playerName, onSuccess = {}, onFailure = {})
        GamePlayImpl.countdownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = millisUntilFinished / 1000
                if(remainingSeconds.toInt()==0) {
                    fireStoreManager.updateCountDown(playerName,0, onSuccess = {}, onFailure = {})
                    fireStoreManager.setStartPlaying(playerName,false, onSuccess = {}, onFailure = {})
                }
                binding.countdownTextView.text = remainingSeconds.toString()        // Update the TextView with the remaining seconds
            }
            override fun onFinish() {
                binding.countdownTextView.text = "0"
                viewModelScope.launch {
                    Functions.insertScoreToDatabase(
                        context,
                        Score(
                            Constants.HUNDRED_SEC_MODE,
                            gamePlay.continuousRightAnswers,
                            Functions.getCurrentDate()
                        )
                    )
                }
                viewModelScope.launch { dataStoreManager.saveGameOver(true) }
            }    // Countdown has finished, you can perform any action here
        }.start()
    }   /* write in FireStore & DataStore & Room */
    private fun multiPlayerModeBoxesOnClickListener(gameMode: String, playerName: String, binding: View, context: Context,gamePlay: GamePlayImpl) {
        multiPlayerBoxOnClickListener(binding.boxOne,gameMode,playerName,binding,context,gamePlay)
        multiPlayerBoxOnClickListener(binding.boxTwo,gameMode,playerName,binding,context,gamePlay)
        multiPlayerBoxOnClickListener(binding.boxThree,gameMode,playerName,binding,context,gamePlay)
        multiPlayerBoxOnClickListener(binding.boxFour,gameMode,playerName,binding,context,gamePlay)
        multiPlayerBoxOnClickListener(binding.boxFive,gameMode,playerName,binding,context,gamePlay)
        multiPlayerBoxOnClickListener(binding.boxSix,gameMode,playerName,binding,context,gamePlay)
        multiPlayerBoxOnClickListener(binding.boxSeven,gameMode,playerName,binding,context,gamePlay)
    }
    private fun multiPlayerBoxOnClickListener(boxView: View, gameMode: String, playerName: String, binding: View, context: Context,gamePlay: GamePlayImpl) {
        val dataStoreManager = DataStoreManager.create(context.dataStore)                  // Pass the DataStore instance to DataStoreManager during initialization
        boxView.setOnClickListener {
            val boxId = when (boxView.id) {
                R.id.boxOne -> Constants.BOX_ONE
                R.id.boxTwo -> Constants.BOX_TWO
                R.id.boxThree -> Constants.BOX_THREE
                R.id.boxFour -> Constants.BOX_FOUR
                R.id.boxFive -> Constants.BOX_FIVE
                R.id.boxSix -> Constants.BOX_SIX
                R.id.boxSeven -> Constants.BOX_SEVEN
                else -> return@setOnClickListener
            }

            when (gameMode){
                Constants.CONTINUOUS_RIGHT_MODE -> gamePlay.continuousRightModeGamePlay(boxId,binding,context,dataStoreManager,viewModelScope)
                Constants.HUNDRED_SEC_MODE -> hundredSecondMultiPlayerGamePlay(playerName,boxId,binding,gamePlay)
                Constants.THREE_WRONG_MODE -> gamePlay.threeWrongGamePlay(boxId,binding,context,dataStoreManager,viewModelScope)
            }

        }
    }
    private fun hundredSecondMultiPlayerGamePlay(playerName: String, boxId: String,binding: View,gamePlay: GamePlayImpl){
        if(GamePlayImpl.chosenBox ==boxId) { fireStoreManager.incrementScore(playerName, onSuccess = {}, onFailure = {});  gamePlay.continuousRightAnswers++; }
        else { fireStoreManager.decrementScore(playerName, onSuccess = {}, onFailure = {}); gamePlay.continuousRightAnswers--; }
        GamePlayImpl.chosenBox = gamePlay.getNewUI(binding)
    }   /* write in FireStore */

}