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
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.dataStore.DataStoreManager
import com.google.android.gms.ads.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MultiplayerGamePlayFragment : Fragment() {
    private val args: MultiplayerGamePlayFragmentArgs by navArgs()

    private lateinit var fireStoreManager: FirestoreManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentMultiplayerGamePlayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplayer_game_play,container,false)
        val adsManager = AdsManager(requireContext(),"MultiplayerGamePlayFragment")             /* AdsManager instance */
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)   /* DataStore instance */
        fireStoreManager = FirestoreManager(Firebase.firestore)

        MobileAds.initialize(requireContext()) { loadBannerAds(adsManager,binding); loadInterstitialAds(adsManager) }     /* Load ads */

        setCountDownToHundred()
        setScoreToZero()
        loadUI(binding,args.myUserName,args.myFriendName)             /* Set names */

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
                            showInterstitialAds(adsManager,binding,args.myUserName,args.myFriendName,myScore,myFriendScore)
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

    private fun loadUI(binding:FragmentMultiplayerGamePlayBinding, myName:String, myFriendName:String){
        binding.me.text=myName
        binding.myFriend.text=myFriendName
        binding.myScore.text="0"
        binding.myFriendScore.text="0"
    }

    private fun setCountDownToHundred(){
        fireStoreManager.updateCountDown(args.myUserName,100, onSuccess = {}, onFailure = {})
    }
    private fun setScoreToZero(){
        fireStoreManager.setScoreToZero(args.myUserName, onSuccess = {}, onFailure = {})
    }

    private fun loadBannerAds(adsManager: AdsManager,binding: FragmentMultiplayerGamePlayBinding){
        adsManager.loadBannerAds(binding)
    }
    private fun loadInterstitialAds(adsManager: AdsManager){
        adsManager.loadInterstitialAds()
    }
    private fun showInterstitialAds(adsManager: AdsManager,binding: FragmentMultiplayerGamePlayBinding,myName: String,myFriendName: String,myScore: Int,myFriendScore:Int){
        adsManager.showInterstitialAds(binding,myName,myFriendName,myScore,myFriendScore)
    }

    private fun setGameOverToFalse(dataStoreManager: DataStoreManager) {
        GlobalScope.launch { dataStoreManager.saveGameOver(false) }
    }

}
