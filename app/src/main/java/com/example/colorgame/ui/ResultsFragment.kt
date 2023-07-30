package com.example.colorgame.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.colorgame.R
import com.example.colorgame.databinding.FragmentResultsBinding

class ResultsFragment : Fragment() {

    private val argsOne: CongratsFragmentArgs by navArgs()
    private val argsTwo: TryAgainFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentResultsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_results,container,false)

        /* To be used in the dao to display specific mode results in a recyclerView */
        if(argsOne.gameMode=="hundredSec") Toast.makeText(requireActivity().applicationContext,argsTwo.gameMode,Toast.LENGTH_LONG).show()
        else                                Toast.makeText(requireActivity().applicationContext,argsOne.gameMode,Toast.LENGTH_LONG).show()

        return binding.root
    }

}