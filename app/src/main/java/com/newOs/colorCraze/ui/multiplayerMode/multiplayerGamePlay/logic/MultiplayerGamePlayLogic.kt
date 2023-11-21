package com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.logic

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.newOs.colorCraze.R
import com.newOs.colorCraze.datastore.DataStoreManager
import com.newOs.colorCraze.domain.GamePlay
import com.newOs.colorCraze.helpers.Constants
import com.newOs.colorCraze.helpers.Functions
import com.newOs.colorCraze.room.Score
import kotlinx.android.synthetic.main.fragment_game_play.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MultiplayerGamePlayLogic(lifecycleScope: CoroutineScope, context: Context) : GamePlay(lifecycleScope, context) {

    private val dataStoreManager = DataStoreManager.getInstance(context)
    private val fireStoreManager = com.newOs.colorCraze.firebase.FirestoreManager(Firebase.firestore)

    init { lifecycleScope.launch { dataStoreManager.saveGameOver(false) } }

    /** Used in MultiPlayerGamePlayFrag */
    fun setMultiplayerGamePlay(gameMode: String,playerName: String,binding: View,context: Context?,seconds: Long){
        if(gameMode == Constants.HUNDRED_SEC_MODE) startMultiplayerCountDown(binding,context!!,seconds,playerName)
        multiPlayerModeBoxesOnClickListener(gameMode,playerName,binding,context!!)
    }
    private fun startMultiplayerCountDown(binding:View,context: Context,seconds: Long,playerName: String) {
        countdownTimer?.cancel() // Cancel any existing timers
        fireStoreManager.setCountDownToHundred(playerName, onSuccess = {}, onFailure = {})
        countdownTimer = object : CountDownTimer(seconds * 1000, 1000) {
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
                lifecycleScope.launch {
                    Functions.insertScoreToDatabase(
                        context,
                        Score(
                            Constants.HUNDRED_SEC_MODE,
                            continuousRightAnswers,
                            Functions.getCurrentDate()
                        )
                    )
                }
                lifecycleScope.launch { dataStoreManager.saveGameOver(true) }
            }    // Countdown has finished, you can perform any action here
        }.start()
    }   /* write in FireStore & DataStore & Room */
    private fun multiPlayerModeBoxesOnClickListener(gameMode: String, playerName: String, binding: View, context: Context) {
        multiPlayerBoxOnClickListener(binding.boxOne,gameMode,playerName,binding,context)
        multiPlayerBoxOnClickListener(binding.boxTwo,gameMode,playerName,binding,context)
        multiPlayerBoxOnClickListener(binding.boxThree,gameMode,playerName,binding,context)
        multiPlayerBoxOnClickListener(binding.boxFour,gameMode,playerName,binding,context)
        multiPlayerBoxOnClickListener(binding.boxFive,gameMode,playerName,binding,context)
        multiPlayerBoxOnClickListener(binding.boxSix,gameMode,playerName,binding,context)
        multiPlayerBoxOnClickListener(binding.boxSeven,gameMode,playerName,binding,context)
    }
    private fun multiPlayerBoxOnClickListener(boxView: View, gameMode: String, playerName: String, binding: View, context: Context) {
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
                Constants.CONTINUOUS_RIGHT_MODE -> super.continuousRightModeGamePlay(boxId,binding,context)
                Constants.HUNDRED_SEC_MODE -> hundredSecondMultiPlayerGamePlay(playerName,boxId,binding)
                Constants.THREE_WRONG_MODE -> super.threeWrongGamePlay(boxId,binding,context)
            }

        }
    }
    private fun hundredSecondMultiPlayerGamePlay(playerName: String, boxId: String,binding: View){
        if(chosenBox ==boxId) { fireStoreManager.incrementScore(playerName, onSuccess = {}, onFailure = {});  continuousRightAnswers++; }
        else { fireStoreManager.decrementScore(playerName, onSuccess = {}, onFailure = {}); continuousRightAnswers--; }
        chosenBox = super.getNewUI(binding)
    }   /* write in FireStore */


}