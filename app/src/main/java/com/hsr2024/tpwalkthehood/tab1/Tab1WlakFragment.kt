package com.hsr2024.tpwalkthehood.tab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hsr2024.tpwalkthehood.databinding.FragmentTab1WlakBinding

class Tab1WlakFragment : Fragment() {

    private lateinit var binding:FragmentTab1WlakBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTab1WlakBinding.inflate(layoutInflater)
        return binding.root

    }
}