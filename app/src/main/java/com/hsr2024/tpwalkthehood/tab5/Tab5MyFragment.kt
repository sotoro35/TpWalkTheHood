package com.hsr2024.tpwalkthehood.tab5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hsr2024.tpwalkthehood.databinding.FragmentTab3FeedBinding
import com.hsr2024.tpwalkthehood.databinding.FragmentTab5MyBinding

// 내정보화면.. 이동과 로그아웃


class Tab5MyFragment : Fragment(){

    private val binding by lazy { FragmentTab5MyBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}