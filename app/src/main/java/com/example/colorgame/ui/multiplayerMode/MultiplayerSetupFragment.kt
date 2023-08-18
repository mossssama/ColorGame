package com.example.colorgame.ui.multiplayerMode

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentMultiplierBinding
import com.example.colorgame.ads.AdsManager
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MultiplayerSetupFragment : Fragment() {

    private lateinit var binding:FragmentMultiplierBinding
    private lateinit var playerName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplier,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fireStoreManager = FirestoreManager(Firebase.firestore)
        val adsManager = AdsManager(requireContext())

        MobileAds.initialize(requireContext()) { adsManager.loadBannerAds(binding) }     /* Load ads on Banner */

        binding.addPlayer.setOnClickListener { addPlayer(fireStoreManager) }
        binding.addOpposite.setOnClickListener { addOpposite(fireStoreManager) }
    }

    private fun addPlayer(fireStoreManager: FirestoreManager){
        playerName = binding.etOne.text.toString()
        val initPlayer = mapOf("score" to 0,"countDown" to 100,"startPlaying" to false)

        if (playerName.isNotBlank()) {
            fireStoreManager.addUserIfDoesNotExist(playerName, initPlayer,
                onSuccess = {
                    fireStoreManager.initPlayerCollection(playerName, initPlayer,
                        onSuccess = {  firePlayerAddedSuccessfullyToast(requireContext()) },
                        onFailure = { Toast.makeText(requireContext(),"Player addition failed; Check Internet Connection",Toast.LENGTH_LONG).show() }
                    )
                },
                onFailure = { checkInternetConnectionToast(requireContext()) }
            )
        } else Toast.makeText(requireContext(), "Can't keep textField empty", Toast.LENGTH_LONG).show()
    }

    private fun addOpposite(fireStoreManager: FirestoreManager){
        val oppositeName = binding.etTwo.text.toString()

        if (oppositeName.isNotBlank()) {
            fireStoreManager.checkIfUserExists(oppositeName,
                onSuccess = { exists ->
                    firePlayerAddedSuccessfullyToast(requireContext())
                    if (exists){
                        binding.startPlaying.setOnClickListener {
                            setStartPlaying(fireStoreManager)
                            fireProgressFragment(playerName,oppositeName)
                        }
                }
                    else Toast.makeText(requireContext(), "No Player with this userName", Toast.LENGTH_LONG).show()
                },
                onFailure = { checkInternetConnectionToast(requireContext()) }
            )
        } else Toast.makeText(requireContext(), "Can't keep textField empty", Toast.LENGTH_LONG).show()
    }

    private fun fireProgressFragment(userName:String,friendName:String){
        Navigation.findNavController(binding.root).navigate(MultiplayerSetupFragmentDirections.goToProgressFragment(userName,friendName))
    }

    private fun firePlayerAddedSuccessfullyToast(context: Context){
        Toast.makeText(context,"Player is added successfully",Toast.LENGTH_SHORT).show()
    }

    private fun checkInternetConnectionToast(context: Context){
        Toast.makeText(context,"Check Internet Connection",Toast.LENGTH_LONG).show()
    }

    /* Can be put in viewModel */
    private fun setStartPlaying(fireStoreManager: FirestoreManager){
        fireStoreManager.setStartPlaying(playerName, true, onSuccess = {}, onFailure = {})
    }
}