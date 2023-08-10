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
import com.example.colorgame.domain.GamePlay.Companion.CONTINUOUS_RIGHT_MODE
import com.example.colorgame.domain.GamePlay.Companion.HUNDRED_SEC_MODE
import com.example.colorgame.domain.GamePlay.Companion.THREE_WRONG_MODE
import com.example.colorgame.ui.intro.IntroFragmentArgs

class GamePlayFragment : Fragment() {
    private val args: IntroFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentGamePlayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_play, container, false)
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)   /* DataStore instance */

        val gamePlay= GamePlay(lifecycleScope, requireActivity().baseContext)
        GamePlay.chosenBox = gamePlay.getNewUI(binding)
        gamePlay.setGamePlay(args.gameMode,binding,requireActivity().baseContext)

        /* Listen to GameOver Value */
        lifecycleScope.launchWhenStarted {
            dataStoreManager.isGameOver.collect { isGameOver ->
                if(isGameOver){
                    when (args.gameMode) {
                        HUNDRED_SEC_MODE -> {      goToCongratsFragment(binding,gamePlay.continuousRightAnswers,args.gameMode) }        /* 100sec time limit ->  Right Answer +1 | Wrong Answer -1  */
                        CONTINUOUS_RIGHT_MODE -> { goToTryAgainFragment(binding,gamePlay.continuousRightAnswers,args.gameMode) }        /* One chance  */
                        THREE_WRONG_MODE -> {      goToTryAgainFragment(binding,gamePlay.totalCorrectAnswers,args.gameMode)    }        /* Three chances */
                    }
                }
            }
        }

        return binding.root
    }

    private fun goToTryAgainFragment(binding: FragmentGamePlayBinding,score: Int,gameMode: String){
        Navigation.findNavController(binding.root).navigate(GamePlayFragmentDirections.navigateToTryAgainFragment(score, gameMode))
    }

    private fun goToCongratsFragment(binding: FragmentGamePlayBinding,score: Int,gameMode: String){
        Navigation.findNavController(binding.root).navigate(GamePlayFragmentDirections.navigateToCongratsFragment(score, gameMode))
    }

}



