package com.example.colorgame.ui.results

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorgame.R
import com.example.colorgame.ui.adapters.RecyclerViewAdapter
import com.example.colorgame.room.pojo.ScoreItem
import com.example.colorgame.databinding.FragmentResultsBinding
import com.example.colorgame.room.pojo.Score
import com.example.colorgame.room.ScoreDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultsFragment : Fragment() {

    private val argsOne: CongratsFragmentArgs by navArgs()
    private val argsTwo: TryAgainFragmentArgs by navArgs()

    private lateinit var scoreDatabase: ScoreDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentResultsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_results, container, false)
        val gameMode = if (argsOne.gameMode == "hundredSec") argsTwo.gameMode else argsOne.gameMode
        val newArrayList: ArrayList<ScoreItem> = ArrayList()
        scoreDatabase = ScoreDatabase.getInstance(requireActivity().baseContext)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val results = getResults(readResults(gameMode))
            newArrayList.addAll(results) // Add the retrieved results to the list
            binding.recyclerView.adapter = RecyclerViewAdapter(newArrayList) // Set the RecyclerView adapter here
        }
        return binding.root
    }


    private suspend fun readResults(gameMode: String): List<Score> {
        return withContext(Dispatchers.IO) {
            scoreDatabase.scoreDao.getAllScoresByGameMode(gameMode)
        }
    }

    private fun getResults(scores: List<Score>): List<ScoreItem> {
        val sortedScores = scores.sortedByDescending { it.score }
        return sortedScores.mapIndexed { index, score -> ScoreItem(rank = index + 1, data = score.date, score = score.score) }
    }
}



