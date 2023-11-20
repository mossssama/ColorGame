package com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.newOs.colorCraze.R
import com.newOs.colorCraze.databinding.FragmentMultiplayerResultsBinding
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.model.MultiplayerGameResult
import com.newOs.colorCraze.ui.multiplayerMode.multiplayerResult.viewModel.MultiplayerGameResultViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultiplayerGameResultFrag : Fragment() {
    private val args: MultiplayerGameResultFragArgs by navArgs()
    private val viewModel: MultiplayerGameResultViewModel by viewModels()

    private lateinit var binding: FragmentMultiplayerResultsBinding

    private var isViewModelInitialized = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_multiplayer_results, container, false)
        binding.lifecycleOwner = viewLifecycleOwner // This ensures LiveData updates are observed correctly.
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    viewModel.loadMultiplayerGameResult(savedInstanceState).observe(viewLifecycleOwner) {
                        updateBannerText(requireContext(),it.playerScore, it.oppositeScore)
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
                    updateBannerText(context,it.playerScore, it.oppositeScore)
                    updateScoresUI(it.playerScore, it.oppositeScore)
                }
            }
        }
    }

    private fun goToIntroFragment(){
        Navigation.findNavController(binding.root).navigate(MultiplayerGameResultFragDirections.returnToIntroFragment())
    }

    private fun fireProgressFragment(userName:String, friendName:String){
        Navigation.findNavController(binding.root).navigate(MultiplayerGameResultFragDirections.returnToProgressFragment(userName, friendName))
    }

    private fun updateBannerText(context: Context,myScore: Int, myFriendScore: Int){
        binding.congratsOrHardLuck.text=viewModel.getBannerText(context,myScore,myFriendScore)
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
