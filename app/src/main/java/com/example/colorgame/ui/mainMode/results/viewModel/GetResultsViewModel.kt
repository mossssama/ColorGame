package com.example.colorgame.ui.mainMode.results.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorgame.ui.mainMode.results.repository.GetResultsRepository
import com.example.colorgame.ui.mainMode.results.repository.GetResultsRepositoryImpl
import com.example.colorgame.room.Score
import com.example.colorgame.ui.mainMode.results.model.ScoreItem

class GetResultsViewModel: ViewModel() {
    private val getResultsRepo: GetResultsRepository = GetResultsRepositoryImpl()

    fun getResults(scores: List<Score>): LiveData<List<ScoreItem>> = getResultsRepo.getResults(scores)
}