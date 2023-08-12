package com.example.colorgame.ui.mainMode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.domain.GamePlay
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentGamePlayBinding
import com.example.colorgame.domain.GamePlay.Companion.THREE_WRONG_MODE
import com.example.colorgame.ui.intro.IntroFragmentArgs
import com.example.colorgame.ui.mainMode.scoresHistory.view.ScoresHistoryFragmentArgs

class GamePlayFragment : Fragment() {
    private val argsOne: IntroFragmentArgs by navArgs()
    private val argsTwo: ScoresHistoryFragmentArgs by navArgs()
    private val argsThree: ResultFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentGamePlayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_play, container, false)
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)   /* DataStore instance */

        val gamePlay= GamePlay(lifecycleScope, requireActivity().baseContext)
        GamePlay.chosenBox = gamePlay.getNewUI(binding)
        gamePlay.setGamePlay(getCurrentGameMode(argsOne.gameMode,argsTwo.gameMode,argsThree.gameMode),binding,requireActivity().baseContext)

        /* Listen to GameOver Value */
        lifecycleScope.launchWhenStarted {
            dataStoreManager.isGameOver.collect { isGameOver -> if(isGameOver){ sendResult(binding,gamePlay) } }
        }

        return binding.root
    }

    private fun goToResultFragment(binding: FragmentGamePlayBinding, score: Int, gameMode: String){
        Navigation.findNavController(binding.root).navigate(GamePlayFragmentDirections.goToResultFragment(score,gameMode))
    }

    private fun sendResult(binding: FragmentGamePlayBinding,gamePlay: GamePlay){
        if(argsOne.gameMode==THREE_WRONG_MODE) goToResultFragment(binding,gamePlay.totalCorrectAnswers,argsOne.gameMode)
        else                                   goToResultFragment(binding,gamePlay.continuousRightAnswers,argsOne.gameMode)
    }

    private fun getCurrentGameMode(startGameMode: String,returnedGameMode: String,returnedGameModeTwo: String): String = if(startGameMode==""){ if(returnedGameMode=="") returnedGameModeTwo else returnedGameMode } else startGameMode

}



