package com.example.colorgame.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.colorgame.GamePlay
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentGamePlayBinding


class GamePlayFragment : Fragment() {

    private val args: IntroFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentGamePlayBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_game_play, container, false)

        val gamePlay= GamePlay(lifecycleScope, requireActivity().baseContext)
        GamePlay.chosenBox = gamePlay.getNewUI(binding)
        gamePlay.setGamePlay(args.gameMode,binding,requireActivity().baseContext)

        return binding.root
    }
}



