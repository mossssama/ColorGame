package com.example.colorgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.colorgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var gamePlay: GamePlay
    private lateinit var chosenBox:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        gamePlay=GamePlay()

        gamePlay.initColors(this)

        chosenBox = gamePlay.getNewUI(binding)
        onBoxesListener()
    }

    private fun onBoxesListener() {
        boxOnClickListener(binding.boxOne)
        boxOnClickListener(binding.boxTwo)
        boxOnClickListener(binding.boxThree)
        boxOnClickListener(binding.boxFour)
        boxOnClickListener(binding.boxFive)
        boxOnClickListener(binding.boxSix)
        boxOnClickListener(binding.boxSeven)
    }

    private fun boxOnClickListener(boxView: View) {
        boxView.setOnClickListener {
            val boxId = when (boxView.id) {
                R.id.boxOne -> GamePlay.BOX_ONE
                R.id.boxTwo -> GamePlay.BOX_TWO
                R.id.boxThree -> GamePlay.BOX_THREE
                R.id.boxFour -> GamePlay.BOX_FOUR
                R.id.boxFive -> GamePlay.BOX_FIVE
                R.id.boxSix -> GamePlay.BOX_SIX
                R.id.boxSeven -> GamePlay.BOX_SEVEN
                else -> return@setOnClickListener
            }

            /* in (if) we can handle right answers counts & in (else) we can handle gameOver */
            if (chosenBox == boxId) Toast.makeText(baseContext, "Right", Toast.LENGTH_SHORT).show()
            else Toast.makeText(baseContext, "Wrong", Toast.LENGTH_SHORT).show()

            chosenBox = gamePlay.getNewUI(binding)
        }
    }



}

