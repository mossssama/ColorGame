package com.example.colorgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        GamePlay.chosenBox = gamePlay.getNewUI(binding)

        gamePlay.onBoxesListener(GamePlay.CONTINUOUS_RIGHT_MODE,binding,baseContext)

    }

}

