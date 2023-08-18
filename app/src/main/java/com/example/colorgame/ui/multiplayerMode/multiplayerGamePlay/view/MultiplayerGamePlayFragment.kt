package com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.colorgame.ads.AdsManager
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentMultiplayerGamePlayBinding
import com.example.colorgame.domain.GamePlay
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.domain.GamePlay.Companion.HUNDRED_SEC_MODE
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.model.MultiplayerGameState
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.viewModel.MultiplayerGameStateViewModel
import com.google.android.gms.ads.*

class MultiplayerGamePlayFragment : Fragment() {
    private val args: MultiplayerGamePlayFragmentArgs by navArgs()

    private lateinit var binding: FragmentMultiplayerGamePlayBinding
    private lateinit var viewModel: MultiplayerGameStateViewModel

    private lateinit var gamePlay: GamePlay

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplayer_game_play,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =  ViewModelProvider(this)[MultiplayerGameStateViewModel::class.java]

        val adsManager = AdsManager(requireContext())
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)

        MobileAds.initialize(requireContext()) { loadBannerAds(adsManager); viewModel.loadInterstitialAds(adsManager) }     /* Load ads */

        viewModel.shouldExecuteSetScoreToZero = savedInstanceState?.getBoolean("shouldExecuteSetScoreToZero", true) ?: true
        if (viewModel.shouldExecuteSetScoreToZero) { viewModel.setScoreToZero(args.myUserName);    viewModel.shouldExecuteSetScoreToZero = false }

        viewModel.setCountDownToHundred(args.myUserName)
        loadUI(args.myUserName,args.myFriendName)             /* Set names */

        /* update scores*/
        viewModel.fireStoreManager.listenToScoreChanges(args.myUserName) { score -> binding.myScore.text=score.toString() }
        viewModel.fireStoreManager.listenToScoreChanges(args.myFriendName) { score -> binding.myFriendScore.text=score.toString() }

        /* init game */
        gamePlay= GamePlay(lifecycleScope, requireActivity().baseContext)
        GamePlay.chosenBox = gamePlay.getNewUI(binding)
        gamePlay.setGamePlay(HUNDRED_SEC_MODE,args.myUserName,binding,requireActivity().baseContext,100)

        /* Listen to GameOver Value */
        listenToGameOverChanges(dataStoreManager,adsManager)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val countDownValue = binding.countdownTextView.text.toString().toLong()
        val playerScore = gamePlay.continuousRightAnswers
        viewModel.saveGameState(outState, MultiplayerGameState(countDownValue,playerScore))
        outState.putBoolean("shouldExecuteSetScoreToZero", viewModel.shouldExecuteSetScoreToZero)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            viewModel.loadGameState(savedInstanceState).observe(requireActivity()){
                gamePlay.setGamePlay(HUNDRED_SEC_MODE,args.myUserName,binding,requireActivity().baseContext,it.countDownValue)
                gamePlay.continuousRightAnswers=it.playerScore
            }
        }
    }

    private fun listenToGameOverChanges(dataStoreManager: DataStoreManager, adsManager: AdsManager) {
        lifecycleScope.launchWhenStarted {
            dataStoreManager.isGameOver.collect { isGameOver ->
                if(isGameOver){
                    viewModel.fireStoreManager.readScore(args.myUserName, onSuccess = { myScore ->
                        viewModel.fireStoreManager.readScore(args.myFriendName, onSuccess = { myFriendScore ->
                            showInterstitialAds(adsManager,args.myUserName,args.myFriendName,myScore,myFriendScore)
                            viewModel.setGameOverToFalse(dataStoreManager)
                        }, onFailure = {})
                    }, onFailure = {})
                }
            }
        }
    }

    private fun loadUI(myName:String, myFriendName:String){
        binding.me.text=myName
        binding.myFriend.text=myFriendName
        binding.myScore.text="0"
        binding.myFriendScore.text="0"
    }


    private fun loadBannerAds(adsManager: AdsManager){
        adsManager.loadBannerAds(binding)
    }

    private fun showInterstitialAds(adsManager: AdsManager, myName: String, myFriendName: String, myScore: Int, myFriendScore:Int){
        adsManager.showInterstitialAds(binding,myName,myFriendName,myScore,myFriendScore)
    }



}
