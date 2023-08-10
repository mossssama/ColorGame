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

    @Query("SELECT * FROM Score WHERE gameMode = :gameMode ORDER BY score DESC")
    fun getAllScoresByGameMode(gameMode: String): List<Score>

    @Query("SELECT COUNT(*) FROM Score WHERE gameMode = :gameMode")
    fun getNumberOfScoresByGameMode(gameMode: String): Flow<Int>

    @Query("DELETE FROM Score WHERE gameMode = :gameMode")
    fun deleteScoresByGameMode(gameMode: String)

}