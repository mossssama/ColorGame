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

        gamePlay.initTextsColors(this)

        binding.floatingActionButton.setOnClickListener {

            val correctColor = gamePlay.chooseCorrectColor()
            val correctText = gamePlay.chooseTheRightColor(correctColor)                        /* Choosing a color that will have the text with the same color */
            val chosenBox = gamePlay.chooseCorrectBox()                                         /* Assign text & color to right Box */

            gamePlay.assignTextAndColorToRightBox(chosenBox,correctColor,correctText,binding)

            val boxesWithTexts = gamePlay.getHashMapOfBoxesAndTexts(gamePlay.boxes,gamePlay.colors)
            gamePlay.assignTextsToBoxes(boxesWithTexts,binding)

            var boxesWithColors:HashMap<String,Int>
            do{
                gamePlay.resetBoxesToFalse(gamePlay.boxes)
                gamePlay.resetColorsToFalse(gamePlay.colors)
                gamePlay.assignTextAndColorToRightBox(chosenBox,correctColor,correctText,binding)
                boxesWithColors = gamePlay.getHashMapOfBoxesAndColors(gamePlay.boxes, gamePlay.colors, correctColor)
            }while(!gamePlay.areKeyPairsUnique(boxesWithTexts,gamePlay.convertColorsToNames(boxesWithColors)))

            gamePlay.assignColorsToBoxes(boxesWithColors,binding)

            gamePlay.resetBoxesToFalse(gamePlay.boxes)
            gamePlay.resetColorsToFalse(gamePlay.colors)

            /* For debugging only */
            Log.i("TAG",chosenBox)
            Log.i("TAG",boxesWithTexts.toString())
            Log.i("TAG",gamePlay.convertColorsToNames(boxesWithColors).toString())
            Log.i("TAG",gamePlay.areKeyPairsUnique(boxesWithTexts,gamePlay.convertColorsToNames(boxesWithColors)).toString())
        }

    }

}

