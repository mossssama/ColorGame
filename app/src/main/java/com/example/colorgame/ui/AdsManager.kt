package com.example.colorgame.domain

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.navigation.Navigation
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentCongratsBinding
import com.example.colorgame.databinding.FragmentIntroBinding
import com.example.colorgame.databinding.FragmentMultiplayerGamePlayBinding
import com.example.colorgame.databinding.FragmentTryAgainBinding
import com.example.colorgame.ui.multiplayerMode.MultiplayerGamePlayFragmentDirections
import com.example.colorgame.ui.results.CongratsFragmentDirections
import com.example.colorgame.ui.results.TryAgainFragmentDirections
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdsManager(private val context: Context,private val tAG: String) {
    private var mInterstitialAd: InterstitialAd? = null

    fun loadBannerAds(binding: FragmentMultiplayerGamePlayBinding){
        binding.adView.loadAd(AdRequest.Builder().build())
    }

    fun loadBannerAds(binding: FragmentIntroBinding){
        binding.adView.loadAd(AdRequest.Builder().build())
    }

    fun loadInterstitialAds(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context,context.getString(R.string.game_over_interstitial_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) { mInterstitialAd = null }
            override fun onAdLoaded(interstitialAd: InterstitialAd) { mInterstitialAd = interstitialAd }
        })
    }

    fun showInterstitialAds(binding: FragmentMultiplayerGamePlayBinding, myUserName: String, myFriendName: String, myScore: Int, myFriendScore: Int) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(context as Activity)
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    Log.d(tAG, "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    Log.d(tAG, "Ad dismissed fullscreen content.")
                    mInterstitialAd = null
                    loadInterstitialAds()
                    goToMultiplayerResultsFragment(binding, myUserName, myFriendName, myScore, myFriendScore)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(tAG, "Ad failed to show fullscreen content.")
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    Log.d(tAG, "Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(tAG, "Ad showed fullscreen content.")
                }
            }
        } else {
            goToMultiplayerResultsFragment(binding, myUserName, myFriendName, myScore, myFriendScore)
            Log.d(tAG, "The interstitial ad wasn't ready yet.")
        }
    }

    fun goToMultiplayerResultsFragment(binding: FragmentMultiplayerGamePlayBinding,myUserName:String,myFriendName:String,myScore:Int,myFriendScore:Int){
        Navigation.findNavController(binding.root).navigate(MultiplayerGamePlayFragmentDirections.navigateToMultiplayerResultsFragment(myUserName, myFriendName, myScore, myFriendScore))
    }


    fun showInterstitialAds(binding: FragmentCongratsBinding,gameMode: String){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(context as Activity)
            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {

                // Called when a click is recorded for an ad.
                override fun onAdClicked() { Log.d(tAG, "Ad was clicked.") }

                // Called when ad is dismissed.
                override fun onAdDismissedFullScreenContent() { Log.d(tAG, "Ad dismissed fullscreen content."); mInterstitialAd = null; loadInterstitialAds(); goToResultsFragment(binding,gameMode) }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) { Log.e(tAG, "Ad failed to show fullscreen content.");    mInterstitialAd = null }

                // Called when an impression is recorded for an ad.
                override fun onAdImpression() { Log.d(tAG, "Ad recorded an impression.") }

                // Called when ad is shown.
                override fun onAdShowedFullScreenContent() { Log.d(tAG, "Ad showed fullscreen content.") }
            }
        }
        else { goToResultsFragment(binding,gameMode);   Log.d(tAG, "The interstitial ad wasn't ready yet.") }
    }

    private fun goToResultsFragment(binding: FragmentCongratsBinding,gameMode: String){
        Navigation.findNavController(binding.root).navigate(CongratsFragmentDirections.navigateFromCongratsToResultsFragment(gameMode))
    }

    fun showInterstitialAds(binding: FragmentTryAgainBinding,gameMode: String){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(context as Activity)
            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {

                // Called when a click is recorded for an ad.
                override fun onAdClicked() { Log.d(tAG, "Ad was clicked.") }

                // Called when ad is dismissed.
                override fun onAdDismissedFullScreenContent() { Log.d(tAG, "Ad dismissed fullscreen content."); mInterstitialAd = null; loadInterstitialAds(); goToResultsFragment(binding,gameMode) }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) { Log.e(tAG, "Ad failed to show fullscreen content.");    mInterstitialAd = null }

                // Called when an impression is recorded for an ad.
                override fun onAdImpression() { Log.d(tAG, "Ad recorded an impression.") }

                // Called when ad is shown.
                override fun onAdShowedFullScreenContent() { Log.d(tAG, "Ad showed fullscreen content.") }
            }
        }
        else { goToResultsFragment(binding, gameMode);   Log.d(tAG, "The interstitial ad wasn't ready yet.") }
    }

    private fun goToResultsFragment(binding: FragmentTryAgainBinding,gameMode: String){
        Navigation.findNavController(binding.root).navigate(TryAgainFragmentDirections.navigateFromTryAgainToResultsFragment(gameMode))
    }

}