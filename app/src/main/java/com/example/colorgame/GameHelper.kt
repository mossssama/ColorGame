package com.example.colorgame

class GameHelper {

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

}