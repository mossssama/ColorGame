package com.newOs.colorCraze.ui.mainMode.gamePlay.logic

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import com.newOs.colorCraze.R
import com.newOs.colorCraze.datastore.DataStoreManager
import com.newOs.colorCraze.domain.GamePlay
import com.newOs.colorCraze.helpers.Constants
import com.newOs.colorCraze.helpers.Functions
import com.newOs.colorCraze.room.Score
import kotlinx.android.synthetic.main.fragment_game_play.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GamePlayLogic(lifecycleScope: CoroutineScope, context: Context) : GamePlay(lifecycleScope, context) {

    private val dataStoreManager = DataStoreManager.getInstance(context)

    init { lifecycleScope.launch { dataStoreManager.saveGameOver(false) } }


    /** Used in SinglePlayerGamePlayFrag */
    fun setSinglePlayerGamePlay(gameMode: String, binding: View, context: Context, seconds: Long){
        if(gameMode == Constants.HUNDRED_SEC_MODE) startSinglePlayerCountDown(binding,context,seconds)
        singlePlayerModeBoxesOnClickListener(gameMode,binding,context)
    }
    private fun startSinglePlayerCountDown(binding: View, context: Context, seconds: Long) {
        countdownTimer?.cancel() // Cancel any existing timers
        countdownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = millisUntilFinished / 1000
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
    }   /* write in DataStore & Room */
    private fun singlePlayerModeBoxesOnClickListener(gameMode: String, binding: View, context: Context) {
        singlePlayerBoxOnClickListener(binding.boxOne,gameMode,binding,context)
        singlePlayerBoxOnClickListener(binding.boxTwo,gameMode,binding,context)
        singlePlayerBoxOnClickListener(binding.boxThree,gameMode,binding,context)
        singlePlayerBoxOnClickListener(binding.boxFour,gameMode,binding,context)
        singlePlayerBoxOnClickListener(binding.boxFive,gameMode,binding,context)
        singlePlayerBoxOnClickListener(binding.boxSix,gameMode,binding,context)
        singlePlayerBoxOnClickListener(binding.boxSeven,gameMode,binding,context)
    }
    private fun singlePlayerBoxOnClickListener(boxView: View, gameMode: String, binding: View, context: Context) {
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
                Constants.HUNDRED_SEC_MODE -> hundredSecondSinglePlayerGamePlay(boxId,binding)
                Constants.THREE_WRONG_MODE -> super.threeWrongGamePlay(boxId,binding,context)
            }

        }
    }
    private fun hundredSecondSinglePlayerGamePlay(boxId: String,binding: View){
        if(chosenBox ==boxId) { continuousRightAnswers++; }
        else { continuousRightAnswers--; }
        chosenBox = super.getNewUI(binding)
    }

}