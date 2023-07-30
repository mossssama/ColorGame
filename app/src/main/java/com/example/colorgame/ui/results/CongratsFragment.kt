package com.example.colorgame.ui.results

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentCongratsBinding
import com.example.colorgame.ui.game.GamePlayFragmentArgs

class CongratsFragment : Fragment() {
    private val args: GamePlayFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCongratsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_congrats,container,false)

        binding.scoreIsTv.text=args.score.toString()
        binding.showResults.setOnClickListener { goToResultsFragment(binding) }

        return binding.root
    }

    private fun goToResultsFragment(binding: FragmentCongratsBinding){
        val action = CongratsFragmentDirections.navigateFromCongratsToResultsFragment(args.gameMode)
        Navigation.findNavController(binding.root).navigate(action)
    }
}