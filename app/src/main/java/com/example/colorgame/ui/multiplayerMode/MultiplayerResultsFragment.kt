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
import com.example.colorgame.databinding.FragmentMultiplayerResultsBinding
import com.example.colorgame.firebaseFireStore.FirestoreManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MultiplayerResultsFragment : Fragment() {
    private val args: MultiplayerResultsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMultiplayerResultsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplayer_results, container, false)

        loadUI(binding,args.myUserName,args.myFriendName,args.myScore)

        binding.playAgain.setOnClickListener { goToMultiplayerGamePlayFragment(binding,args.myUserName,args.myFriendName) }
        binding.returnToMainPage.setOnClickListener { goToIntroFragment(binding) }

        return binding.root
    }

    private fun loadUI(binding: FragmentMultiplayerResultsBinding,myName:String,myFriendName:String,myScore:Int){
        val fireStoreManager = FirestoreManager(Firebase.firestore)

        binding.congratsOrHardLuck.text="Wait till your friend finish"

        fireStoreManager.listenToCountDownChanges(args.myFriendName) { countDown ->
            if(countDown==0){
                fireStoreManager.readScore(args.myUserName,
                    onSuccess = { score ->
                        fireStoreManager.readScore(args.myFriendName, onSuccess = { friendScore ->
                            updateBannerText(binding,score,friendScore)
                            updateScoresUI(binding,score,friendScore)
                        }, onFailure = { binding.myScoreIsTv.text=myScore.toString() })
                    }, onFailure = { binding.myScoreIsTv.text=myScore.toString() }
                )
            }
        }

        updateNamesUI(binding,myName,myFriendName)
    }

    private fun goToIntroFragment(binding: FragmentMultiplayerResultsBinding){
        Navigation.findNavController(binding.root).navigate(MultiplayerResultsFragmentDirections.returnToIntroFragment())
    }

    private fun goToMultiplayerGamePlayFragment(binding: FragmentMultiplayerResultsBinding,myName: String,myFriendName:String){
        Navigation.findNavController(binding.root).navigate(MultiplayerResultsFragmentDirections.returnToMultiPlayerGamePlayFragment(myName,myFriendName))
    }

    private fun updateBannerText(binding: FragmentMultiplayerResultsBinding, myScore: Int, myFriendScore: Int){
        binding.congratsOrHardLuck.text=if (myScore >= myFriendScore) "Well Done" else "Hard Luck"
    }

    private fun updateScoresUI(binding: FragmentMultiplayerResultsBinding, myScore: Int, myFriendScore: Int){
        binding.myScoreIsTv.text=myScore.toString()
        binding.myFriendScoreIsTv.text=myFriendScore.toString()
    }

    private fun updateNamesUI(binding:FragmentMultiplayerResultsBinding, myName:String, myFriendName: String){
        binding.myScoreTv.text=myName
        binding.myFriendScoreTv.text=myFriendName
    }

}