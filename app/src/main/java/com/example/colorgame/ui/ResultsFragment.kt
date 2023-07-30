package com.example.colorgame.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentResultsBinding
import com.example.colorgame.room.ScoreDatabase
import kotlinx.coroutines.launch

class ResultsFragment : Fragment() {

    private val argsOne: CongratsFragmentArgs by navArgs()
    private val argsTwo: TryAgainFragmentArgs by navArgs()

    private lateinit var scoreDatabase: ScoreDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentResultsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_results,container,false)
        lateinit var gameMode: String

        /* To be used in the dao to display specific mode results in a recyclerView */
        if(argsOne.gameMode=="hundredSec") { gameMode=argsTwo.gameMode;     Toast.makeText(requireActivity().applicationContext,argsTwo.gameMode,Toast.LENGTH_LONG).show() }
        else {                               gameMode=argsOne.gameMode;     Toast.makeText(requireActivity().applicationContext,argsOne.gameMode,Toast.LENGTH_LONG).show() }

        scoreDatabase = ScoreDatabase.getInstance(requireActivity().baseContext)
        readNumberOfScores(gameMode)

        return binding.root
    }

    private fun readNumberOfScores(gameMode: String) {
        lifecycleScope.launch {
            scoreDatabase.scoreDao.getNumberOfScoresByGameMode(gameMode).collect{ Toast.makeText(requireContext(),"$it",Toast.LENGTH_LONG).show() }
        }
    }

}