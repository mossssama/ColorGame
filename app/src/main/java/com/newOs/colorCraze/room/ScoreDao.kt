package com.newOs.colorCraze.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {
    @Insert
    suspend fun insertScore(score: Score)

    @Query("SELECT * FROM Score WHERE gameMode = :gameMode ORDER BY score DESC")
    fun getAllScoresByGameMode(gameMode: String): List<Score>


/* TODO: to be used in added features */

//    @Delete
//    suspend fun deleteScore(score: Score)
//
//    @Query("SELECT COUNT(*) FROM Score WHERE gameMode = :gameMode")
//    fun getNumberOfScoresByGameMode(gameMode: String): Flow<Int>
//
//    @Query("DELETE FROM Score WHERE gameMode = :gameMode")
//    fun deleteScoresByGameMode(gameMode: String)

}