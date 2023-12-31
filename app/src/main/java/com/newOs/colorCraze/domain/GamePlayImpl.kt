package com.newOs.colorCraze.domain

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import com.newOs.colorCraze.R
import com.newOs.colorCraze.datastore.DataStoreManager
import com.newOs.colorCraze.helpers.Colors.blackColor
import com.newOs.colorCraze.helpers.Colors.blueColor
import com.newOs.colorCraze.helpers.Colors.greenColor
import com.newOs.colorCraze.helpers.Colors.orangeColor
import com.newOs.colorCraze.helpers.Colors.purpleColor
import com.newOs.colorCraze.helpers.Colors.redColor
import com.newOs.colorCraze.helpers.Colors.yellowColor
import com.newOs.colorCraze.helpers.Constants.BOX_FIVE
import com.newOs.colorCraze.helpers.Constants.BOX_FOUR
import com.newOs.colorCraze.helpers.Constants.BOX_ONE
import com.newOs.colorCraze.helpers.Constants.BOX_SEVEN
import com.newOs.colorCraze.helpers.Constants.BOX_SIX
import com.newOs.colorCraze.helpers.Constants.BOX_THREE
import com.newOs.colorCraze.helpers.Constants.BOX_TWO
import com.newOs.colorCraze.helpers.Constants.CONTINUOUS_RIGHT_MODE
import com.newOs.colorCraze.helpers.Constants.THREE_WRONG_MODE
import com.newOs.colorCraze.helpers.Functions.areKeyPairsUnique
import com.newOs.colorCraze.helpers.Functions.chooseRandomBox
import com.newOs.colorCraze.helpers.Functions.chooseRandomColor
import com.newOs.colorCraze.helpers.Functions.getCurrentDate
import com.newOs.colorCraze.helpers.Functions.getMapOfBoxesAndTexts
import com.newOs.colorCraze.helpers.Functions.insertScoreToDatabase
import com.newOs.colorCraze.helpers.Functions.resetBoxesToFalse
import com.newOs.colorCraze.helpers.Functions.resetColorsToFalse
import com.newOs.colorCraze.room.Score
import kotlinx.android.synthetic.main.fragment_game_play.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class GamePlayImpl @Inject constructor(var context: Context)  :GamePlay{

    companion object{
        lateinit var chosenBox: String
        var countdownTimer: CountDownTimer? = null
    }

    var continuousRightAnswers: Int = 0
    var totalCorrectAnswers: Int =0
    var totalInCorrectAnswers: Int = 0

    private val blue = context.getString(R.string.blue)
    private val orange = context.getString(R.string.orange)
    private val red = context.getString(R.string.red)
    private val yellow = context.getString(R.string.yellow)
    private val green = context.getString(R.string.green)
    private val purple = context.getString(R.string.purple)
    private val black = context.getString(R.string.black)

    private fun getColorForColorName(color: String): Int =
        when (color) {
            blue -> blueColor
            orange -> orangeColor
            red -> redColor
            yellow -> yellowColor
            green -> greenColor
            purple -> purpleColor
            black -> blackColor
            else -> blueColor
        }
    private fun getColorNameForColorInt(color: Int): String =
        when (color) {
            blueColor -> blue
            orangeColor -> orange
            redColor -> red
            yellowColor -> yellow
            greenColor -> green
            purpleColor -> purple
            blackColor -> black
            else -> ""
        }

    private var boxes: HashMap<String, Boolean> = hashMapOf(
        BOX_ONE to false,
        BOX_TWO to false,
        BOX_THREE to false,
        BOX_FOUR to false,
        BOX_FIVE to false,
        BOX_SIX to false,
        BOX_SEVEN to false
    )

    private var colors: HashMap<String, Boolean> = hashMapOf(
        blue to false,
        orange to false,
        red to false,
        yellow to false,
        green to false,
        purple to false,
        black to false
    )

    private fun getColorText(correctColor: Int): String {
        var correctText=""
        when (correctColor) {
            blueColor -> { correctText = blue; colors[blue] = true; }
            orangeColor -> { correctText = orange; colors[orange] = true; }
            redColor -> { correctText = red; colors[red] = true; }
            yellowColor -> { correctText = yellow; colors[yellow] = true; }
            greenColor -> { correctText = green; colors[green] = true; }
            purpleColor -> { correctText = purple; colors[purple] = true; }
            blackColor -> { correctText = black; colors[black] = true; }
        }
        return correctText
    }

    private fun convertColorsToNames(colorMap: HashMap<String, Int>): HashMap<String, String> {
        val colorNameMap: HashMap<String, String> = HashMap()

        for ((box, color) in colorMap) {
            val colorName = getColorNameForColorInt(color)
            colorNameMap[box] = colorName
        }

        return colorNameMap
    }

    private fun resetBoxesAndColors(){
        resetBoxesToFalse(boxes)
        resetColorsToFalse(colors)
    }


    /* For debugging only */
    private fun debuggingForCheck(chosenBox: String, boxesTextsMap: HashMap<String, String>, boxesColorsMap: HashMap<String, Int>) {
        Timber.i("$chosenBox|$boxesTextsMap|${convertColorsToNames(boxesColorsMap)}|${areKeyPairsUnique(boxesTextsMap,convertColorsToNames(boxesColorsMap))}")
    }

    /** Shared Logic */
    override fun getNewUI(binding: View):String {
        val correctColor = chooseRandomColor()                                     /* Choose Random Color to be the Right Color */
        val correctText = getColorText(correctColor)                               /* Return the Color as Text */
        val chosenBox = chooseRandomBox()                                          /* Choose Random box to put in it the text Colored with the same color of the written word */
        assignTextAndColorToRightBox(chosenBox,correctColor,correctText,binding)   /* Assign text Colored to the chosenBox */

        /* Write a text to each box */
        val boxesTextsMap = getMapOfBoxesAndTexts(boxes,colors)
        assignTextsToBoxes(boxesTextsMap,binding)

        /* Choose a color to text that is different from the text word */
        val boxesColorsMap = assureEveryBoxHasTextAndDifferentColor(chosenBox,correctColor, correctText, boxesTextsMap,binding)

        /* Apply a color to each box */
        assignColorsToBoxes(boxesColorsMap,binding)

        resetBoxesAndColors()

        /* For debugging only */
        debuggingForCheck(chosenBox,boxesTextsMap,boxesColorsMap)

        return chosenBox
    }
    private fun assignTextsToBoxes(linkedBoxesAndTexts:HashMap<String,String>, binding:View){
        for ((box, color) in linkedBoxesAndTexts) {
            when (box) {
                BOX_ONE ->  binding.boxOne.text = color
                BOX_TWO ->  binding.boxTwo.text = color
                BOX_THREE -> binding.boxThree.text = color
                BOX_FOUR -> binding.boxFour.text = color
                BOX_FIVE -> binding.boxFive.text = color
                BOX_SIX -> binding.boxSix.text = color
                BOX_SEVEN -> binding.boxSeven.text = color
            }
        }
    }
    private fun assignColorsToBoxes(linkedBoxesAndColors: HashMap<String, Int>, binding: View) {
        for ((box, color) in linkedBoxesAndColors) {
            when (box) {
                BOX_ONE ->  binding.boxOne.setTextColor(color)
                BOX_TWO ->  binding.boxTwo.setTextColor(color)
                BOX_THREE -> binding.boxThree.setTextColor(color)
                BOX_FOUR -> binding.boxFour.setTextColor(color)
                BOX_FIVE -> binding.boxFive.setTextColor(color)
                BOX_SIX -> binding.boxSix.setTextColor(color)
                BOX_SEVEN -> binding.boxSeven.setTextColor(color)
            }
        }
    }
    private fun assignTextAndColorToRightBox(chosenBox: String, correctColor:Int, correctText:String, binding: View) {
        when (chosenBox) {
            BOX_ONE -> { binding.boxOne.setTextColor(correctColor);     binding.boxOne.text = correctText;      boxes[BOX_ONE] = true }
            BOX_TWO -> { binding.boxTwo.setTextColor(correctColor);     binding.boxTwo.text = correctText;      boxes[BOX_TWO] = true }
            BOX_THREE -> { binding.boxThree.setTextColor(correctColor); binding.boxThree.text = correctText;    boxes[BOX_THREE] = true }
            BOX_FOUR -> { binding.boxFour.setTextColor(correctColor);   binding.boxFour.text = correctText;     boxes[BOX_FOUR] = true }
            BOX_FIVE -> { binding.boxFive.setTextColor(correctColor);   binding.boxFive.text = correctText;     boxes[BOX_FIVE] = true }
            BOX_SIX -> { binding.boxSix.setTextColor(correctColor);     binding.boxSix.text = correctText;      boxes[BOX_SIX] = true }
            BOX_SEVEN -> { binding.boxSeven.setTextColor(correctColor); binding.boxSeven.text = correctText;    boxes[BOX_SEVEN] = true }
        }
    }
    private fun assureEveryBoxHasTextAndDifferentColor(chosenBox:String, correctColor:Int, correctText:String, boxesTextsMap:HashMap<String,String>, binding: View): HashMap<String, Int> {
        var boxesColorsMap:HashMap<String,Int>
        do{
            resetBoxesAndColors()
            assignTextAndColorToRightBox(chosenBox,correctColor,correctText,binding)
            boxesColorsMap = getMapOfBoxesAndColors(boxes, colors, correctColor)
        }while(!areKeyPairsUnique(boxesTextsMap,convertColorsToNames(boxesColorsMap)))
        return boxesColorsMap
    }
    private fun getMapOfBoxesAndColors(boxes: HashMap<String, Boolean>, colors: HashMap<String, Boolean>, currentColor: Int): HashMap<String, Int> {
        val boxesAndColorsMap: HashMap<String, Int> = HashMap()
        val shuffledBoxes = boxes.filterValues { !it }.keys.toList().shuffled()

        // Filter out the color assigned to the true box and currentColor from available colors
        val shuffledAvailableColors = colors.filterValues { !it }
            .filterKeys { getColorForColorName(it) != currentColor }
            .keys.toList().shuffled()

        val pairsCount = minOf(shuffledBoxes.size, shuffledAvailableColors.size)

        for (i in 0 until pairsCount) {
            val box = shuffledBoxes[i]
            val color = getColorForColorName(shuffledAvailableColors[i])

            boxesAndColorsMap[box] = color
            boxes[box] = true
        }

        return boxesAndColorsMap
    }
    override fun threeWrongGamePlay(boxId: String, binding: View, context: Context,dataStoreManager: DataStoreManager,lifecycleScope: CoroutineScope) {
        if(chosenBox !=boxId) { totalInCorrectAnswers++}

        if(totalInCorrectAnswers>2) {
            lifecycleScope.launch { insertScoreToDatabase(context, Score(THREE_WRONG_MODE, totalCorrectAnswers, getCurrentDate())) }
            lifecycleScope.launch { dataStoreManager.saveGameOver(true) }
        }
        else{
            if(chosenBox ==boxId) totalCorrectAnswers++
            chosenBox =getNewUI(binding)
        }
    }        /* write in DataStore & Room */
    override fun continuousRightModeGamePlay(boxId: String,binding: View,context: Context,dataStoreManager: DataStoreManager,lifecycleScope: CoroutineScope){
        if(chosenBox ==boxId) { continuousRightAnswers++; chosenBox = getNewUI(binding) }
        else {
            lifecycleScope.launch { insertScoreToDatabase(context, Score(CONTINUOUS_RIGHT_MODE, continuousRightAnswers, getCurrentDate())) }
            lifecycleScope.launch { dataStoreManager.saveGameOver(true) }
        }
    }


}
