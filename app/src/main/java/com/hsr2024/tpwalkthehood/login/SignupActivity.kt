package com.hsr2024.tpwalkthehood.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.UserSignupData
import com.hsr2024.tpwalkthehood.databinding.ActivitySignupBinding
import com.hsr2024.tpwalkthehood.network.RetrofitHelper
import com.hsr2024.tpwalkthehood.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create


// 닉네임,이메일 중복확인. 모든 값은 필수. 저장정보 서버로 전송
// 동네설정을 뺌.. 현재 위치를 기반으로 내 동이 설정됨
// 차후에는.. 동네를 틀리면 안되니까.. 동네를 선택할수있게끔 바꾸기
class SignupActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }

    lateinit var nickname:String
    lateinit var email:String

    var nicknameCheck:Boolean = false
    var emailCheck:Boolean =false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnSignup.setOnClickListener { clickSingup() }
        binding.btnNicknameCheck.setOnClickListener { clickCheckName() }
        binding.btnEmailCheck.setOnClickListener { clickCheckEmail() }

    }

    private fun clickSingup(){
        // 작성한 데이터를 서버에 업로그하기

        // 중복확인을 했는지 확인..하고 중복확인이 성공했다면 서버에 저장

        // 닉네임에 특수문자 못들어가게.. 이메일에 @들어가게...

        nickname = binding.inputLayoutNickname.editText!!.text.toString()
        email = binding.inputLayoutEmail.editText!!.text.toString()
        var password = binding.inputLayoutPassword.editText!!.text.toString()
        var passwordConfirm = binding.inputLayoutPasswordConfirm.editText!!.text.toString()

        if (nickname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()){

            if (password != passwordConfirm){
                AlertDialog.Builder(this).setMessage("패스워드가 다릅니다.다시 확인해주세요").create().show()
                binding.inputLayoutPasswordConfirm.editText!!.selectAll()
                return
            }

            if (nicknameCheck && emailCheck){

                val retrofit = RetrofitHelper.getRetrofitInstance()
                val retrofitService = retrofit.create(RetrofitService::class.java)

                val userData= UserSignupData(nickname, email, password)
                retrofitService.userDataToServer(userData).enqueue(object : Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        var s= response.body().toString()
                        Toast.makeText(this@SignupActivity, s, Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@SignupActivity, "서버오류 ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("서버오류","${t.message}")
                    }

                })//callback
            }else AlertDialog.Builder(this).setMessage("중복확인을 해주세요").create().show()

        } else AlertDialog.Builder(this).setMessage("모두 입력해주세요").create().show()


    }

    private fun clickCheckName():Boolean{
        var check:Boolean= false
        var nickname = binding.inputLayoutNickname.editText!!.text.toString()

        val retrofit = RetrofitHelper.getRetrofitInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.userCheckNickname(nickname).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var s= response.body().toString()
                AlertDialog.Builder(this@SignupActivity).setMessage(s).create().show()
//                check=s.toBoolean()
//                if (check){
//                    AlertDialog.Builder(this@SignupActivity).setMessage("이미사용중입니다").create().show()
//                }else {
//                    AlertDialog.Builder(this@SignupActivity).setMessage("사용가능합니다").create().show()
//                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

        return check
    }

    private fun clickCheckEmail():Boolean{


        return true

    }

    private fun loadData(nickname:String,email:String) : Boolean{

        return true
    }


}