package com.newOs.colorCraze.ui.mainMode.result.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.*
import com.newOs.colorCraze.datastore.DataStoreManager
import com.newOs.colorCraze.databinding.FragmentResultBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.newOs.colorCraze.R
import com.newOs.colorCraze.ui.mainMode.gamePlay.view.GamePlayFragArgs

class ResultFrag : Fragment() {
    private val args: GamePlayFragArgs by navArgs()
    private lateinit var binding: FragmentResultBinding

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")        // Create a DataStore instance using preferencesDataStore extension

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adsManager = com.newOs.colorCraze.ads.AdsManager(requireContext())
        val dataStoreManager = DataStoreManager.create(requireContext().dataStore)

        MobileAds.initialize(requireContext()) { loadInterstitialAds(adsManager,getString(R.string.singlePlayer_interstitial_id_mockup)) }

        setScoreTextView(args.score)

        binding.showResults.setOnClickListener { showInterstitialAds(adsManager,args.gameMode,getString(R.string.singlePlayer_interstitial_id_mockup)) }

        binding.playAgain.setOnClickListener{ setGameOverToFalse(dataStoreManager); goToGamePlayFragment() }
    }

    private fun goToGamePlayFragment(){
        Navigation.findNavController(binding.root).navigate(
            ResultFragDirections.goAgainToGamePlayFragment(args.gameMode)
        )
    }

    private fun setScoreTextView(scoreValue:Int){
        binding.scoreResult.text=scoreValue.toString()
    }

    private fun showInterstitialAds(adsManager: com.newOs.colorCraze.ads.AdsManager, gameMode:String, unitId:String){
        adsManager.showInterstitialAds(binding,gameMode, unitId)
    }

    /* Can be put in ViewModel */
    private fun setGameOverToFalse(dataStoreManager: DataStoreManager) {
        GlobalScope.launch { dataStoreManager.saveGameOver(false) }
    }

    private fun loadInterstitialAds(adsManager: com.newOs.colorCraze.ads.AdsManager, unitId: String){
        adsManager.loadInterstitialAds(unitId)
    }

}