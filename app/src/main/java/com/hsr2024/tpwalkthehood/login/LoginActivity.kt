package com.hsr2024.tpwalkthehood.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        val retrofit= RetrofitHelper.getRetrofitInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)
        val loginData= UserLoginData(email,password)
        retrofitService.userLoginToServer(loginData).enqueue(object : Callback<UserLoginResponse>{
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                val userResponse= response.body()
                userResponse?.user?.apply {
                    val loadAccount:UserAccount = this
                    if (G.userAccount == null) G.userAccount = UserAccount(this.email,this.password, this.nickname)

                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                    finish()

                    } ?: AlertDialog.Builder(this@LoginActivity).setMessage("이메일과 비밀번호를 확인하세요").create().show()
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
            }

        })
    }

}// main...

