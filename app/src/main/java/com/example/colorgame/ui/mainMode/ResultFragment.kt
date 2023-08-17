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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentResultBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_result,container,false)
        val adsManager = AdsManager(requireContext())
        val dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)

        MobileAds.initialize(requireContext()) { loadInterstitialAds(adsManager) }

        setScoreTextView(binding,args.score)

        binding.showResults.setOnClickListener { showInterstitialAds(adsManager,binding,args.gameMode) }

        binding.playAgain.setOnClickListener{ setGameOverToFalse(dataStoreManager); goToGamePlayFragment(binding) }

        return binding.root
    }

    private fun setScoreTextView(binding: FragmentResultBinding, scoreValue:Int){
        binding.scoreResult.text=scoreValue.toString()
    }

    private fun loadInterstitialAds(adsManager: AdsManager){
        adsManager.loadInterstitialAds()
    }

    private fun showInterstitialAds(adsManager: AdsManager, binding: FragmentResultBinding, gameMode:String){
        adsManager.showInterstitialAds(binding,gameMode)
    }

    private fun setGameOverToFalse(dataStoreManager: DataStoreManager) {
        GlobalScope.launch { dataStoreManager.saveGameOver(false) }
    }

    private fun goToGamePlayFragment(binding: FragmentResultBinding){
        Navigation.findNavController(binding.root).navigate(ResultFragmentDirections.goAgainToGamePlayFragment(args.gameMode))
    }

}