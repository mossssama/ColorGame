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
import com.example.colorgame.databinding.FragmentCongratsBinding

class CongratsFragment : Fragment() {
    private val args: GamePlayFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCongratsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_congrats,container,false)

        Toast.makeText(requireActivity().applicationContext,"${args.score}", Toast.LENGTH_LONG).show()

        return binding.root
    }
}