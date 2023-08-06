package com.example.colorgame.ui.multiplayerMode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.colorgame.domain.AdsManager
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentMultiplayerGamePlayBinding
import com.example.colorgame.domain.GamePlay
import com.example.colorgame.firebaseFireStore.FirestoreManager
import com.example.colorgame.jetPackDataStore.DataStoreManager
import com.google.android.gms.ads.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MultiplayerGamePlayFragment : Fragment() {
    private val args: MultiplayerGamePlayFragmentArgs by navArgs()

    private lateinit var fireStoreManager: FirestoreManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMultiplayerGamePlayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplayer_game_play,container,false)
        val adsManager = AdsManager(requireContext(),"MultiplayerGamePlayFragment")             /* AdsManager instance */
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)   /* DataStore instance */

        MobileAds.initialize(requireContext()) { adsManager.loadBannerAds(binding); adsManager.loadInterstitialAds() }     /* Load ads */

        resetValues()
        setNames(binding,args.myUserName,args.myFriendName)             /* Set names */

        fireStoreManager = FirestoreManager(Firebase.firestore)

        /* update scores*/
        fireStoreManager.listenToScoreChanges(args.myUserName) { score -> binding.myScore.text=score.toString() }
        fireStoreManager.listenToScoreChanges(args.myFriendName) { score -> binding.myFriendScore.text=score.toString() }

        /* init game */
        val gamePlay= GamePlay(lifecycleScope, requireActivity().baseContext)
        GamePlay.chosenBox = gamePlay.getNewUI(binding)
        gamePlay.setGamePlay(GamePlay.HUNDRED_SEC_MODE,args.myUserName,binding,requireActivity().baseContext)

        /* Listen to GameOver Value */
        lifecycleScope.launchWhenStarted {
            dataStoreManager.isGameOver.collect { isGameOver ->
                if(isGameOver){
                    fireStoreManager.readScore(args.myUserName, onSuccess = { myScore ->
                        fireStoreManager.readScore(args.myFriendName, onSuccess = { myFriendScore ->
                            adsManager.showInterstitialAds(binding,args.myUserName,args.myFriendName,myScore,myFriendScore)
                            setGameOverToFalse(dataStoreManager)
                        }, onFailure = {})
                    }, onFailure = {})
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fireStoreManager.removeAllScoreListeners()
        fireStoreManager.removeAllCountDownListeners()
    }

    private fun setNames(binding:FragmentMultiplayerGamePlayBinding,myName:String,myFriendName:String){
        binding.me.text=myName
        binding.myFriend.text=myFriendName
    }

    private fun resetValues(){
        fireStoreManager.updateCountDown(args.myUserName,100, onSuccess = {}, onFailure = {})
        fireStoreManager.setScoreToZero(args.myUserName, onSuccess = {}, onFailure = {})
        fireStoreManager.setScoreToZero(args.myFriendName, onSuccess = {}, onFailure = {})
    }

    private fun setGameOverToFalse(dataStoreManager: DataStoreManager) {
        GlobalScope.launch { dataStoreManager.saveGameOver(false) }
    }

}
