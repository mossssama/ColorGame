package com.example.colorgame.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Score::class],
    version = 1
)
abstract class ScoreDatabase: RoomDatabase() {
    abstract val scoreDao: ScoreDao
}