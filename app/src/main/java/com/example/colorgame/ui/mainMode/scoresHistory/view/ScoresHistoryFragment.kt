package com.example.colorgame.ui.mainMode.scoresHistory.view

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorgame.ui.mainMode.scoresHistory.viewModel.GetScoresViewModel
import com.example.colorgame.R
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.ui.adapters.RecyclerViewAdapter
import com.example.colorgame.ui.mainMode.scoresHistory.model.ScoreItem
import com.example.colorgame.databinding.FragmentScoresHistoryBinding
import com.example.colorgame.room.Score
import com.example.colorgame.room.ScoreDatabase
import com.example.colorgame.ui.mainMode.ResultFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScoresHistoryFragment : Fragment() {

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentScoresHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_scores_history, container, false)
        val scoreDatabase = ScoreDatabase.getInstance(requireActivity().baseContext)
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)
        val gameMode = args.gameMode
        val newArrayList: ArrayList<ScoreItem> = ArrayList()

        val getResultsViewModel: GetScoresViewModel by viewModels()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            getResultsViewModel.getResults(readResults(scoreDatabase,gameMode)).observe(requireActivity()) {
                newArrayList.addAll(it)                                          // Add the retrieved results to the list
                binding.recyclerView.adapter = RecyclerViewAdapter(newArrayList) // Set the RecyclerView adapter here
            }
        }

        binding.playAgain.setOnClickListener { setGameOverToFalse(dataStoreManager); goToGamePlayFragment(binding) }

        return binding.root
    }

    private suspend fun readResults(scoreDatabase: ScoreDatabase,gameMode: String): List<Score> = withContext(Dispatchers.IO) { scoreDatabase.scoreDao.getAllScoresByGameMode(gameMode) }

//    private fun goToIntroFragment(binding: FragmentScoresHistoryBinding){
//        Navigation.findNavController(binding.root).navigate(ScoresHistoryFragmentDirections.goToIntroFragment())
//    }

    private fun goToGamePlayFragment(binding: FragmentScoresHistoryBinding){
        Navigation.findNavController(binding.root).navigate(ScoresHistoryFragmentDirections.returnToGamePlayFragment(args.gameMode))
    }

    /* Update DataStore */
    private fun setGameOverToFalse(dataStoreManager: DataStoreManager) {
        GlobalScope.launch { dataStoreManager.saveGameOver(false) }
    }

}



