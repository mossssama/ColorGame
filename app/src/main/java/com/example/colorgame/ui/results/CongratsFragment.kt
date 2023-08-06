package com.example.colorgame.ui.results

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.colorgame.domain.AdsManager
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentCongratsBinding
import com.example.colorgame.ui.mainMode.GamePlayFragmentArgs
import com.google.android.gms.ads.*

class CongratsFragment : Fragment() {
    private val args: GamePlayFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentCongratsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_congrats,container,false)
        val adsManager = AdsManager(requireContext(),"CongratsFragment")

        MobileAds.initialize(requireContext()) { adsManager.loadInterstitialAds() }

        binding.scoreIsTv.text=args.score.toString()
        binding.showResults.setOnClickListener { adsManager.showInterstitialAds(binding,args.gameMode) }

        return binding.root
    }

}