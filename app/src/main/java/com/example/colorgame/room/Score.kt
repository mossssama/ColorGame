package com.example.colorgame.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Score(val gameMode: String, val score: Int, val date: String, @PrimaryKey(autoGenerate = true) val id : Int=0)
