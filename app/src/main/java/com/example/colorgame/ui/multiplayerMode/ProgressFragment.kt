package com.example.colorgame.ui.multiplayerMode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.colorgame.R
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.databinding.FragmentProgressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProgressFragment : Fragment() {

    private val args: ProgressFragmentArgs by navArgs()
    private lateinit var fireStoreManager: FirestoreManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentProgressBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_progress, container, false)
        fireStoreManager = FirestoreManager(Firebase.firestore)

        /* Go to MultiPlayer GamePlay when the other player clicks on startPlaying */
        fireStoreManager.listenToStartPlayingChanges(args.friendName) { startPlaying ->
            if (startPlaying) { view?.let { goToMultiplayerGamePlayFragment(it,args.userName,args.friendName) } }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fireStoreManager.removeAllStartPlayingListeners()
    }

    private fun goToMultiplayerGamePlayFragment(view:View,userName:String,friendName:String){
        Navigation.findNavController(view).navigate(ProgressFragmentDirections.navigateToMultiplayerGamePlay(userName, friendName))
    }

}
