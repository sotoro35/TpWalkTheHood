package com.hsr2024.tpwalkthehood.tab5

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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

    val imgUrl= "http://ruaris.dothome.co.kr/WalkTheHood/${G.userAccount?.imgfile}"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("이미지확인", imgUrl)

        // 텍스트에 밑줄
        binding.btnLogout.paintFlags= Paint.UNDERLINE_TEXT_FLAG
        binding.tabTvNickname.text = G.userAccount?.nickname

        if (G.userAccount?.imgfile.equals("") || G.userAccount?.imgfile == null){
            binding.tabIvProfile.setImageResource(R.drawable.profile)
        }else Glide.with(requireContext()).load(imgUrl).into(binding.tabIvProfile)

        binding.btnLogout.setOnClickListener { clickLogout() }

        binding.changeProfile.setOnClickListener { startActivity(Intent(requireContext(),ChangeProfileActivity::class.java)) }
        binding.myFeed.setOnClickListener { startActivity(Intent(requireContext(),MyFeedActivity::class.java)) }
        binding.favorite.setOnClickListener { startActivity(Intent(requireContext(),FavoriteListActivity::class.java)) }
        binding.contactus.setOnClickListener { startActivity(Intent(requireContext(),ContactusActivity::class.java)) }
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

    fun reloadMypage(){
//fun reloadMypage(nickname:String,imgUrl:String){
        binding.tabTvNickname.text = G.userAccount?.nickname
        Glide.with(requireContext()).load(imgUrl).into(binding.tabIvProfile)
    }
}