package com.newOs.colorCraze.ui.mainMode.gamePlay.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.newOs.colorCraze.R
import com.newOs.colorCraze.datastore.DataStoreManager
import com.newOs.colorCraze.databinding.FragmentGamePlayBinding
import com.newOs.colorCraze.domain.GamePlayImpl
import com.newOs.colorCraze.helpers.Constants.HUNDRED_SEC_MODE
import com.newOs.colorCraze.helpers.Constants.THREE_WRONG_MODE
import com.newOs.colorCraze.ui.intro.HomeFragArgs
import com.newOs.colorCraze.ui.mainMode.gamePlay.model.GameState
import com.newOs.colorCraze.ui.mainMode.gamePlay.viewModel.GameStateViewModel
import com.newOs.colorCraze.ui.mainMode.result.view.ResultFragArgs
import com.newOs.colorCraze.ui.mainMode.scoresHistory.view.ScoresHistoryFragArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamePlayFrag : Fragment() {
    private val argsOne: HomeFragArgs by navArgs()
    private val argsTwo: ScoresHistoryFragArgs by navArgs()
    private val argsThree: ResultFragArgs by navArgs()

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: FragmentGamePlayBinding
    private lateinit var gamePlay: GamePlayImpl
    private lateinit var dataStoreManager: DataStoreManager
    private val viewModel: GameStateViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_play, container, false)
        binding.lifecycleOwner = viewLifecycleOwner // This ensures LiveData updates are observed correctly.

        dataStoreManager = DataStoreManager.create(requireContext().dataStore)
        lifecycleScope.launch { dataStoreManager.saveGameOver(false) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentGameMode = viewModel.getCurrentGameMode(argsOne.gameMode,argsTwo.gameMode,argsThree.gameMode)

        gamePlay= GamePlayImpl(requireActivity().baseContext)
        GamePlayImpl.chosenBox = gamePlay.getNewUI(binding.root)
        viewModel.setSinglePlayerGamePlay(currentGameMode,binding.root,requireActivity().baseContext,100,gamePlay)

        /* Listen to GameOver Value */
        lifecycleScope.launchWhenStarted { dataStoreManager.isGameOver.collect { isGameOver ->
                if(isGameOver){ sendResult() }
            }
        }
        
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val currentGameMode = viewModel.getCurrentGameMode(argsOne.gameMode,argsTwo.gameMode,argsThree.gameMode)

        val countDownValue = if(currentGameMode==HUNDRED_SEC_MODE) binding.countdownTextView.text.toString().toLong() else 100
        val currentScore = if(currentGameMode==THREE_WRONG_MODE) gamePlay.totalCorrectAnswers else gamePlay.continuousRightAnswers
        val currentWrongAnswers = gamePlay.totalInCorrectAnswers

        viewModel.saveGameState(outState, GameState(countDownValue,currentScore,currentWrongAnswers))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val currentGameMode = viewModel.getCurrentGameMode(argsOne.gameMode,argsTwo.gameMode,argsThree.gameMode)

        if (savedInstanceState != null) {
            viewModel.loadGameState(savedInstanceState).observe(viewLifecycleOwner){
                viewModel.setSinglePlayerGamePlay(currentGameMode, binding.root,requireActivity().baseContext, it.countDownValue,gamePlay)
                if(currentGameMode== THREE_WRONG_MODE) { gamePlay.totalCorrectAnswers = it.correctScore; gamePlay.totalInCorrectAnswers = it.inCorrectScore }
                else gamePlay.continuousRightAnswers = it.correctScore
            }

        }
    }

    private fun goToResultFragment(score: Int, gameMode: String){
        findNavController().navigate(GamePlayFragDirections.goToResultFragment(score, gameMode))
    }

    private fun sendResult(){
        if(argsOne.gameMode==THREE_WRONG_MODE) goToResultFragment(gamePlay.totalCorrectAnswers,argsOne.gameMode)
        else                                   goToResultFragment(gamePlay.continuousRightAnswers,argsOne.gameMode)
    }

}



