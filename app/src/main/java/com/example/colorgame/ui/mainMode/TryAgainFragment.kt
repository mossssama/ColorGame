package com.example.colorgame.ui.mainMode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.colorgame.domain.AdsManager
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentTryAgainBinding
import com.google.android.gms.ads.*

class TryAgainFragment : Fragment() {
    private val args: GamePlayFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentTryAgainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_try_again,container,false)
        val adsManager = AdsManager(requireContext(),"TryAgainFragment")

        MobileAds.initialize(requireContext()) { loadInterstitialAds(adsManager) }

        binding.scoreIsTv.text=args.score.toString()
        binding.showResults.setOnClickListener { showInterstitialAds(adsManager,binding,args.gameMode) }

        return binding.root
    }


    private fun loadInterstitialAds(adsManager: AdsManager){
        adsManager.loadInterstitialAds()
    }
    private fun showInterstitialAds(adsManager: AdsManager, binding: FragmentTryAgainBinding, gameMode:String){
        adsManager.showInterstitialAds(binding,gameMode)
    }

}