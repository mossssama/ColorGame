package com.example.colorgame.ui.mainMode.gamePlay.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.domain.GamePlay
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentGamePlayBinding
import com.example.colorgame.domain.GamePlay.Companion.HUNDRED_SEC_MODE
import com.example.colorgame.domain.GamePlay.Companion.THREE_WRONG_MODE
import com.example.colorgame.ui.intro.IntroFragmentArgs
import com.example.colorgame.ui.mainMode.ResultFragmentArgs
import com.example.colorgame.ui.mainMode.gamePlay.model.GameState
import com.example.colorgame.ui.mainMode.gamePlay.viewModel.GameStateViewModel
import com.example.colorgame.ui.mainMode.scoresHistory.view.ScoresHistoryFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamePlayFragment : Fragment() {
    private val argsOne: IntroFragmentArgs by navArgs()
    private val argsTwo: ScoresHistoryFragmentArgs by navArgs()
    private val argsThree: ResultFragmentArgs by navArgs()

    private lateinit var binding: FragmentGamePlayBinding
    private val viewModel: GameStateViewModel by viewModels()
    private lateinit var gamePlay: GamePlay

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_play, container, false)
        binding.lifecycleOwner = viewLifecycleOwner // This ensures LiveData updates are observed correctly.
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)
        val currentGameMode = viewModel.getCurrentGameMode(argsOne.gameMode,argsTwo.gameMode,argsThree.gameMode)

        gamePlay= GamePlay(lifecycleScope, requireActivity().baseContext)
        GamePlay.chosenBox = gamePlay.getNewUI(binding)
        gamePlay.setGamePlay(currentGameMode,binding,requireActivity().baseContext,100)

        /* Listen to GameOver Value */
        lifecycleScope.launchWhenStarted { dataStoreManager.isGameOver.collect { isGameOver -> if(isGameOver){ sendResult(gamePlay) } } }
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
                gamePlay.setGamePlay(currentGameMode, binding, requireActivity().baseContext, it.countDownValue)

                if(currentGameMode== THREE_WRONG_MODE) { gamePlay.totalCorrectAnswers = it.correctScore; gamePlay.totalInCorrectAnswers = it.inCorrectScore }
                else gamePlay.continuousRightAnswers = it.correctScore
            }
        }

    }

    private fun goToResultFragment(score: Int, gameMode: String){
        Navigation.findNavController(binding.root).navigate(GamePlayFragmentDirections.goToResultFragment(score, gameMode))
    }

    private fun sendResult(gamePlay: GamePlay){
        if(argsOne.gameMode==THREE_WRONG_MODE) goToResultFragment(gamePlay.totalCorrectAnswers,argsOne.gameMode)
        else                                   goToResultFragment(gamePlay.continuousRightAnswers,argsOne.gameMode)
    }

}



