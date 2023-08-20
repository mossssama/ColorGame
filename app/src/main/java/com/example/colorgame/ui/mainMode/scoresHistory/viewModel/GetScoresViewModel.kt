package com.example.colorgame.ui.mainMode.scoresHistory.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.ui.mainMode.scoresHistory.repository.GetScoresRepository
import com.example.colorgame.room.Score
import com.example.colorgame.room.ScoreDatabase
import com.example.colorgame.ui.mainMode.scoresHistory.model.ScoreItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GetScoresViewModel @Inject constructor(private val getResultsRepo:GetScoresRepository): ViewModel() {

    var newArrayList:ArrayList<ScoreItem> = ArrayList()

    fun getResults(scores: List<Score>): LiveData<List<ScoreItem>> {
        clearArray()
        return getResultsRepo.getResults(scores)
    }

    suspend fun readResults(scoreDatabase: ScoreDatabase, gameMode: String): List<Score> = withContext(Dispatchers.IO) { scoreDatabase.scoreDao.getAllScoresByGameMode(gameMode) }

    fun setGameOverToFalse(dataStoreManager: DataStoreManager) { GlobalScope.launch { dataStoreManager.saveGameOver(false) } }

    private fun clearArray(){ newArrayList.clear() }
}