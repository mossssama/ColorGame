package com.example.colorgame.ui.intro

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.colorgame.R
import com.example.colorgame.jetPackDataStore.DataStoreManager
import com.example.colorgame.databinding.FragmentIntroBinding
import com.example.colorgame.domain.GamePlay.Companion.CONTINUOUS_RIGHT_MODE
import com.example.colorgame.domain.GamePlay.Companion.HUNDRED_SEC_MODE
import com.example.colorgame.domain.GamePlay.Companion.THREE_WRONG_MODE
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.launch

class IntroFragment : Fragment() {

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentIntroBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro,container,false)
        dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)

        initOnClickListeners(binding)                                   /* Init onClickListeners */

        MobileAds.initialize(requireContext()) { loadAds(binding) }     /* Load ads on Banner */

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { dataStoreManager.saveGameOver(false) }
    }

    private fun initOnClickListeners(binding: FragmentIntroBinding){
        binding.correctRun.setOnClickListener {     goToGamePlayFragment(binding, CONTINUOUS_RIGHT_MODE)   }
        binding.hundredSec.setOnClickListener {     goToGamePlayFragment(binding, HUNDRED_SEC_MODE)        }
        binding.threeMistakes.setOnClickListener {  goToGamePlayFragment(binding, THREE_WRONG_MODE)        }
        binding.multiplier.setOnClickListener {     goToMultiplierFragment(binding)                        }
    }

    private fun goToGamePlayFragment(binding: FragmentIntroBinding, gameMode: String){
        val action = IntroFragmentDirections.navigateToGamePlayFragment(gameMode)
        Navigation.findNavController(binding.root).navigate(action)
    }

    private fun goToMultiplierFragment(binding: FragmentIntroBinding){
        val action = IntroFragmentDirections.navigateToMultiplierFragment()
        Navigation.findNavController(binding.root).navigate(action)
    }

    private fun loadAds(binding: FragmentIntroBinding){
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

}