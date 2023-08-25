package com.example.colorgame.ui.mainMode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.colorgame.ads.AdsManager
import com.example.colorgame.R
import com.example.colorgame.dataStore.DataStoreManager
import com.example.colorgame.databinding.FragmentResultBinding
import com.example.colorgame.ui.mainMode.gamePlay.view.GamePlayFragmentArgs
import com.google.android.gms.ads.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {
    private val args: GamePlayFragmentArgs by navArgs()
    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adsManager = AdsManager(requireContext())
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)

        MobileAds.initialize(requireContext()) { loadInterstitialAds(adsManager,getString(R.string.singlePlayer_interstitial_id_mockup)) }

        setScoreTextView(args.score)

        binding.showResults.setOnClickListener { showInterstitialAds(adsManager,args.gameMode,getString(R.string.singlePlayer_interstitial_id_mockup)) }

        binding.playAgain.setOnClickListener{ setGameOverToFalse(dataStoreManager); goToGamePlayFragment() }
    }

    private fun goToGamePlayFragment(){
        Navigation.findNavController(binding.root).navigate(ResultFragmentDirections.goAgainToGamePlayFragment(args.gameMode))
    }

    private fun setScoreTextView(scoreValue:Int){
        binding.scoreResult.text=scoreValue.toString()
    }

    private fun showInterstitialAds(adsManager: AdsManager, gameMode:String,unitId:String){
        adsManager.showInterstitialAds(binding,gameMode, unitId)
    }

    /* Can be put in ViewModel */
    private fun setGameOverToFalse(dataStoreManager: DataStoreManager) {
        GlobalScope.launch { dataStoreManager.saveGameOver(false) }
    }

    private fun loadInterstitialAds(adsManager: AdsManager,unitId: String){
        adsManager.loadInterstitialAds(unitId)
    }

}