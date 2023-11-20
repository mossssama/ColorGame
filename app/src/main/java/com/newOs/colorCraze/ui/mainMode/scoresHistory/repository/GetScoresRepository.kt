package com.newOs.colorCraze.ui.mainMode.scoresHistory.repository

import androidx.lifecycle.MutableLiveData
import com.newOs.colorCraze.room.Score
import com.newOs.colorCraze.ui.mainMode.scoresHistory.model.ScoreItem

interface GetScoresRepository {
    fun getResults(scores: List<Score>): MutableLiveData<List<ScoreItem>>
}