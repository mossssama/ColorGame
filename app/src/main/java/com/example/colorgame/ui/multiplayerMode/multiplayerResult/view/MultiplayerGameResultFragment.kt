package com.example.colorgame.ui.multiplayerMode.multiplayerResult.view

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
import com.example.colorgame.cloudFirestore.FirestoreManager
import com.example.colorgame.ui.multiplayerMode.MultiplayerResultFragmentArgs
import com.example.colorgame.ui.multiplayerMode.MultiplayerResultFragmentDirections
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MultiplayerGameResultFragment : Fragment() {
    private val args: MultiplayerResultFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: FragmentMultiplayerResultsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplayer_results, container, false)
        val fireStoreManager = FirestoreManager(Firebase.firestore)

        loadUI(binding,fireStoreManager,args.myUserName,args.myFriendName,args.myScore)

        binding.playAgain.setOnClickListener {         rePlayInit(fireStoreManager);   fireProgressFragment(binding,args.myUserName,args.myFriendName) }
        binding.returnToMainPage.setOnClickListener {  returnInit(fireStoreManager);   goToIntroFragment(binding)                                      }

        return binding.root
    }

    private fun loadUI(binding: FragmentMultiplayerResultsBinding,fireStoreManager: FirestoreManager,myName:String,myFriendName:String,myScore:Int){
        fireStoreManager.listenToCountDownChanges(args.myFriendName) { countDown ->
            if(countDown==0){
                fireStoreManager.readScore(args.myUserName,
                    onSuccess = { score ->
                        fireStoreManager.readScore(args.myFriendName,
                            onSuccess = { friendScore -> updateBannerText(binding,score,friendScore);     updateScoresUI(binding,score,friendScore)
                            }, onFailure = { binding.myScoreIsTv.text=myScore.toString() })
                    }, onFailure = { binding.myScoreIsTv.text=myScore.toString() }
                )
            }
        }
        updateNamesUI(binding,myName,myFriendName)
    }

    private fun goToIntroFragment(binding: FragmentMultiplayerResultsBinding){
        Navigation.findNavController(binding.root).navigate(MultiplayerResultFragmentDirections.returnToIntroFragment())
    }

    private fun fireProgressFragment(binding: FragmentMultiplayerResultsBinding, userName:String, friendName:String){
        Navigation.findNavController(binding.root).navigate(MultiplayerResultFragmentDirections.returnToProgressFragment(userName,friendName))
    }

    private fun updateBannerText(binding: FragmentMultiplayerResultsBinding, myScore: Int, myFriendScore: Int){
        binding.congratsOrHardLuck.text=if (myScore >= myFriendScore) "Winner" else "Loser"
    }

    private fun updateScoresUI(binding: FragmentMultiplayerResultsBinding, myScore: Int, myFriendScore: Int){
        binding.myScoreIsTv.text=myScore.toString()
        binding.myFriendScoreIsTv.text=myFriendScore.toString()
    }

    private fun updateNamesUI(binding:FragmentMultiplayerResultsBinding, myName:String, myFriendName: String){
        binding.myScoreTv.text=myName
        binding.myFriendScoreTv.text=myFriendName
    }

    private fun rePlayInit(fireStoreManager: FirestoreManager){
        resetScore(fireStoreManager)
        setStartPlayingValue(fireStoreManager,true)
    }

    private fun returnInit(fireStoreManager: FirestoreManager){
        setStartPlayingValue(fireStoreManager,false)
    }

    /* Update fireStore */
    private fun resetScore(fireStoreManager: FirestoreManager){
        fireStoreManager.setScoreToZero(args.myUserName, onSuccess = {}, onFailure = {})
    }

    private fun setStartPlayingValue(fireStoreManager: FirestoreManager,value:Boolean){
        fireStoreManager.setStartPlaying(args.myUserName,value, onSuccess = {}, onFailure = {})
    }

}