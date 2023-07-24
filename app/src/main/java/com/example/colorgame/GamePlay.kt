package com.example.colorgame

import android.content.Context
import com.example.colorgame.databinding.ActivityMainBinding

class GamePlay {

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
    }

    private var blueColor: Int = 0
    private var orangeColor: Int = 0
    private var redColor: Int = 0
    private var yellowColor: Int = 0
    private var greenColor: Int = 0
    private var purpleColor: Int = 0
    private var blackColor: Int = 0

    var boxes: HashMap<String, Boolean> = hashMapOf(
        BOX_ONE to false,
        BOX_TWO to false,
        BOX_THREE to false,
        BOX_FOUR to false,
        BOX_FIVE to false,
        BOX_SIX to false,
        BOX_SEVEN to false
    )

    var colors: HashMap<String, Boolean> = hashMapOf(
        BLUE to false,
        ORANGE to false,
        RED to false,
        YELLOW to false,
        GREEN to false,
        PURPLE to false,
        BLACK to false
    )

    fun initColors(context: Context) {
        blueColor = context.getColor(R.color.blue)
        orangeColor = context.getColor(R.color.orange)
        redColor = context.getColor(R.color.red)
        yellowColor = context.getColor(R.color.yellow)
        greenColor = context.getColor(R.color.green)
        purpleColor = context.getColor(R.color.purple)
        blackColor = context.getColor(R.color.black)
    }

    fun chooseRandomColor(): Int {
        val numbers = listOf(
            blueColor,
            orangeColor,
            redColor,
            yellowColor,
            greenColor,
            purpleColor,
            blackColor
        )
        return numbers.random()
    }

    private fun getColorForStringColor(color: String): Int {
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

    private fun getColorNameForColor(color: Int): String {
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

    fun chooseRandomBox(): String {
        val numbers = listOf("boxOne", "boxTwo", "boxThree", "boxFour", "boxFive", "boxSix", "boxSeven")
        return numbers.random()
    }

    fun resetBoxesToFalse(boxes: HashMap<String, Boolean>) {
        for (key in boxes.keys) boxes[key] = false
    }

    fun resetColorsToFalse(colors: HashMap<String, Boolean>) {
        for (key in colors.keys) colors[key] = false
    }

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

    fun getColorText(correctColor: Int): String {
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

    fun getMapOfBoxesAndTexts(
        boxes: HashMap<String, Boolean>,
        colors: HashMap<String, Boolean>
    ): HashMap<String, String> {
        val linkedHashMap: HashMap<String, String> = HashMap()
        val shuffledBoxes = boxes.filterValues { !it }.keys.toList().shuffled()
        val shuffledColors = colors.filterValues { !it }.keys.toList().shuffled()

        val pairsCount = minOf(shuffledBoxes.size, shuffledColors.size)

        for (i in 0 until pairsCount) {
            val box = shuffledBoxes[i]
            val color = shuffledColors[i]

            linkedHashMap[box] = color
            boxes[box] = true
            colors[color] = true
        }

        return linkedHashMap
    }

    fun getMapOfBoxesAndColors(
        boxes: HashMap<String, Boolean>,
        colors: HashMap<String, Boolean>,
        currentColor: Int
    ): HashMap<String, Int> {
        val linkedHashMap: HashMap<String, Int> = HashMap()
        val shuffledBoxes = boxes.filterValues { !it }.keys.toList().shuffled()

        // Filter out the color assigned to the true box and currentColor from available colors
        val shuffledAvailableColors = colors.filterValues { !it }
            .filterKeys { getColorForStringColor(it) != currentColor }
            .keys.toList().shuffled()

        val pairsCount = minOf(shuffledBoxes.size, shuffledAvailableColors.size)

        for (i in 0 until pairsCount) {
            val box = shuffledBoxes[i]
            val color = getColorForStringColor(shuffledAvailableColors[i])

            linkedHashMap[box] = color
            boxes[box] = true
        }

        return linkedHashMap
    }


    /* For debugging only */
    fun convertColorsToNames(colorMap: HashMap<String, Int>): HashMap<String, String> {
        val colorNameMap: HashMap<String, String> = HashMap()

        for ((box, color) in colorMap) {
            val colorName = getColorNameForColor(color)
            colorNameMap[box] = colorName
        }

        return colorNameMap
    }

    fun assignColorsToBoxes(linkedBoxesAndColors: HashMap<String, Int>,binding: ActivityMainBinding) {
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

    fun assignTextsToBoxes(linkedBoxesAndTexts:HashMap<String,String>,binding: ActivityMainBinding){
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

    fun assignTextAndColorToRightBox(chosenBox: String,correctColor:Int,correctText:String,binding: ActivityMainBinding) {
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

    fun resetBoxesAndColors(){
        resetBoxesToFalse(boxes)
        resetColorsToFalse(colors)
    }

}