package com.example.colorgame.ui.multiplayerMode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentMultiplierBinding
import com.example.colorgame.domain.AdsManager
import com.example.colorgame.firebaseFireStore.FirestoreManager
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MultiplayerSetupFragment : Fragment() {

    private lateinit var currentPlayerUserName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMultiplierBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplier,container,false)
        val fireStoreManager = FirestoreManager(Firebase.firestore)
        val adsManager = AdsManager(requireContext(),"MultiplayerSetupFragment")

        MobileAds.initialize(requireContext()) { adsManager.loadBannerAds(binding) }     /* Load ads on Banner */

        binding.addMe.setOnClickListener { addMe(binding,fireStoreManager) }
        binding.addFriend.setOnClickListener { addMyFriend(binding,fireStoreManager) }

        return binding.root
    }

    private fun goToMultiplayerGamePlayFragment(binding: FragmentMultiplierBinding,myUserName:String,myFriendUserName: String){
        Navigation.findNavController(binding.root).navigate(MultiplayerSetupFragmentDirections.navigateToMultiplayerGamePlay(myUserName, myFriendUserName))
    }

    private fun addMe(binding: FragmentMultiplierBinding,fireStoreManager: FirestoreManager){
        currentPlayerUserName = binding.etOne.text.toString()
        val initPlayer = mapOf("score" to 0,"countDown" to 100)

        if (currentPlayerUserName.isNotBlank()) {
            fireStoreManager.addUserIfDoesNotExist(currentPlayerUserName, initPlayer,
                onSuccess = { fireStoreManager.initPlayerKeyValuePair(currentPlayerUserName, initPlayer, onSuccess = {}) {} },
                onFailure = { e -> Log.i("TAG", "Error adding user or initializing score.", e) }
            )
        } else Toast.makeText(requireContext(), "Can't keep textField empty", Toast.LENGTH_LONG).show()
    }

    private fun addMyFriend(binding: FragmentMultiplierBinding, fireStoreManager: FirestoreManager){
        val friendUserName = binding.etTwo.text.toString()
        if (friendUserName.isNotBlank()) {
            fireStoreManager.checkIfUserExists(friendUserName,
                onSuccess = { exists ->
                    if (exists) binding.startPlaying.setOnClickListener { goToMultiplayerGamePlayFragment(binding, currentPlayerUserName, friendUserName) }
                    else Toast.makeText(requireContext(), "No Player with this userName", Toast.LENGTH_LONG).show()
                },
                onFailure = { exception -> Log.i("TAG", "Check failed", exception) }
            )
        } else Toast.makeText(requireContext(), "Can't keep textField empty", Toast.LENGTH_LONG).show()
    }
}