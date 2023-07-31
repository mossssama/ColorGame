package com.example.colorgame.ui.results

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentCongratsBinding
import com.example.colorgame.ui.game.GamePlayFragmentArgs
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class CongratsFragment : Fragment() {
    private val args: GamePlayFragmentArgs by navArgs()

    private var mInterstitialAd: InterstitialAd? = null
    private var tAG = "CongratsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentCongratsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_congrats,container,false)

        MobileAds.initialize(requireContext()) { loadAds() }

        binding.scoreIsTv.text=args.score.toString()
        binding.showResults.setOnClickListener { showAds(binding) }

        return binding.root
    }

    private fun goToResultsFragment(binding: FragmentCongratsBinding){
        val action = CongratsFragmentDirections.navigateFromCongratsToResultsFragment(args.gameMode)
        Navigation.findNavController(binding.root).navigate(action)
    }

    private fun loadAds(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),getString(R.string.game_over_interstitial_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) { mInterstitialAd = null }
            override fun onAdLoaded(interstitialAd: InterstitialAd) { mInterstitialAd = interstitialAd }
        })
    }

    private fun showAds(binding: FragmentCongratsBinding){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(requireActivity())
            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {

                // Called when a click is recorded for an ad.
                override fun onAdClicked() { Log.d(tAG, "Ad was clicked.") }

                // Called when ad is dismissed.
                override fun onAdDismissedFullScreenContent() { Log.d(tAG, "Ad dismissed fullscreen content."); mInterstitialAd = null; loadAds(); goToResultsFragment(binding) }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) { Log.e(tAG, "Ad failed to show fullscreen content.");    mInterstitialAd = null }

                // Called when an impression is recorded for an ad.
                override fun onAdImpression() { Log.d(tAG, "Ad recorded an impression.") }

                // Called when ad is shown.
                override fun onAdShowedFullScreenContent() { Log.d(tAG, "Ad showed fullscreen content.") }
            }
        }
        else { goToResultsFragment(binding);   Log.d(tAG, "The interstitial ad wasn't ready yet.") }
    }

}