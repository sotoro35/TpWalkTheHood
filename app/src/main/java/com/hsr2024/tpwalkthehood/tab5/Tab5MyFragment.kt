package com.hsr2024.tpwalkthehood.tab5

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.UserAccount
import com.hsr2024.tpwalkthehood.databinding.FragmentTab3FeedBinding
import com.hsr2024.tpwalkthehood.databinding.FragmentTab5MyBinding
import com.hsr2024.tpwalkthehood.login.GuestFragment

// 내정보화면.. 이동과 로그아웃
// 프로필 이미지 선택.. Ex68참고


class Tab5MyFragment : Fragment(){

    private val binding by lazy { FragmentTab5MyBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 텍스트에 밑줄
        binding.btnLogout.paintFlags= Paint.UNDERLINE_TEXT_FLAG
        binding.tvNickname.text = G.userAccount?.nickname
        binding.btnLogout.setOnClickListener { clickLogout() }
    }

    private fun clickLogout(){
        val preferences: SharedPreferences = (activity as MainActivity).getSharedPreferences("UserData",
            AppCompatActivity.MODE_PRIVATE)

        val editor:Editor = preferences.edit()
        editor.clear()
        editor.apply()

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container_fragment, GuestFragment())
        transaction.commit()

        G.userAccount = null

    }
}