package com.example.colorgame.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Insert
    suspend fun insertScore(score: Score)

    @Delete
    suspend fun deleteScore(score: Score)

    @Query("SELECT * FROM Score WHERE gameMode = 'hundredSec' ORDER BY score DESC")
    fun getAllScoresOfHundredSecMode(): Flow<List<Score>>

    @Query("SELECT * FROM Score WHERE gameMode = 'continuousRight' ORDER BY score DESC")
    fun getAllScoresOfContinuousRightMode(): Flow<List<Score>>

    @Query("SELECT * FROM Score WHERE gameMode = 'rightWrong' ORDER BY score DESC")
    fun getAllScoresOfRightWrongMode(): Flow<List<Score>>

    @Query("DELETE FROM Score WHERE gameMode = :gameMode")
    fun deleteScoresByGameMode(gameMode: String)
}