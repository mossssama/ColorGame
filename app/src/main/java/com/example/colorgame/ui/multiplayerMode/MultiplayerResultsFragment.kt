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

class MultiplayerResultsFragment : Fragment() {
    private val args: MultiplayerResultsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMultiplayerResultsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiplayer_results, container, false)

        loadUI(binding,args.myUserName,args.myFriendName,args.myScore,args.myFriendScore)

        binding.playAgain.setOnClickListener { goToMultiplayerGamePlayFragment(binding,args.myUserName,args.myFriendName) }

        binding.returnToMainPage.setOnClickListener { goToIntroFragment(binding) }

        return binding.root
    }

    private fun loadUI(binding: FragmentMultiplayerResultsBinding,myName:String,myFriendName:String,myScore:Int,myFriendScore:Int){
        if(args.myScore>=args.myFriendScore) binding.congratsOrHardLuck.text="Well Done"
        else binding.congratsOrHardLuck.text="Hard Luck"

        binding.myScoreTv.text=myName
        binding.myFriendScoreTv.text=myFriendName
        binding.myScoreIsTv.text=myScore.toString()
        binding.myFriendScoreIsTv.text=myFriendScore.toString()
    }

    private fun goToIntroFragment(binding: FragmentMultiplayerResultsBinding){
        val action = MultiplayerResultsFragmentDirections.returnToIntroFragment()
        Navigation.findNavController(binding.root).navigate(action)
    }

    private fun goToMultiplayerGamePlayFragment(binding: FragmentMultiplayerResultsBinding,myName: String,myFriendName:String){
        val action = MultiplayerResultsFragmentDirections.returnToMultiPlayerGamePlayFragment(myName,myFriendName)
        Navigation.findNavController(binding.root).navigate(action)
    }

}