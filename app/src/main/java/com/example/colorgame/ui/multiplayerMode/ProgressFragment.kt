package com.example.colorgame.ui.multiplayerMode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.colorgame.R
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.databinding.FragmentProgressBinding
import com.example.colorgame.ui.multiplayerMode.multiplayerGamePlay.view.MultiplayerGamePlayFragmentDirections
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProgressFragment : Fragment() {

    private val args: ProgressFragmentArgs by navArgs()
    private lateinit var binding: FragmentProgressBinding
    private lateinit var fireStoreManager: FirestoreManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_progress, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* Go to MultiPlayer GamePlay when the other player clicks on startPlaying */
        fireStoreManager = FirestoreManager(Firebase.firestore)
        fireStoreManager.listenToStartPlayingChanges(args.friendName) { startPlaying ->
            if (startPlaying) { navigation(args.userName,args.friendName) }
        }

    }

    private fun goToMultiplayerGamePlayFragment(userName:String,friendName:String){
        Navigation.findNavController(binding.root).navigate(ProgressFragmentDirections.goToMultiplayerGamePlay(userName, friendName))
    }

    private fun reMultiplayerGamePlayFragment(userName:String,friendName:String){
        findNavController().navigate(MultiplayerGamePlayFragmentDirections.reMultiplayerGamePlay(userName,friendName))
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
