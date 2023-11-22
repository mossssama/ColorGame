package com.newOs.colorCraze.ui.mainMode.gamePlay.viewModel

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newOs.colorCraze.R
import com.newOs.colorCraze.datastore.DataStoreManager
import com.newOs.colorCraze.domain.GamePlayImpl
import com.newOs.colorCraze.helpers.Constants
import com.newOs.colorCraze.helpers.Functions
import com.newOs.colorCraze.room.Score
import com.newOs.colorCraze.ui.mainMode.gamePlay.model.GameState
import com.newOs.colorCraze.ui.mainMode.gamePlay.repository.GameStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.fragment_game_play.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameStateViewModel @Inject constructor(
    private val gameStateRepo: GameStateRepository,
    ):ViewModel() {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    fun loadGameState(savedInstanceState: Bundle?): LiveData<GameState> = gameStateRepo.loadGameState(savedInstanceState)
    fun saveGameState(savedInstanceState: Bundle,gameState: GameState) = gameStateRepo.saveGameState(savedInstanceState,gameState)
    fun getCurrentGameMode(startGameMode: String,returnedGameMode: String,returnedGameModeTwo: String): String = if(startGameMode==""){ if(returnedGameMode=="") returnedGameModeTwo else returnedGameMode } else startGameMode
    fun setSinglePlayerGamePlay(gameMode: String, binding: View, context: Context, seconds: Long,gamePlay: GamePlayImpl){
        if(gameMode == Constants.HUNDRED_SEC_MODE) startSinglePlayerCountDown(binding,context,seconds,gamePlay)
        singlePlayerModeBoxesOnClickListener(gameMode,binding,context,gamePlay)
    }

    private fun startSinglePlayerCountDown(binding: View, context: Context, seconds: Long,gamePlay: GamePlayImpl) {
        val dataStoreManager = DataStoreManager.create(context.dataStore)                  // Pass the DataStore instance to DataStoreManager during initialization
        GamePlayImpl.countdownTimer?.cancel() // Cancel any existing timers
        GamePlayImpl.countdownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = millisUntilFinished / 1000
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
            }
        }.start()
    }
    private fun singlePlayerModeBoxesOnClickListener(gameMode: String, binding: View, context: Context,gamePlay: GamePlayImpl) {
        singlePlayerBoxOnClickListener(binding.boxOne,gameMode,binding,context,gamePlay)
        singlePlayerBoxOnClickListener(binding.boxTwo,gameMode,binding,context,gamePlay)
        singlePlayerBoxOnClickListener(binding.boxThree,gameMode,binding,context,gamePlay)
        singlePlayerBoxOnClickListener(binding.boxFour,gameMode,binding,context,gamePlay)
        singlePlayerBoxOnClickListener(binding.boxFive,gameMode,binding,context,gamePlay)
        singlePlayerBoxOnClickListener(binding.boxSix,gameMode,binding,context,gamePlay)
        singlePlayerBoxOnClickListener(binding.boxSeven,gameMode,binding,context,gamePlay)
    }
    private fun singlePlayerBoxOnClickListener(boxView: View, gameMode: String, binding: View, context: Context,gamePlay: GamePlayImpl) {
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
                Constants.HUNDRED_SEC_MODE -> hundredSecondSinglePlayerGamePlay(boxId,binding,gamePlay)
                Constants.THREE_WRONG_MODE -> gamePlay.threeWrongGamePlay(boxId,binding,context,dataStoreManager,viewModelScope)
            }

        }
    }
    private fun hundredSecondSinglePlayerGamePlay(boxId: String,binding: View,gamePlay: GamePlayImpl){
        if(GamePlayImpl.chosenBox ==boxId) { gamePlay.continuousRightAnswers++; }
        else { gamePlay.continuousRightAnswers--; }
        GamePlayImpl.chosenBox = gamePlay.getNewUI(binding)
    }

}