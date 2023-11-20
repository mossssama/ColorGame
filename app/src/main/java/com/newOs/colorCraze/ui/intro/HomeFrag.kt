package com.newOs.colorCraze.ui.intro

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.gms.ads.MobileAds
import com.newOs.colorCraze.R
import com.newOs.colorCraze.dataStore.DataStoreManager
import com.newOs.colorCraze.databinding.FragmentIntroBinding
import com.newOs.colorCraze.domain.GamePlay.Companion.CONTINUOUS_RIGHT_MODE
import com.newOs.colorCraze.domain.GamePlay.Companion.HUNDRED_SEC_MODE
import com.newOs.colorCraze.domain.GamePlay.Companion.THREE_WRONG_MODE
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeFrag : Fragment() {

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentIntroBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro,container,false)
        val adsManager = com.newOs.colorCraze.ads.AdsManager(requireContext())

        dataStoreManager = DataStoreManager.getInstance(requireActivity().applicationContext)

        initOnClickListeners(binding)                                   /* Init onClickListeners */

        MobileAds.initialize(requireContext()) { adsManager.loadBannerAds(binding) }     /* Load ads on Banner */

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { dataStoreManager.saveGameOver(false) }
        Timber.i("onResume")
    }

    private fun initOnClickListeners(binding: FragmentIntroBinding){
        binding.correctRun.setOnClickListener {     goToGamePlayFragment(binding, CONTINUOUS_RIGHT_MODE)   }
        binding.hundredSec.setOnClickListener {     goToGamePlayFragment(binding, HUNDRED_SEC_MODE)        }
        binding.threeMistakes.setOnClickListener {  goToGamePlayFragment(binding, THREE_WRONG_MODE)        }
        binding.multiplier.setOnClickListener {     goToMultiPlayerFragment(binding)                       }
    }

    private fun goToGamePlayFragment(binding: FragmentIntroBinding, gameMode: String){
        Navigation.findNavController(binding.root).navigate(HomeFragDirections.goToGamePlayFragment(gameMode))
    }

    private fun goToMultiPlayerFragment(binding: FragmentIntroBinding){
        Navigation.findNavController(binding.root).navigate(HomeFragDirections.goToMultiplayerFragment())
    }

}