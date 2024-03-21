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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.UserAccount
import com.hsr2024.tpwalkthehood.data.UserLoginData
import com.hsr2024.tpwalkthehood.databinding.FragmentTab3FeedBinding
import com.hsr2024.tpwalkthehood.databinding.FragmentTab5MyBinding
import com.hsr2024.tpwalkthehood.login.GuestFragment
import com.hsr2024.tpwalkthehood.network.RetrofitHelper
import com.hsr2024.tpwalkthehood.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 내정보화면.. 이동과 로그아웃
// 프로필 이미지 선택.. Ex68참고
class Tab5MyFragment : Fragment(){

    private val binding by lazy { FragmentTab5MyBinding.inflate(layoutInflater) }


    var imgUrl= "http://ruaris.dothome.co.kr/WalkTheHood/${G.userAccount?.imgfile}"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("이미지주소", "현재주소:$imgUrl")
       // Log.e("이미지주소","현재주소:${G.userAccount?.imgfile}")

        // 텍스트에 밑줄
        binding.btnLogout.paintFlags= Paint.UNDERLINE_TEXT_FLAG
        binding.btnUserDelete.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        //클릭리스너
        binding.btnLogout.setOnClickListener { clickLogout() }
        binding.btnUserDelete.setOnClickListener { clickDelete() }
        binding.changeProfile.setOnClickListener { startActivity(Intent(requireContext(),ChangeProfileActivity::class.java)) }
        binding.myFeed.setOnClickListener { startActivity(Intent(requireContext(),MyFeedActivity::class.java)) }
        binding.favorite.setOnClickListener { startActivity(Intent(requireContext(),FavoriteListActivity::class.java)) }
        binding.contactus.setOnClickListener { startActivity(Intent(requireContext(),ContactusActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()
        //멤버변수의 값이(imgfile) 교체된걸 새로 넣어줘야 됨...
        imgUrl= "http://ruaris.dothome.co.kr/WalkTheHood/${G.userAccount?.imgfile}"
        reloadMypage()
    }

    lateinit var alertDialog:AlertDialog
    private fun clickDelete(){
        val dialogV = layoutInflater.inflate(R.layout.dialog_userdelete,null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogV)
        alertDialog = builder.create()

        dialogV.findViewById<TextView>(R.id.deleteOk).setOnClickListener {
            val passwordV = dialogV.findViewById<TextInputLayout>(R.id.input_password_delete)
            var password = passwordV.editText!!.text.toString()

            // 서버에서 회원정보 비교 후, 맞다면 회원 정보 삭제
            clickDeleteRetrofit(password)

        }
        alertDialog.show()

    }

    private fun clickDeleteRetrofit(password:String){
        val retrofit = RetrofitHelper.getRetrofitInstance("http://ruaris.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)
        var email = G.userAccount?.email ?: ""
        val loginData= UserLoginData(email,password)
        retrofitService.userDelete(loginData).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var s = response.body()
                AlertDialog.Builder(requireContext()).setMessage(s).create().show()
                clickLogout()

                alertDialog.dismiss()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                Log.e("탈퇴오류","${t.message}")
            }

        })

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

        G.userAccount?.email = ""

    }

    fun reloadMypage(){
//        fun reloadMypage(imgUrl:String){

        binding.tabTvNickname.text = G.userAccount?.nickname
        //Log.e("이미지주소","변경된주소:${G.userAccount?.imgfile}")
        //Log.e("이미지주소","변경된주소:$imgUrl")

        if (G.userAccount?.imgfile.equals("") || G.userAccount?.imgfile == null){
            binding.tabIvProfile.setImageResource(R.drawable.profile)

        } else {
            Log.e("이미지","변경된imgfile값:${G.userAccount?.imgfile}")
            Log.e("이미지주소","변경된주소:$imgUrl")
            Glide.with(requireContext()).load(imgUrl).into(binding.tabIvProfile)
        }
    }
}