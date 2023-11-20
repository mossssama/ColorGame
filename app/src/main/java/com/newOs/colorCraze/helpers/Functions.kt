package com.newOs.colorCraze.helpers

import android.content.Context
import com.newOs.colorCraze.room.Score
import com.newOs.colorCraze.room.ScoreDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object Functions {
    fun chooseRandomBox(): String = listOf("boxOne", "boxTwo", "boxThree", "boxFour", "boxFive", "boxSix", "boxSeven").random()

    fun areKeyPairsUnique(map1: HashMap<String, String>, map2: HashMap<String, String>): Boolean {
        for ((key1, value1) in map1) {
            for ((key2, value2) in map2) {
                if (key1 == key2 && value1 == value2) {
                    return false
                }
            }
        }
        return true
    }

    fun chooseRandomColor(): Int = listOf(
        Colors.blueColor,
        Colors.orangeColor,
        Colors.redColor,
        Colors.yellowColor,
        Colors.greenColor,
        Colors.purpleColor,
        Colors.blackColor
    ).random()

    fun getMapOfBoxesAndTexts(
        boxes: HashMap<String, Boolean>,
        colors: HashMap<String, Boolean>
    ): HashMap<String, String> {
        val boxesAndTextsMap: HashMap<String, String> = HashMap()
        val shuffledBoxes = boxes.filterValues { !it }.keys.toList().shuffled()
        val shuffledColors = colors.filterValues { !it }.keys.toList().shuffled()

        val pairsCount = minOf(shuffledBoxes.size, shuffledColors.size)

        for (i in 0 until pairsCount) {
            val box = shuffledBoxes[i]
            val color = shuffledColors[i]

            boxesAndTextsMap[box] = color
            boxes[box] = true
            colors[color] = true
        }

        return boxesAndTextsMap
    }

    suspend fun insertScoreToDatabase(context: Context, score: Score) {
        val database = ScoreDatabase.getInstance(context = context)
        withContext(Dispatchers.IO) { database.scoreDao.insertScore(score) }
    }

    fun resetBoxesToFalse(boxes: HashMap<String, Boolean>) {
        for (key in boxes.keys) boxes[key] = false
    }

    fun resetColorsToFalse(colors: HashMap<String, Boolean>) {
        for (key in colors.keys) colors[key] = false
    }

    fun getCurrentDate(): String = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault()).format(Date())
}