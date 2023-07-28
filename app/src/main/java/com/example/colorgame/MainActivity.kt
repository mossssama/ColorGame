package com.example.colorgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.colorgame.databinding.ActivityMainBinding
import com.example.colorgame.room.ScoreDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var scoreDatabase: ScoreDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        scoreDatabase = ScoreDatabase.getInstance(this)
////        readNumberOfScores()

        val gamePlay=GamePlay(lifecycleScope,baseContext)
        GamePlay.chosenBox = gamePlay.getNewUI(binding)
        gamePlay.setGamePlay(GamePlay.HUNDRED_SEC_MODE,binding,baseContext)

    }

//    private fun readNumberOfScores() {
//        lifecycleScope.launch {
//            scoreDatabase.scoreDao.getNumberOfScoresByGameMode(GamePlay.HUNDRED_SEC_MODE).collect{
//                Toast.makeText(baseContext,"$it",Toast.LENGTH_LONG).show()
//            }
//        }
//    }

}

