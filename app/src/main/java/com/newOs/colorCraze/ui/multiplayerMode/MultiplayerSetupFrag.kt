package com.newOs.colorCraze.ui.multiplayerMode

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.newOs.colorCraze.R
import com.newOs.colorCraze.databinding.FragmentMultiplierBinding

class MultiplayerSetupFrag : Fragment() {

    private lateinit var binding: FragmentMultiplierBinding
    private lateinit var playerName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplier,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fireStoreManager =
            com.newOs.colorCraze.cloudFirestore.FirestoreManager(Firebase.firestore)
        val adsManager = com.newOs.colorCraze.ads.AdsManager(requireContext())

        MobileAds.initialize(requireContext()) { adsManager.loadBannerAds(binding) }     /* Load ads on Banner */

        binding.addPlayer.setOnClickListener { addPlayer(fireStoreManager) }
        binding.addOpposite.setOnClickListener { addOpposite(fireStoreManager) }
        binding.startPlaying.setOnClickListener{ checkYouAddedYourselfAndYourFriend(requireContext()) }
    }

    private fun addPlayer(fireStoreManager: com.newOs.colorCraze.cloudFirestore.FirestoreManager){
        playerName = binding.etOne.text.toString()
        val initPlayer = mapOf("score" to 0,"countDown" to 100,"startPlaying" to false)

        if (playerName.isNotBlank()) {
            fireStoreManager.addUserIfDoesNotExist(requireContext(),playerName, initPlayer,
                onSuccess = {
                    currentlyAddingYouToast(requireContext())
                    fireStoreManager.initPlayerCollection(playerName, initPlayer,
                        onSuccess = { firePlayerAddedSuccessfullyToast(requireContext()) },
                        onFailure = { Toast.makeText(requireContext(),getString(R.string.playerAdditionFailedCheckInternetConnection),Toast.LENGTH_LONG).show() }
                    )
                },
                onFailure = { checkInternetConnectionToast(requireContext()) }
            )
        } else cannotKeepTextFieldEmptyToast(requireContext())
    }

    private fun addOpposite(fireStoreManager: com.newOs.colorCraze.cloudFirestore.FirestoreManager){
        val oppositeName = binding.etTwo.text.toString()

        if (oppositeName.isNotBlank()) {
            fireStoreManager.checkIfUserExists(oppositeName,
                onSuccess = { exists ->
                    if (exists){
                        firePlayerAddedSuccessfullyToast(requireContext())
                        binding.startPlaying.setOnClickListener {
                            setStartPlaying(fireStoreManager)
                            fireProgressFragment(playerName,oppositeName)
                        }
                    }
                    else Toast.makeText(requireContext(), getString(R.string.NoPlayerWithThisUserName), Toast.LENGTH_LONG).show()
                },
                onFailure = { checkInternetConnectionToast(requireContext()) }
            )
        } else cannotKeepTextFieldEmptyToast(requireContext())

    }

    private fun fireProgressFragment(userName:String,friendName:String){
        Navigation.findNavController(binding.root).navigate(MultiplayerSetupFragDirections.goToProgressFragment(userName,friendName))
    }

    private fun firePlayerAddedSuccessfullyToast(context: Context){
        Toast.makeText(context,getString(R.string.playerIsAddedSuccessfully),Toast.LENGTH_SHORT).show()
    }

    private fun currentlyAddingYouToast(context: Context){
        Toast.makeText(context,getString(R.string.WeCurrentlyAddYouAsPlayer),Toast.LENGTH_SHORT).show()
    }

    private fun checkInternetConnectionToast(context: Context){
        Toast.makeText(context,getString(R.string.checkInternetConnection),Toast.LENGTH_LONG).show()
    }

    private fun cannotKeepTextFieldEmptyToast(context: Context){
        Toast.makeText(context,getString(R.string.cannotKeepTextFieldEmpty),Toast.LENGTH_LONG).show()
    }

    private fun checkYouAddedYourselfAndYourFriend(context: Context){
        Toast.makeText(context,getString(R.string.CheckYouAddedYourselfAndYourFriend),Toast.LENGTH_LONG).show()
    }
    /* Can be put in viewModel */
    private fun setStartPlaying(fireStoreManager: com.newOs.colorCraze.cloudFirestore.FirestoreManager){
        fireStoreManager.setStartPlaying(playerName, true, onSuccess = {}, onFailure = {})
    }

}