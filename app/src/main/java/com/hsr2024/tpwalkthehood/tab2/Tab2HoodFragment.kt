package com.hsr2024.tpwalkthehood.tab2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hsr2024.tpwalkthehood.databinding.FragmentTab2HoodBinding
import com.hsr2024.tpwalkthehood.login.LoginActivity

class Tab2HoodFragment : Fragment() {

    private val binding by lazy { FragmentTab2HoodBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLoginTest.setOnClickListener {
            startActivity(Intent(requireContext(),LoginActivity::class.java))

        }
    }
}
