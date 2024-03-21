package com.hsr2024.tpwalkthehood.login

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.data.UserAccount
import com.hsr2024.tpwalkthehood.data.UserLoginData
import com.hsr2024.tpwalkthehood.data.UserLoginResponse
import com.hsr2024.tpwalkthehood.databinding.ActivityLoginBinding
import com.hsr2024.tpwalkthehood.network.RetrofitHelper
import com.hsr2024.tpwalkthehood.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// 로그인 화면...
// 회원가입 이동 / 로그인 하기(서버에 있는지 비교) / 간편로그인 기능 /
class LoginActivity : AppCompatActivity() {

    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener { startActivity(Intent(this,SignupActivity::class.java)) }
        binding.tvGo.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
        binding.btnLogin.setOnClickListener { clickLogin() }

    }

    private fun clickLogin(){
        var email= binding.inputLayoutId.editText!!.text.toString()
        var password= binding.inputLayoutPassword.editText!!.text.toString()

        val retrofit= RetrofitHelper.getRetrofitInstance("http://ruaris.dothome.co.kr")
        val retrofitService = retrofit.create(RetrofitService::class.java)
        val loginData= UserLoginData(email,password)
        retrofitService.userLoginToServer(loginData).enqueue(object : Callback<UserLoginResponse>{
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                val userResponse= response.body()

                G.userAccount?.imgfile = userResponse?.user?.imgfile?: ""

                userResponse?.user?.apply {
                    if (G.userAccount?.email == null || G.userAccount?.email == "") {
                        G.userAccount?.email = userResponse.user.email
                        G.userAccount?.password = userResponse.user.password
                        G.userAccount?.nickname = userResponse.user.nickname

                        saveSharedPreferences()
                        finish()

                    }

                }?: AlertDialog.Builder(this@LoginActivity).setMessage("이메일과 비밀번호를 확인하세요").create().show()

            }


            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
            }

        })
    }


    fun saveSharedPreferences(){
        // SharedPreference 로 저장하기 - "Data.xml"파일에 저장해주는 객체를 소환하기
        val preferences:SharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        // 저장작업 시작! -- 작성자 객체를 리턴해 줌
        val edior:Editor = preferences.edit()
        // 작성자를 통해 데이터를 작성
        edior.putString("nickname",G.userAccount?.nickname)
        edior.putString("email",G.userAccount?.email)
        edior.putString("password",G.userAccount?.password)
        edior.putString("imgfile",G.userAccount?.imgfile)
        edior.apply()

    }

}// main...

