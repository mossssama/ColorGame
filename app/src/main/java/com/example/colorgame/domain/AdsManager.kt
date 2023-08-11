package com.example.colorgame.domain

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.navigation.Navigation
import com.example.colorgame.R
import com.example.colorgame.databinding.*
import com.example.colorgame.ui.mainMode.ResultFragmentDirections
import com.example.colorgame.ui.multiplayerMode.MultiplayerGamePlayFragmentDirections
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdsManager(private val context: Context,private val tAG: String) {
    private var mInterstitialAd: InterstitialAd? = null

    /* load Banner methods */
    fun loadBannerAds(binding: FragmentMultiplayerGamePlayBinding){
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    fun loadBannerAds(binding: FragmentIntroBinding){
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    fun loadBannerAds(binding: FragmentMultiplierBinding){
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
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
        Navigation.findNavController(binding.root).navigate(MultiplayerGamePlayFragmentDirections.goToMultiplayerResultsFragment(myUserName, myFriendName, myScore, myFriendScore))
    }


    fun showInterstitialAds(binding: FragmentResultBinding,gameMode: String){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(context as Activity)
            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {

                // Called when a click is recorded for an ad.
                override fun onAdClicked() { Log.d(tAG, "Ad was clicked.") }

                // Called when ad is dismissed.
                override fun onAdDismissedFullScreenContent() { Log.d(tAG, "Ad dismissed fullscreen content."); mInterstitialAd = null; loadInterstitialAds(); goToScoresHistoryFragment(binding,gameMode) }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) { Log.e(tAG, "Ad failed to show fullscreen content.");    mInterstitialAd = null }

                // Called when an impression is recorded for an ad.
                override fun onAdImpression() { Log.d(tAG, "Ad recorded an impression.") }

                // Called when ad is shown.
                override fun onAdShowedFullScreenContent() { Log.d(tAG, "Ad showed fullscreen content.") }
            }
        }
        else { goToScoresHistoryFragment(binding,gameMode);   Log.d(tAG, "The interstitial ad wasn't ready yet.") }
    }

    private fun goToScoresHistoryFragment(binding: FragmentResultBinding, gameMode: String){
        Navigation.findNavController(binding.root).navigate(ResultFragmentDirections.goToScoresHistoryFragment(gameMode))
    }

}