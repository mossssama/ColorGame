package com.example.colorgame.ui.mainMode.scoresHistory.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorgame.ui.mainMode.scoresHistory.repository.GetScoresRepository
import com.example.colorgame.ui.mainMode.scoresHistory.repository.GetScoresRepositoryImpl
import com.example.colorgame.room.Score
import com.example.colorgame.ui.mainMode.scoresHistory.model.ScoreItem

class GetScoresViewModel: ViewModel() {
    private val getResultsRepo: GetScoresRepository = GetScoresRepositoryImpl()

    fun getResults(scores: List<Score>): LiveData<List<ScoreItem>> = getResultsRepo.getResults(scores)
}