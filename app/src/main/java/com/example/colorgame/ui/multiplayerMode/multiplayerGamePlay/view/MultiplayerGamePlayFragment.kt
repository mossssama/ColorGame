package com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.colorgame.ads.AdsManager
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentMultiplayerGamePlayBinding
import com.example.colorgame.domain.GamePlay
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.domain.GamePlay.Companion.HUNDRED_SEC_MODE
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.model.MultiplayerGameState
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.viewModel.MultiplayerGameStateViewModel
import com.google.android.gms.ads.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MultiplayerGamePlayFragment : Fragment() {
    private val args: MultiplayerGamePlayFragmentArgs by navArgs()

    private lateinit var fireStoreManager: FirestoreManager
    private val multiplayerGameStateViewModel: MultiplayerGameStateViewModel by viewModels()
    private lateinit var binding: FragmentMultiplayerGamePlayBinding
    private lateinit var gamePlay: GamePlay

    private var shouldExecuteSetScoreToZero = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplayer_game_play,container,false)
        val adsManager = AdsManager(requireContext())                                               /* AdsManager instance */
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)   /* DataStore instance */
        fireStoreManager = FirestoreManager(Firebase.firestore)

        MobileAds.initialize(requireContext()) { loadBannerAds(adsManager,binding); loadInterstitialAds(adsManager) }     /* Load ads */

        shouldExecuteSetScoreToZero = savedInstanceState?.getBoolean("shouldExecuteSetScoreToZero", true) ?: true
        if (shouldExecuteSetScoreToZero) { setScoreToZero();    shouldExecuteSetScoreToZero = false }

        setCountDownToHundred()
        loadUI(binding,args.myUserName,args.myFriendName)             /* Set names */

        /* update scores*/
        fireStoreManager.listenToScoreChanges(args.myUserName) { score -> binding.myScore.text=score.toString() }
        fireStoreManager.listenToScoreChanges(args.myFriendName) { score -> binding.myFriendScore.text=score.toString() }

        /* init game */
        gamePlay= GamePlay(lifecycleScope, requireActivity().baseContext)
        GamePlay.chosenBox = gamePlay.getNewUI(binding)
        gamePlay.setGamePlay(HUNDRED_SEC_MODE,args.myUserName,binding,requireActivity().baseContext,100)

        /* Listen to GameOver Value */
        lifecycleScope.launchWhenStarted {
            dataStoreManager.isGameOver.collect { isGameOver ->
                if(isGameOver){
                    fireStoreManager.readScore(args.myUserName, onSuccess = { myScore ->
                        fireStoreManager.readScore(args.myFriendName, onSuccess = { myFriendScore ->
                            showInterstitialAds(adsManager,binding,args.myUserName,args.myFriendName,myScore,myFriendScore)
                            setGameOverToFalse(dataStoreManager)
                        }, onFailure = {})
                    }, onFailure = {})
                }
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val countDownValue = binding.countdownTextView.text.toString().toLong()
        val playerScore = gamePlay.continuousRightAnswers
        multiplayerGameStateViewModel.saveGameState(outState, MultiplayerGameState(countDownValue,playerScore))
        outState.putBoolean("shouldExecuteSetScoreToZero", shouldExecuteSetScoreToZero)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            multiplayerGameStateViewModel.loadGameState(savedInstanceState).observe(requireActivity()){
                gamePlay.setGamePlay(HUNDRED_SEC_MODE,args.myUserName,binding,requireActivity().baseContext,it.countDownValue)
                gamePlay.continuousRightAnswers=it.playerScore
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shouldExecuteSetScoreToZero = true
        fireStoreManager.removeAllScoreListeners()
        fireStoreManager.removeAllCountDownListeners()
    }

    private fun loadUI(binding:FragmentMultiplayerGamePlayBinding, myName:String, myFriendName:String){
        binding.me.text=myName
        binding.myFriend.text=myFriendName
        binding.myScore.text="0"
        binding.myFriendScore.text="0"
    }

    /* Update FireStore */
    private fun setCountDownToHundred(){
        fireStoreManager.updateCountDown(args.myUserName,100, onSuccess = {}, onFailure = {})
    }
    private fun setScoreToZero(){
        fireStoreManager.setScoreToZero(args.myUserName, onSuccess = {}, onFailure = {})
    }

    /* Ads Methods */
    private fun loadBannerAds(adsManager: AdsManager, binding: FragmentMultiplayerGamePlayBinding){
        adsManager.loadBannerAds(binding)
    }
    private fun loadInterstitialAds(adsManager: AdsManager){
        adsManager.loadInterstitialAds()
    }
    private fun showInterstitialAds(adsManager: AdsManager, binding: FragmentMultiplayerGamePlayBinding, myName: String, myFriendName: String, myScore: Int, myFriendScore:Int){
        adsManager.showInterstitialAds(binding,myName,myFriendName,myScore,myFriendScore)
    }

    /* Update DataStore */
    private fun setGameOverToFalse(dataStoreManager: DataStoreManager) {
        GlobalScope.launch { dataStoreManager.saveGameOver(false) }
    }

}
