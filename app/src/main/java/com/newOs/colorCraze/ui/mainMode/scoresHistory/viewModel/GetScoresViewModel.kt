package com.newOs.colorCraze.ui.mainMode.scoresHistory.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.newOs.colorCraze.dataStore.DataStoreManager
import com.newOs.colorCraze.room.Score
import com.newOs.colorCraze.room.ScoreDatabase
import com.newOs.colorCraze.ui.mainMode.scoresHistory.model.ScoreItem
import com.newOs.colorCraze.ui.mainMode.scoresHistory.repository.GetScoresRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GetScoresViewModel @Inject constructor(private val getResultsRepo: GetScoresRepository): ViewModel() {

    var newArrayList:ArrayList<ScoreItem> = ArrayList()

    fun getResults(scores: List<Score>): LiveData<List<ScoreItem>> {
        clearArray()
        return getResultsRepo.getResults(scores)
    }

    suspend fun readResults(scoreDatabase: ScoreDatabase, gameMode: String): List<Score> = withContext(Dispatchers.IO) { scoreDatabase.scoreDao.getAllScoresByGameMode(gameMode) }

    fun setGameOverToFalse(dataStoreManager: DataStoreManager) { GlobalScope.launch { dataStoreManager.saveGameOver(false) } }

    private fun clearArray(){ newArrayList.clear() }
}