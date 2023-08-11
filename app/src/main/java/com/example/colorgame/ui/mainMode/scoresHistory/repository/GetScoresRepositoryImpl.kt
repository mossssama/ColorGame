package com.example.colorgame.ui.mainMode.scoresHistory.repository

import androidx.lifecycle.MutableLiveData
import com.example.colorgame.room.Score
import com.example.colorgame.ui.mainMode.scoresHistory.model.ScoreItem

class GetScoresRepositoryImpl : GetScoresRepository {
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