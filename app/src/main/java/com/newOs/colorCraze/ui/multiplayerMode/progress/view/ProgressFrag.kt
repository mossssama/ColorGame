package com.newOs.colorCraze.ui.multiplayerMode.progress.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.newOs.colorCraze.R
import com.newOs.colorCraze.databinding.FragmentProgressBinding
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerGamePlay.view.MultiplayerGamePlayFragDirections

class ProgressFrag : Fragment() {

    private val args: ProgressFragArgs by navArgs()
    private lateinit var binding: FragmentProgressBinding
    private lateinit var fireStoreManager: com.newOs.colorCraze.firebase.FirestoreManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_progress, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* Go to MultiPlayer GamePlayImpl when the other player clicks on startPlaying */
        fireStoreManager =
            com.newOs.colorCraze.firebase.FirestoreManager(Firebase.firestore)
        fireStoreManager.listenToStartPlayingChanges(args.friendName) { startPlaying ->
            if (startPlaying) { navigation(args.userName,args.friendName) }
        }

    }

    private fun goToMultiplayerGamePlayFragment(userName:String,friendName:String){
        Navigation.findNavController(binding.root).navigate(
            ProgressFragDirections.goToMultiplayerGamePlay(
                userName,
                friendName
            )
        )
    }

    private fun reMultiplayerGamePlayFragment(userName:String,friendName:String){
        findNavController().navigate(MultiplayerGamePlayFragDirections.reMultiplayerGamePlay(userName,friendName))
    }

    private fun navigation(userName: String, friendName: String) {
        try { goToMultiplayerGamePlayFragment(userName,friendName) }
        catch (e: IllegalArgumentException) { reMultiplayerGamePlayFragment(userName, friendName) }
    }

    /* Can be put in ViewModel */
    override fun onDestroyView() {
        super.onDestroyView()
        fireStoreManager.removeAllStartPlayingListeners()
    }
}
