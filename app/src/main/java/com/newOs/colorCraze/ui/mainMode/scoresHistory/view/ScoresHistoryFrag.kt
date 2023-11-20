package com.newOs.colorCraze.ui.mainMode.scoresHistory.view

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
import com.newOs.colorCraze.R
import com.newOs.colorCraze.dataStore.DataStoreManager
import com.newOs.colorCraze.databinding.FragmentScoresHistoryBinding
import com.newOs.colorCraze.room.ScoreDatabase
import com.newOs.colorCraze.ui.mainMode.result.view.ResultFragArgs
import com.newOs.colorCraze.ui.mainMode.scoresHistory.viewModel.GetScoresViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScoresHistoryFrag : Fragment() {
    private val args: ResultFragArgs by navArgs()
    private val viewModel: GetScoresViewModel by viewModels()

    private lateinit var binding: FragmentScoresHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scores_history, container, false)
        binding.lifecycleOwner = viewLifecycleOwner // This ensures LiveData updates are observed correctly.
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scoreDatabase = ScoreDatabase.getInstance(requireActivity().baseContext)
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.getResults(viewModel.readResults(scoreDatabase,args.gameMode)).observe(viewLifecycleOwner) {
                viewModel.newArrayList.addAll(it)                                          // Add the retrieved results to the list
                binding.recyclerView.adapter = RecyclerViewAdapter(viewModel.newArrayList) // Set the RecyclerView adapter here
            }
        }

        binding.playAgain.setOnClickListener {
            viewModel.setGameOverToFalse(dataStoreManager)
            goToGamePlayFragment()
        }
    }

    private fun goToGamePlayFragment(){
        Navigation.findNavController(binding.root).navigate(ScoresHistoryFragDirections.returnToGamePlayFragment(args.gameMode))
    }

}



