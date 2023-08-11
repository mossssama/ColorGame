package com.example.colorgame.ui.mainMode.scoresHistory.repository

import androidx.lifecycle.MutableLiveData
import com.example.colorgame.room.Score
import com.example.colorgame.ui.mainMode.scoresHistory.model.ScoreItem

interface GetScoresRepository {
    fun getResults(scores: List<Score>): MutableLiveData<List<ScoreItem>>
}