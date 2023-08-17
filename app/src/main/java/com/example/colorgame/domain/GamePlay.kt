package com.example.colorgame.domain

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import com.example.colorgame.R
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.databinding.FragmentGamePlayBinding
import com.example.colorgame.databinding.FragmentMultiplayerGamePlayBinding
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.room.Score
import com.example.colorgame.room.ScoreDatabase
import com.example.colorgame.utils.DateUtils
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class GamePlay(private val lifecycleScope: CoroutineScope,context: Context) {

    companion object{
        const val BOX_ONE = "boxOne"
        const val BOX_TWO = "boxTwo"
        const val BOX_THREE = "boxThree"
        const val BOX_FOUR = "boxFour"
        const val BOX_FIVE = "boxFive"
        const val BOX_SIX = "boxSix"
        const val BOX_SEVEN = "boxSeven"
        const val BLUE = "BLUE"
        const val ORANGE = "ORANGE"
        const val RED = "RED"
        const val YELLOW = "YELLOW"
        const val GREEN = "GREEN"
        const val PURPLE = "PURPLE"
        const val BLACK = "BLACK"

        const val HUNDRED_SEC_MODE = "hundredSecondMode"
        const val CONTINUOUS_RIGHT_MODE = "continuousRightMode"
        const val THREE_WRONG_MODE = "threeWrongMode"
        lateinit var chosenBox: String

        private var countdownTimer: CountDownTimer? = null
    }

    var continuousRightAnswers: Int = 0
    var totalCorrectAnswers: Int =0
    var totalInCorrectAnswers: Int = 0


    private var blueColor: Int = context.getColor(R.color.blue)
    private var orangeColor: Int = context.getColor(R.color.orange)
    private var redColor: Int = context.getColor(R.color.red)
    private var yellowColor: Int = context.getColor(R.color.yellow)
    private var greenColor: Int = context.getColor(R.color.green)
    private var purpleColor: Int = context.getColor(R.color.purple)
    private var blackColor: Int = context.getColor(R.color.black)

    private val dataStoreManager = DataStoreManager.getInstance(context)
    private val fireStoreManager = FirestoreManager(Firebase.firestore)

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
        BLUE to false,
        ORANGE to false,
        RED to false,
        YELLOW to false,
        GREEN to false,
        PURPLE to false,
        BLACK to false
    )

    init { lifecycleScope.launch { dataStoreManager.saveGameOver(false) } }

    private fun chooseRandomColor(): Int = listOf(blueColor, orangeColor, redColor, yellowColor, greenColor, purpleColor, blackColor).random()
    private fun chooseRandomBox(): String = listOf("boxOne", "boxTwo", "boxThree", "boxFour", "boxFive", "boxSix", "boxSeven").random()

    private fun getColorForColorName(color: String): Int {
        return when (color) {
            BLUE -> blueColor
            ORANGE -> orangeColor
            RED -> redColor
            YELLOW -> yellowColor
            GREEN -> greenColor
            PURPLE -> purpleColor
            BLACK -> blackColor
            else -> blueColor
        }
    }

    private fun getColorNameForColorInt(color: Int): String {
        return when (color) {
            blueColor -> BLUE
            orangeColor -> ORANGE
            redColor -> RED
            yellowColor -> YELLOW
            greenColor -> GREEN
            purpleColor -> PURPLE
            blackColor -> BLACK
            else -> ""
        }
    }

    private fun areKeyPairsUnique(map1: HashMap<String, String>, map2: HashMap<String, String>): Boolean {
        for ((key1, value1) in map1) {
            for ((key2, value2) in map2) {
                if (key1 == key2 && value1 == value2) {
                    return false
                }
            }
        }
        return true
    }

    private fun getColorText(correctColor: Int): String {
        var correctText=""
        when (correctColor) {
            blueColor -> { correctText = BLUE; colors[BLUE] = true; }
            orangeColor -> { correctText = ORANGE; colors[ORANGE] = true; }
            redColor -> { correctText = RED; colors[RED] = true; }
            yellowColor -> { correctText = YELLOW; colors[YELLOW] = true; }
            greenColor -> { correctText = GREEN; colors[GREEN] = true; }
            purpleColor -> { correctText = PURPLE; colors[PURPLE] = true; }
            blackColor -> { correctText = BLACK; colors[BLACK] = true; }
        }
        return correctText
    }

    private fun getMapOfBoxesAndTexts(
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

    private fun getMapOfBoxesAndColors(
        boxes: HashMap<String, Boolean>,
        colors: HashMap<String, Boolean>,
        currentColor: Int
    ): HashMap<String, Int> {
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


    /* For debugging only */
    private fun convertColorsToNames(colorMap: HashMap<String, Int>): HashMap<String, String> {
        val colorNameMap: HashMap<String, String> = HashMap()

        for ((box, color) in colorMap) {
            val colorName = getColorNameForColorInt(color)
            colorNameMap[box] = colorName
        }

        return colorNameMap
    }

    private fun assignColorsToBoxes(linkedBoxesAndColors: HashMap<String, Int>, binding: FragmentGamePlayBinding) {
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

    private fun assignColorsToBoxes(linkedBoxesAndColors: HashMap<String, Int>, binding: FragmentMultiplayerGamePlayBinding) {
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

    private fun assignTextsToBoxes(linkedBoxesAndTexts:HashMap<String,String>, binding:FragmentGamePlayBinding){
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

    private fun assignTextsToBoxes(linkedBoxesAndTexts:HashMap<String,String>, binding:FragmentMultiplayerGamePlayBinding){
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

    private fun assignTextAndColorToRightBox(chosenBox: String, correctColor:Int, correctText:String, binding: FragmentGamePlayBinding) {
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

    private fun assignTextAndColorToRightBox(chosenBox: String, correctColor:Int, correctText:String, binding: FragmentMultiplayerGamePlayBinding) {
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

    private fun resetBoxesAndColors(){
        resetBoxesToFalse(boxes)
        resetColorsToFalse(colors)
    }

    private fun resetBoxesToFalse(boxes: HashMap<String, Boolean>) {
        for (key in boxes.keys) boxes[key] = false
    }

    private fun resetColorsToFalse(colors: HashMap<String, Boolean>) {
        for (key in colors.keys) colors[key] = false
    }

    private fun assureEveryBoxHasTextAndDifferentColor(chosenBox:String, correctColor:Int, correctText:String, boxesTextsMap:HashMap<String,String>, binding: FragmentGamePlayBinding): HashMap<String, Int> {
        var boxesColorsMap:HashMap<String,Int>
        do{
            resetBoxesAndColors()
            assignTextAndColorToRightBox(chosenBox,correctColor,correctText,binding)
            boxesColorsMap = getMapOfBoxesAndColors(boxes, colors, correctColor)
        }while(!areKeyPairsUnique(boxesTextsMap,convertColorsToNames(boxesColorsMap)))
        return boxesColorsMap
    }

    private fun assureEveryBoxHasTextAndDifferentColor(chosenBox:String, correctColor:Int, correctText:String, boxesTextsMap:HashMap<String,String>, binding: FragmentMultiplayerGamePlayBinding): HashMap<String, Int> {
        var boxesColorsMap:HashMap<String,Int>
        do{
            resetBoxesAndColors()
            assignTextAndColorToRightBox(chosenBox,correctColor,correctText,binding)
            boxesColorsMap = getMapOfBoxesAndColors(boxes, colors, correctColor)
        }while(!areKeyPairsUnique(boxesTextsMap,convertColorsToNames(boxesColorsMap)))
        return boxesColorsMap
    }

    private fun debuggingForCheck(chosenBox: String, boxesTextsMap: HashMap<String, String>, boxesColorsMap: HashMap<String, Int>) {
        Timber.i("$chosenBox|$boxesTextsMap|${convertColorsToNames(boxesColorsMap)}|${areKeyPairsUnique(boxesTextsMap,convertColorsToNames(boxesColorsMap))}")
    }

    fun getNewUI(binding: FragmentGamePlayBinding):String {
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

    fun getNewUI(binding: FragmentMultiplayerGamePlayBinding):String {
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

    fun setGamePlay(gameMode: String,binding: FragmentGamePlayBinding,context: Context,seconds: Long){
        if(gameMode == HUNDRED_SEC_MODE) startCountdown(binding,context,seconds)
        onBoxesListener(gameMode,binding,context)
    }

    fun setGamePlay(gameMode: String,playerName: String,binding: FragmentMultiplayerGamePlayBinding,context: Context,seconds: Long){
        if(gameMode == HUNDRED_SEC_MODE) startCountdown(binding,context,seconds,playerName)
        onBoxesListener(gameMode,playerName,binding,context)
    }

    private fun onBoxesListener(gameMode: String,binding: FragmentGamePlayBinding,context: Context) {
        boxOnClickListener(binding.boxOne,gameMode,binding,context)
        boxOnClickListener(binding.boxTwo,gameMode,binding,context)
        boxOnClickListener(binding.boxThree,gameMode,binding,context)
        boxOnClickListener(binding.boxFour,gameMode,binding,context)
        boxOnClickListener(binding.boxFive,gameMode,binding,context)
        boxOnClickListener(binding.boxSix,gameMode,binding,context)
        boxOnClickListener(binding.boxSeven,gameMode,binding,context)
    }

    private fun onBoxesListener(gameMode: String,playerName: String,binding: FragmentMultiplayerGamePlayBinding,context: Context) {
        boxOnClickListener(binding.boxOne,gameMode,playerName,binding,context)
        boxOnClickListener(binding.boxTwo,gameMode,playerName,binding,context)
        boxOnClickListener(binding.boxThree,gameMode,playerName,binding,context)
        boxOnClickListener(binding.boxFour,gameMode,playerName,binding,context)
        boxOnClickListener(binding.boxFive,gameMode,playerName,binding,context)
        boxOnClickListener(binding.boxSix,gameMode,playerName,binding,context)
        boxOnClickListener(binding.boxSeven,gameMode,playerName,binding,context)
    }

    private fun boxOnClickListener(boxView: View, gameMode: String,binding: FragmentGamePlayBinding, context: Context) {
        boxView.setOnClickListener {
            val boxId = when (boxView.id) {
                R.id.boxOne -> BOX_ONE
                R.id.boxTwo -> BOX_TWO
                R.id.boxThree -> BOX_THREE
                R.id.boxFour -> BOX_FOUR
                R.id.boxFive -> BOX_FIVE
                R.id.boxSix -> BOX_SIX
                R.id.boxSeven -> BOX_SEVEN
                else -> return@setOnClickListener
            }

            when (gameMode){
                CONTINUOUS_RIGHT_MODE -> continuousRightModeGamePlay(boxId,binding,context)
                HUNDRED_SEC_MODE -> hundredSecondGamePlay(boxId,binding)
                THREE_WRONG_MODE -> threeWrongGamePlay(boxId,binding,context)
            }

        }
    }

    private fun boxOnClickListener(boxView: View, gameMode: String,playerName: String,binding: FragmentMultiplayerGamePlayBinding, context: Context) {
        boxView.setOnClickListener {
            val boxId = when (boxView.id) {
                R.id.boxOne -> BOX_ONE
                R.id.boxTwo -> BOX_TWO
                R.id.boxThree -> BOX_THREE
                R.id.boxFour -> BOX_FOUR
                R.id.boxFive -> BOX_FIVE
                R.id.boxSix -> BOX_SIX
                R.id.boxSeven -> BOX_SEVEN
                else -> return@setOnClickListener
            }

            when (gameMode){
                CONTINUOUS_RIGHT_MODE -> continuousRightModeGamePlay(boxId,binding,context)
                HUNDRED_SEC_MODE -> hundredSecondGamePlay(playerName,boxId,binding)
                THREE_WRONG_MODE -> threeWrongGamePlay(boxId,binding,context)
            }

        }
    }

    private fun threeWrongGamePlay(boxId: String, binding: FragmentGamePlayBinding, context: Context) {
        if(chosenBox !=boxId) { totalInCorrectAnswers++}

        if(totalInCorrectAnswers>2) {
            lifecycleScope.launch { insertScoreToDatabase(context, Score(THREE_WRONG_MODE, totalCorrectAnswers, DateUtils.getCurrentDate())) }
            lifecycleScope.launch { dataStoreManager.saveGameOver(true) }
        }
        else{
            if(chosenBox ==boxId) totalCorrectAnswers++
            chosenBox =getNewUI(binding)
        }
    }

    private fun threeWrongGamePlay(boxId: String, binding: FragmentMultiplayerGamePlayBinding, context: Context) {
        if(chosenBox !=boxId) { totalInCorrectAnswers++}

        if(totalInCorrectAnswers>2) {
            lifecycleScope.launch { insertScoreToDatabase(context, Score(THREE_WRONG_MODE, totalCorrectAnswers, DateUtils.getCurrentDate())) }
            lifecycleScope.launch { dataStoreManager.saveGameOver(true) }
        }
        else{
            if(chosenBox ==boxId) totalCorrectAnswers++
            chosenBox =getNewUI(binding)
        }
    }

    private fun continuousRightModeGamePlay(boxId: String,binding: FragmentGamePlayBinding,context: Context){
        if(chosenBox ==boxId) { continuousRightAnswers++; chosenBox = getNewUI(binding) }
        else {
            lifecycleScope.launch { insertScoreToDatabase(context, Score(CONTINUOUS_RIGHT_MODE, continuousRightAnswers, DateUtils.getCurrentDate())) }
            lifecycleScope.launch { dataStoreManager.saveGameOver(true) }
        }
    }

    private fun continuousRightModeGamePlay(boxId: String,binding: FragmentMultiplayerGamePlayBinding,context: Context){
        if(chosenBox ==boxId) { continuousRightAnswers++; chosenBox = getNewUI(binding) }
        else {
            lifecycleScope.launch { insertScoreToDatabase(context, Score(CONTINUOUS_RIGHT_MODE, continuousRightAnswers, DateUtils.getCurrentDate())) }
            lifecycleScope.launch { dataStoreManager.saveGameOver(true) }
        }
    }

    private fun hundredSecondGamePlay(boxId: String,binding: FragmentGamePlayBinding){
        if(chosenBox ==boxId) { continuousRightAnswers++; }
        else { continuousRightAnswers--; }
        chosenBox = getNewUI(binding)
    }

    private fun hundredSecondGamePlay(playerName: String, boxId: String,binding: FragmentMultiplayerGamePlayBinding){
        if(chosenBox ==boxId) { fireStoreManager.incrementScore(playerName, onSuccess = {}, onFailure = {});  continuousRightAnswers++; }
        else { fireStoreManager.decrementScore(playerName, onSuccess = {}, onFailure = {}); continuousRightAnswers--; }
        chosenBox = getNewUI(binding)
    }

    private fun startCountdown(binding:FragmentGamePlayBinding,context: Context,seconds: Long) {
        countdownTimer?.cancel() // Cancel any existing timers
        countdownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = millisUntilFinished / 1000
                binding.countdownTextView.text = remainingSeconds.toString()        // Update the TextView with the remaining seconds
            }
            override fun onFinish() {
                binding.countdownTextView.text = "0"
                lifecycleScope.launch { insertScoreToDatabase(context, Score(HUNDRED_SEC_MODE, continuousRightAnswers, DateUtils.getCurrentDate())) }
                lifecycleScope.launch { dataStoreManager.saveGameOver(true) }
            }    // Countdown has finished, you can perform any action here
        }.start()
    }

    private fun startCountdown(binding:FragmentMultiplayerGamePlayBinding,context: Context,seconds: Long,playerName: String) {
        countdownTimer?.cancel() // Cancel any existing timers
        fireStoreManager.setCountDownToHundred(playerName, onSuccess = {}, onFailure = {})
        countdownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = millisUntilFinished / 1000
                if(remainingSeconds.toInt()==0) {
                    fireStoreManager.updateCountDown(playerName,0, onSuccess = {}, onFailure = {})
                    fireStoreManager.setStartPlaying(playerName,false, onSuccess = {}, onFailure = {})
                }
                binding.countdownTextView.text = remainingSeconds.toString()        // Update the TextView with the remaining seconds
            }
            override fun onFinish() {
                binding.countdownTextView.text = "0"
                lifecycleScope.launch { insertScoreToDatabase(context, Score(HUNDRED_SEC_MODE, continuousRightAnswers, DateUtils.getCurrentDate())) }
                lifecycleScope.launch { dataStoreManager.saveGameOver(true) }
            }    // Countdown has finished, you can perform any action here
        }.start()
    }

    private suspend fun insertScoreToDatabase(context: Context, score: Score) {
        val database = ScoreDatabase.getInstance(context = context)
        withContext(Dispatchers.IO) { database.scoreDao.insertScore(score) }
    }

}