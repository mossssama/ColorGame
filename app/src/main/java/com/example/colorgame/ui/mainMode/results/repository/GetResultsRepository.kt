package com.example.colorgame.ui.mainMode.results.repository

import androidx.lifecycle.MutableLiveData
import com.example.colorgame.room.Score
import com.example.colorgame.ui.mainMode.results.model.ScoreItem

interface GetResultsRepository {
    fun getResults(scores: List<Score>): MutableLiveData<List<ScoreItem>>
}