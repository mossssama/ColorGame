package com.example.colorgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.colorgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var gamePlay: GamePlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        gamePlay=GamePlay()

        gamePlay.initColors(this)

        binding.floatingActionButton.setOnClickListener { updateGamePlayUI() }

    }

    private fun updateGamePlayUI() {
        val correctColor = gamePlay.chooseRandomColor()                                     /* Choose Random Color to be the Right Color */
        val correctText = gamePlay.getColorText(correctColor)                               /* Return the Color as Text */
        val chosenBox = gamePlay.chooseRandomBox()                                          /* Choose Random box to put in it the text Colored with the same color of the written word */
        gamePlay.assignTextAndColorToRightBox(chosenBox,correctColor,correctText,binding)   /* Assign text Colored to the chosenBox */

        /* Write a text to each box */
        val boxesTextsMap = gamePlay.getMapOfBoxesAndTexts(gamePlay.boxes,gamePlay.colors)
        gamePlay.assignTextsToBoxes(boxesTextsMap,binding)

        /* Choose a color to text that is different from the text word */
        val boxesColorsMap = assureEveryBoxHasTextAndDifferentColor(chosenBox,correctColor, correctText, boxesTextsMap)

        /* Apply a color to each box */
        gamePlay.assignColorsToBoxes(boxesColorsMap,binding)

        gamePlay.resetBoxesAndColors()

        /* For debugging only */
        Log.i("TAG",chosenBox)
        Log.i("TAG",boxesTextsMap.toString())
        Log.i("TAG",gamePlay.convertColorsToNames(boxesColorsMap).toString())
        Log.i("TAG",gamePlay.areKeyPairsUnique(boxesTextsMap,gamePlay.convertColorsToNames(boxesColorsMap)).toString())
    }

    private fun assureEveryBoxHasTextAndDifferentColor(chosenBox:String,correctColor:Int,correctText:String,boxesTextsMap:HashMap<String,String>): HashMap<String, Int> {
        var boxesColorsMap:HashMap<String,Int>
        do{
            gamePlay.resetBoxesAndColors()
            gamePlay.assignTextAndColorToRightBox(chosenBox,correctColor,correctText,binding)
            boxesColorsMap = gamePlay.getMapOfBoxesAndColors(gamePlay.boxes, gamePlay.colors, correctColor)
        }while(!gamePlay.areKeyPairsUnique(boxesTextsMap,gamePlay.convertColorsToNames(boxesColorsMap)))
        return boxesColorsMap
    }


}

