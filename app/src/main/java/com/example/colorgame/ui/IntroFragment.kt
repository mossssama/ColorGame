package com.example.colorgame.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.colorgame.GamePlay
import com.example.colorgame.R
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.databinding.FragmentIntroBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class IntroFragment : Fragment() {

//    private lateinit var scoreDatabase: ScoreDatabase
    private lateinit var dataStoreManager: DataStoreManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentIntroBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro,container,false)
        dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)

        binding.correctRun.setOnClickListener {     navigateToGamePlayFragment(binding,GamePlay.CONTINUOUS_RIGHT_MODE)                  }
        binding.hundredSec.setOnClickListener {     navigateToGamePlayFragment(binding,GamePlay.HUNDRED_SEC_MODE)                       }
        binding.threeMistakes.setOnClickListener {  navigateToGamePlayFragment(binding,GamePlay.THREE_WRONG_MODE)                       }
        binding.multiplier.setOnClickListener {     Snackbar.make(binding.root,"Will be added later",Snackbar.LENGTH_LONG).show()  }

//        scoreDatabase = ScoreDatabase.getInstance(requireActivity().baseContext)
//        readNumberOfScores(THREE_WRONG_MODE)

        return binding.root
    }

    private fun navigateToGamePlayFragment(binding: FragmentIntroBinding,gameMode: String){
        val action = IntroFragmentDirections.navigateToGamePlayFragment(gameMode)
        Navigation.findNavController(binding.root).navigate(action)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { dataStoreManager.saveGameOver(false) }
    }

//    private fun readNumberOfScores(gameMode: String) {
//        lifecycleScope.launch {
//            scoreDatabase.scoreDao.getNumberOfScoresByGameMode(gameMode).collect{ Toast.makeText(requireContext(),"$it",Toast.LENGTH_LONG).show() }
//        }
//    }

}