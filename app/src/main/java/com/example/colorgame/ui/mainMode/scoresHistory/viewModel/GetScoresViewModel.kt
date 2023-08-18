package com.example.colorgame.ui.mainMode.scoresHistory.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.ui.mainMode.scoresHistory.repository.GetScoresRepository
import com.example.colorgame.ui.mainMode.scoresHistory.repository.GetScoresRepositoryImpl
import com.example.colorgame.room.Score
import com.example.colorgame.room.ScoreDatabase
import com.example.colorgame.ui.mainMode.scoresHistory.model.ScoreItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GetScoresViewModel: ViewModel() {
    val newArrayList: ArrayList<ScoreItem> = ArrayList()

    private val getResultsRepo: GetScoresRepository = GetScoresRepositoryImpl()

    fun getResults(scores: List<Score>): LiveData<List<ScoreItem>> = getResultsRepo.getResults(scores)


    fun setGameOverToFalse(dataStoreManager: DataStoreManager) { GlobalScope.launch { dataStoreManager.saveGameOver(false) } }
    suspend fun readResults(scoreDatabase: ScoreDatabase, gameMode: String): List<Score> = withContext(Dispatchers.IO) { scoreDatabase.scoreDao.getAllScoresByGameMode(gameMode) }
}