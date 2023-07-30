package com.example.colorgame.ui

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
import com.example.colorgame.GamePlay
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentGamePlayBinding


class GamePlayFragment : Fragment() {
    private val args: IntroFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
                        /* 100sec time limit ->  Right Answer +1 | Wrong Answer -1  */
                        GamePlay.HUNDRED_SEC_MODE -> {
                            val action = GamePlayFragmentDirections.navigateToCongratsFragment(gamePlay.continuousRightAnswers)
                            Navigation.findNavController(binding.root).navigate(action)
                        }
                        /* One chance  */
                        GamePlay.CONTINUOUS_RIGHT_MODE -> {
                            val action = GamePlayFragmentDirections.navigateToTryAgainFragment(gamePlay.continuousRightAnswers)
                            Navigation.findNavController(binding.root).navigate(action)
                        }
                        /* Three chances */
                        GamePlay.THREE_WRONG_MODE -> {
                            val action = GamePlayFragmentDirections.navigateToTryAgainFragment(gamePlay.totalCorrectAnswers)
                            Navigation.findNavController(binding.root).navigate(action)
                        }
                    }
                }
            }
        }

        return binding.root
    }

}



