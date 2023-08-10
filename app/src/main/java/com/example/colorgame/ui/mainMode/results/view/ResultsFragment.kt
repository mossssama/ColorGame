package com.example.colorgame.ui.mainMode.results.view

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
import com.example.colorgame.ui.mainMode.results.viewModel.GetResultsViewModel
import com.example.colorgame.R
import com.example.colorgame.ui.adapters.RecyclerViewAdapter
import com.example.colorgame.ui.mainMode.results.model.ScoreItem
import com.example.colorgame.databinding.FragmentResultsBinding
import com.example.colorgame.room.pojo.Score
import com.example.colorgame.room.ScoreDatabase
import com.example.colorgame.ui.mainMode.CongratsFragmentArgs
import com.example.colorgame.ui.mainMode.TryAgainFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultsFragment : Fragment() {

    private val argsOne: CongratsFragmentArgs by navArgs()
    private val argsTwo: TryAgainFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentResultsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_results, container, false)
        val scoreDatabase = ScoreDatabase.getInstance(requireActivity().baseContext)
        val gameMode = if (argsOne.gameMode == "hundredSec") argsTwo.gameMode else argsOne.gameMode
        val newArrayList: ArrayList<ScoreItem> = ArrayList()

        val getResultsViewModel: GetResultsViewModel by viewModels()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            getResultsViewModel.getResults(readResults(scoreDatabase,gameMode)).observe(requireActivity()) {
                newArrayList.addAll(it)                                          // Add the retrieved results to the list
                binding.recyclerView.adapter = RecyclerViewAdapter(newArrayList) // Set the RecyclerView adapter here
            }
        }

        binding.back.setOnClickListener { goToIntroFragment(binding) }

        return binding.root
    }

    private suspend fun readResults(scoreDatabase: ScoreDatabase,gameMode: String): List<Score> = withContext(Dispatchers.IO) { scoreDatabase.scoreDao.getAllScoresByGameMode(gameMode) }

    private fun goToIntroFragment(binding: FragmentResultsBinding){
        Navigation.findNavController(binding.root).navigate(ResultsFragmentDirections.navigateToIntroFragment())
    }

}



