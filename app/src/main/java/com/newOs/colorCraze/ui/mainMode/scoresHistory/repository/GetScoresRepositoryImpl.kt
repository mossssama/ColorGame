package com.newOs.colorCraze.ui.mainMode.scoresHistory.repository

import androidx.lifecycle.MutableLiveData
import com.newOs.colorCraze.room.Score
import com.newOs.colorCraze.ui.mainMode.scoresHistory.model.ScoreItem
import javax.inject.Inject

class GetScoresRepositoryImpl @Inject constructor() : GetScoresRepository {

    override fun getResults(scores: List<Score>): MutableLiveData<List<ScoreItem>> {
        val infoMutableLiveData = MutableLiveData<List<ScoreItem>>()
        infoMutableLiveData.postValue(convertScoreToScoreItem(scores))
        return infoMutableLiveData
    }

    private fun convertScoreToScoreItem(scores: List<Score>): List<ScoreItem> {
        val sortedScores = scores.sortedByDescending { it.score }
        return sortedScores.mapIndexed { index, score -> ScoreItem(rank = index + 1, data = score.date, score = score.score) }
    }

}