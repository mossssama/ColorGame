package com.example.colorgame.ui.mainMode.scoresHistory.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorgame.ui.mainMode.scoresHistory.viewModel.GetScoresViewModel
import com.example.colorgame.R
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.ui.adapters.RecyclerViewAdapter
import com.example.colorgame.databinding.FragmentScoresHistoryBinding
import com.example.colorgame.room.ScoreDatabase
import com.example.colorgame.ui.mainMode.ResultFragmentArgs
import kotlinx.coroutines.launch

class ScoresHistoryFragment : Fragment() {
    private val args: ResultFragmentArgs by navArgs()

    private lateinit var binding: FragmentScoresHistoryBinding
    private lateinit var viewModel: GetScoresViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scores_history, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GetScoresViewModel::class.java]
        val scoreDatabase = ScoreDatabase.getInstance(requireActivity().baseContext)
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.getResults(viewModel.readResults(scoreDatabase,args.gameMode)).observe(requireActivity()) {
                viewModel.newArrayList.addAll(it)                                          // Add the retrieved results to the list
                binding.recyclerView.adapter = RecyclerViewAdapter(viewModel.newArrayList) // Set the RecyclerView adapter here
            }
        }

        binding.playAgain.setOnClickListener { viewModel.setGameOverToFalse(dataStoreManager); goToGamePlayFragment() }
    }

    private fun goToGamePlayFragment(){
        Navigation.findNavController(binding.root).navigate(ScoresHistoryFragmentDirections.returnToGamePlayFragment(args.gameMode))
    }

//    private fun goToIntroFragment(binding: FragmentScoresHistoryBinding){
//        Navigation.findNavController(binding.root).navigate(ScoresHistoryFragmentDirections.goToIntroFragment())
//    }



}



