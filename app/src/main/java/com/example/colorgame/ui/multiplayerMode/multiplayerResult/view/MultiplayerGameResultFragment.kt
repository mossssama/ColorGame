package com.example.colorgame.ui.multiplayerMode.multiplayerResult.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentMultiplayerResultsBinding
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult
import com.example.colorgame.ui.multiplayerMode.multiplayerResult.viewModel.MultiplayerGameResultViewModel

class MultiplayerGameResultFragment : Fragment() {
    private val args: MultiplayerGameResultFragmentArgs by navArgs()

    private lateinit var binding: FragmentMultiplayerResultsBinding
    private lateinit var viewModel: MultiplayerGameResultViewModel

    private var isViewModelInitialized = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_multiplayer_results, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MultiplayerGameResultViewModel::class.java]
        isViewModelInitialized = true

        loadUiNames(args.myUserName,args.myFriendName)
        loadUiResults(requireContext(),requireActivity())

        binding.playAgain.setOnClickListener {         viewModel.rePlayInit(args.myUserName);   fireProgressFragment(args.myUserName,args.myFriendName) }
        binding.returnToMainPage.setOnClickListener {  viewModel.returnInit(args.myUserName);   goToIntroFragment()                                     }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::binding.isInitialized && isViewModelInitialized) {
            val playerScore = binding.myScoreIsTv.text.toString()
            val oppositeScore = binding.myFriendScoreIsTv.text.toString()
            if (playerScore != "" && oppositeScore != "") {
                viewModel.saveMultiplayerGameResult(outState, MultiplayerGameResult(playerScore.toInt(), oppositeScore.toInt()))
            } else {
                viewModel.saveMultiplayerGameResult(outState, MultiplayerGameResult(-1, -1))
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null && isViewModelInitialized) {
            viewModel.fireStoreManager.readCountDown(args.myFriendName, onSuccess = { it ->
                if (it == 0) {
                    viewModel.loadMultiplayerGameResult(savedInstanceState).observe(requireActivity()) {
                        updateBannerText(it.playerScore, it.oppositeScore)
                        updateScoresUI(it.playerScore, it.oppositeScore)
                    }
                }
            }, onFailure = {})
        }
    }


    private fun loadUiResults(context: Context,activity: FragmentActivity){
        viewModel.fireStoreManager.listenToCountDownChanges(args.myFriendName) { countDown ->
            if(countDown==0) {
                viewModel.getMultiplayerGameResult(viewModel.fireStoreManager,args.myUserName,args.myFriendName,context,lifecycleScope,activity).observe(activity){
                    updateBannerText(it.playerScore, it.oppositeScore)
                    updateScoresUI(it.playerScore, it.oppositeScore)
                }
            }
        }
    }

    private fun goToIntroFragment(){
        Navigation.findNavController(binding.root).navigate(MultiplayerGameResultFragmentDirections.returnToIntroFragment())
    }

    private fun fireProgressFragment(userName:String, friendName:String){
        Navigation.findNavController(binding.root).navigate(MultiplayerGameResultFragmentDirections.returnToProgressFragment(userName, friendName))
    }

    private fun updateBannerText(myScore: Int, myFriendScore: Int){
        binding.congratsOrHardLuck.text=viewModel.getBannerText(myScore,myFriendScore)
    }

    private fun updateScoresUI(myScore: Int, myFriendScore: Int){
        binding.myScoreIsTv.text=myScore.toString()
        binding.myFriendScoreIsTv.text=myFriendScore.toString()
    }

    private fun loadUiNames(myName:String, myFriendName: String){
        binding.myScoreTv.text=myName
        binding.myFriendScoreTv.text=myFriendName
    }


}