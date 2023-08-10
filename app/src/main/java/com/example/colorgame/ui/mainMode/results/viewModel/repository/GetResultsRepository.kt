package com.example.colorgame.ui.mainMode.results.viewModel.repository

import androidx.lifecycle.MutableLiveData
import com.example.colorgame.room.pojo.Score
import com.example.colorgame.ui.mainMode.results.model.ScoreItem

interface GetResultsRepository {
    fun getResults(scores: List<Score>): MutableLiveData<List<ScoreItem>>
}