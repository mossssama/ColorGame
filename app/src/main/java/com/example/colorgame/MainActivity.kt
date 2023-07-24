package com.example.colorgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.colorgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val BOX_ONE = "boxOne"
    private val BOX_TWO = "boxTwo"
    private val BOX_THREE = "boxThree"
    private val BOX_FOUR = "boxFour"
    private val BOX_FIVE = "boxFive"
    private val BOX_SIX = "boxSix"
    private val BOX_SEVEN = "boxSeven"
    private val BLUE = "BLUE"
    private val ORANGE = "ORANGE"
    private val RED = "RED"
    private val YELLOW = "YELLOW"
    private val GREEN = "GREEN"
    private val PURPLE = "PURPLE"
    private val BLACK = "BLACK"
    private val BROWN = "BROWN"
    private val GREY = "GREY"
    private val PINK = "PINK"

    private var BLUE_COLOR: Int = 0
    private var ORANGE_COLOR: Int = 0
    private var RED_COLOR: Int = 0
    private var YELLOW_COLOR: Int = 0
    private var GREEN_COLOR: Int = 0
    private var PURPLE_COLOR: Int = 0
    private var BLACK_COLOR: Int = 0
    private var GREY_COLOR: Int = 0
    private var BROWN_COLOR: Int = 0
    private var PINK_COLOR: Int = 0

    lateinit var correctText:String

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

    private var moreColors: HashMap<String, Boolean> = hashMapOf(
        BLUE to false,
        ORANGE to false,
        RED to false,
        YELLOW to false,
        GREEN to false,
        PURPLE to false,
        BLACK to false,
        PINK to false,
        GREY to false,
        BROWN to false
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initTextsColors()

        binding.floatingActionButton.setOnClickListener {

            val correctColor = chooseCorrectColor()

            /* Choosing a color that will have the text with the same color */
            chooseTheRightColor(correctColor)

            /* Assign text & color to right Box */
            var chosenBox = chooseCorrectBox()

            assignTextAndColorToRightBox(chosenBox,correctColor,correctText)

            val boxesWithTexts = getHashMapOfBoxesAndTexts(boxes,colors)
            assignTextsToBoxes(boxesWithTexts)

            resetBoxesToFalse(boxes)
            resetBColorsToFalse(colors,moreColors)

            assignTextAndColorToRightBox(chosenBox,correctColor,correctText)

            var boxesWithColors = getHashMapOfBoxesAndColors(boxes, colors, correctColor)
            while(!areKeyPairsUnique(boxesWithTexts,convertColorsToNames(boxesWithColors))){
                resetBoxesToFalse(boxes)
                resetBColorsToFalse(colors,moreColors)
                assignTextAndColorToRightBox(chosenBox,correctColor,correctText)
               boxesWithColors = getHashMapOfBoxesAndColors(boxes, colors, correctColor)
            }


            assignColorsToBoxes(boxesWithColors)

            resetBoxesToFalse(boxes)
            resetBColorsToFalse(colors,moreColors)

            Log.i("TAG",chosenBox)
            Log.i("TAG",boxesWithTexts.toString())
            Log.i("TAG",convertColorsToNames(boxesWithColors).toString())
            Log.i("TAG",areKeyPairsUnique(boxesWithTexts,convertColorsToNames(boxesWithColors)).toString())
        }

    }

    private fun chooseTheRightColor(correctColor: Int) {
        when (correctColor) {
            BLUE_COLOR -> {
                correctText = BLUE; colors[BLUE] = true; }
            ORANGE_COLOR -> {
                correctText = ORANGE; colors[ORANGE] = true; }
            RED_COLOR -> {
                correctText = RED; colors[RED] = true; }
            YELLOW_COLOR -> {
                correctText = YELLOW; colors[YELLOW] = true; }
            GREEN_COLOR -> {
                correctText = GREEN; colors[GREEN] = true; }
            PURPLE_COLOR -> {
                correctText = PURPLE; colors[PURPLE] = true; }
            BLACK_COLOR -> {
                correctText = BLACK; colors[BLACK] = true; }
        }

    }

    private fun assignTextAndColorToRightBox(chosenBox: String,correctColor:Int,correctText:String) {
        when (chosenBox) {
            BOX_ONE -> {
                binding.boxOne.setTextColor(correctColor); binding.boxOne.text = correctText; boxes[BOX_ONE] = true
            }
            BOX_TWO -> {
                binding.boxTwo.setTextColor(correctColor); binding.boxTwo.text =
                    correctText; boxes[BOX_TWO] = true
            }
            BOX_THREE -> {
                binding.boxThree.setTextColor(correctColor); binding.boxThree.text =
                    correctText; boxes[BOX_THREE] = true
            }
            BOX_FOUR -> {
                binding.boxFour.setTextColor(correctColor); binding.boxFour.text =
                    correctText; boxes[BOX_FOUR] = true
            }
            BOX_FIVE -> {
                binding.boxFive.setTextColor(correctColor); binding.boxFive.text =
                    correctText; boxes[BOX_FIVE] = true
            }
            BOX_SIX -> {
                binding.boxSix.setTextColor(correctColor); binding.boxSix.text =
                    correctText; boxes[BOX_SIX] = true
            }
            BOX_SEVEN -> {
                binding.boxSeven.setTextColor(correctColor); binding.boxSeven.text =
                    correctText; boxes[BOX_SEVEN] = true
            }
        }
    }


    private fun initTextsColors() {
        BLUE_COLOR = getColor(R.color.blue)
        ORANGE_COLOR = getColor(R.color.orange)
        RED_COLOR = getColor(R.color.red)
        YELLOW_COLOR = getColor(R.color.yellow)
        GREEN_COLOR = getColor(R.color.green)
        PURPLE_COLOR = getColor(R.color.purple)
        BLACK_COLOR = getColor(R.color.black)
        GREY_COLOR = getColor(R.color.grey)
        PINK_COLOR = getColor(R.color.pink)
        BROWN_COLOR = getColor(R.color.brown)
    }


    private fun chooseCorrectBox(): String {
        val numbers = listOf("boxOne", "boxTwo", "boxThree", "boxFour", "boxFive", "boxSix", "boxSeven")
        return numbers.random()
    }

    private fun chooseCorrectColor(): Int {
        val numbers = listOf(
            BLUE_COLOR,
            ORANGE_COLOR,
            RED_COLOR,
            YELLOW_COLOR,
            GREEN_COLOR,
            PURPLE_COLOR,
            BLACK_COLOR
        )
        return numbers.random()
    }

    private fun getHashMapOfBoxesAndTexts(
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

    private fun getHashMapOfBoxesAndColors(
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



    private fun getColorForStringColor(color: String): Int {
        return when (color) {
            BLUE -> BLUE_COLOR
            ORANGE -> ORANGE_COLOR
            RED -> RED_COLOR
            YELLOW -> YELLOW_COLOR
            GREEN -> GREEN_COLOR
            PURPLE -> PURPLE_COLOR
            BLACK -> BLACK_COLOR
            else -> BLUE_COLOR
        }
    }

    private fun resetBoxesToFalse(boxes: HashMap<String, Boolean>) {
        for (key in boxes.keys) boxes[key] = false
    }

    private fun resetBColorsToFalse(colors: HashMap<String, Boolean>,colorsTwo: HashMap<String, Boolean>) {
        for (key in colors.keys) colors[key] = false
        for (key in colorsTwo.keys) colorsTwo[key] = false
    }

    private fun assignTextsToBoxes(linkedBoxesAndTexts:HashMap<String,String>){
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

    private fun assignColorsToBoxes(linkedBoxesAndColors: HashMap<String, Int>) {
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

    /* For debugging only */
    private fun convertColorsToNames(colorMap: HashMap<String, Int>): HashMap<String, String> {
        val colorNameMap: HashMap<String, String> = HashMap()

        for ((box, color) in colorMap) {
            val colorName = getColorNameForColor(color)
            colorNameMap[box] = colorName
        }

        return colorNameMap
    }

    private fun getColorNameForColor(color: Int): String {
        when (color) {
            BLUE_COLOR -> return BLUE
            ORANGE_COLOR -> return ORANGE
            RED_COLOR -> return RED
            YELLOW_COLOR -> return YELLOW
            GREEN_COLOR -> return GREEN
            PURPLE_COLOR -> return PURPLE
            BLACK_COLOR -> return BLACK
            else -> return ""
        }
    }

}

