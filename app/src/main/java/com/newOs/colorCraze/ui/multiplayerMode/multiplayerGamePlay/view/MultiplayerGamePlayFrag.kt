package com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.newOs.colorCraze.R
import com.newOs.colorCraze.datastore.DataStoreManager
import com.newOs.colorCraze.databinding.FragmentMultiplayerGamePlayBinding
import com.newOs.colorCraze.domain.GamePlay
import com.newOs.colorCraze.helpers.Constants.HUNDRED_SEC_MODE
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.logic.MultiplayerGamePlayLogic
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.model.MultiplayerGameState
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.viewModel.MultiplayerGameStateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultiplayerGamePlayFrag : Fragment() {
    private val args: MultiplayerGamePlayFragArgs by navArgs()
    private val viewModel: MultiplayerGameStateViewModel by viewModels()

    private lateinit var binding: FragmentMultiplayerGamePlayBinding
    private lateinit var gamePlay: MultiplayerGamePlayLogic

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplayer_game_play,container,false)
        binding.lifecycleOwner = viewLifecycleOwner // This ensures LiveData updates are observed correctly.
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adsManager = com.newOs.colorCraze.ads.AdsManager(requireContext())
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)

        viewModel.shouldExecuteSetScoreToZero = savedInstanceState?.getBoolean("shouldExecuteSetScoreToZero", true) ?: true
        if (viewModel.shouldExecuteSetScoreToZero) { viewModel.setScoreToZero(args.myUserName);    viewModel.shouldExecuteSetScoreToZero = false }

        viewModel.setCountDownToHundred(args.myUserName)
        loadUI(args.myUserName,args.myFriendName)             /* Set names */

        /* update scores*/
        viewModel.fireStoreManager.listenToScoreChanges(args.myUserName) { score -> binding.myScore.text=score.toString() }
        viewModel.fireStoreManager.listenToScoreChanges(args.myFriendName) { score -> binding.myFriendScore.text=score.toString() }

        /* init game */
        gamePlay= MultiplayerGamePlayLogic(lifecycleScope, requireActivity().baseContext)
        GamePlay.chosenBox = gamePlay.getNewUI(binding.root)
        gamePlay.setMultiplayerGamePlay(HUNDRED_SEC_MODE,args.myUserName,binding.root,requireActivity().baseContext,100)

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
            viewModel.loadGameState(savedInstanceState).observe(viewLifecycleOwner){
                gamePlay.setMultiplayerGamePlay(HUNDRED_SEC_MODE,args.myUserName,binding.root,requireActivity().baseContext,it.countDownValue)
                gamePlay.continuousRightAnswers=it.playerScore
            }
        }
    }

    private fun listenToGameOverChanges(dataStoreManager: DataStoreManager, adsManager: com.newOs.colorCraze.ads.AdsManager) {
        lifecycleScope.launchWhenStarted {
            dataStoreManager.isGameOver.collect { isGameOver ->
                if(isGameOver){
                    viewModel.fireStoreManager.readScore(args.myUserName, onSuccess = { myScore ->
                        viewModel.fireStoreManager.readScore(args.myFriendName, onSuccess = { myFriendScore ->
                            viewModel.showInterstitialAds(requireActivity(),adsManager,args.myUserName,args.myFriendName,myScore,myFriendScore,getString(R.string.multiPlayer_interstitial_id_mockup))
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

}
