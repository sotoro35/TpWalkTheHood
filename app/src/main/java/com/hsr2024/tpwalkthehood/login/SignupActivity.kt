package com.hsr2024.tpwalkthehood.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
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

    var nickname:String= ""
    var email:String = ""
    var password:String = ""
    var passwordConfirm:String = ""

    var checkNickname = false
    var checkEmail = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnSignup.setOnClickListener { clickSingup() }
        binding.btnNicknameCheck.setOnClickListener { clickCheckName() }
        binding.btnEmailCheck.setOnClickListener { clickCheckEmail() }

        binding.inputLayoutNickname.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if( checkNickname && nickname!=s.toString()) checkNickname=false
                Toast.makeText(this@SignupActivity, "$checkNickname: $s", Toast.LENGTH_SHORT).show()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }) //////

        binding.inputLayoutEmail.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if( checkEmail && email!=s.toString()) checkEmail=false
            }
            override fun afterTextChanged(s: Editable?) {
            }

        })


    }// onCreate...


    private fun clickSingup(){
        // 작성한 데이터를 서버에 업로그하기

        // 중복확인을 했는지 확인..하고 중복확인이 성공했다면 서버에 저장

        // 닉네임에 특수문자 못들어가게.. 이메일에 @들어가게...

        nickname = binding.inputLayoutNickname.editText!!.text.toString()
        email = binding.inputLayoutEmail.editText!!.text.toString()
        password = binding.inputLayoutPassword.editText!!.text.toString()
        passwordConfirm = binding.inputLayoutPasswordConfirm.editText!!.text.toString()

            if (saveCheck(nickname,email,password,passwordConfirm) && checkNickname && checkEmail){

                val retrofit = RetrofitHelper.getRetrofitInstance()
                val retrofitService = retrofit.create(RetrofitService::class.java)

                val userData= UserSignupData(nickname, email, password)
                retrofitService.userDataToServer(userData).enqueue(object : Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        var s= response.body().toString()
                        Toast.makeText(this@SignupActivity, s, Toast.LENGTH_SHORT).show()
                        if (s.trim().equals("회원가입이 완료되었습니다.")) finish()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@SignupActivity, "서버오류 ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("서버오류","${t.message}")
                    }
                })//callback
            }else AlertDialog.Builder(this).setMessage("중복확인을 해주세요").create().show()


    }

    private fun saveCheck(nickname:String,email: String,password:String,passwordConfirm:String) : Boolean {

        var boolean = false

        when{
            !email.contains("@") -> {
                AlertDialog.Builder(this).setMessage("@넣어 입력해주세요").create().show()
                boolean = false
            }

            password != passwordConfirm -> {
                AlertDialog.Builder(this).setMessage("패스워드가 다릅니다.다시 확인해주세요").create().show()
                boolean = false
            }

            nickname.length < 2 -> {
                AlertDialog.Builder(this).setMessage("닉네임이 너무 짧습니다").create().show()
                boolean = false
            }

            password.length < 4 -> {
                AlertDialog.Builder(this).setMessage("비밀번호가 너무 짧습니다").create().show()
                boolean = false
            }

            password.contains(" ") || nickname.contains(" ") || email.contains(" ") -> {
                AlertDialog.Builder(this).setMessage("띄어쓰기는 사용할 수 없습니다").create().show()
                boolean = false
            }

            !nickname.isNotEmpty() && !email.isNotEmpty() && !password.isNotEmpty() && !passwordConfirm.isNotEmpty() -> {
                AlertDialog.Builder(this).setMessage("모두 입력해주세요").create().show()
                boolean = false
            }

            else -> boolean = true
        } // when...

        return boolean
    }




    private fun clickCheckName(){

        nickname = binding.inputLayoutNickname.editText!!.text.toString()

        if (nickname.contains(" ")) {
            AlertDialog.Builder(this).setMessage("띄어쓰기는 사용할 수 없습니다").create().show()

        }else if (nickname.length < 2 || nickname.length >= 9){
                AlertDialog.Builder(this).setMessage("2~8자 이내로 입력하세요").create().show()

        }else {
            val retrofit = RetrofitHelper.getRetrofitInstance()
            val retrofitService = retrofit.create(RetrofitService::class.java)
            retrofitService.userCheckNickname(nickname).enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    var s= response.body().toString()
                    //AlertDialog.Builder(this@SignupActivity).setMessage(s).create().show()
                    checkNickname= s.trim().toBoolean()
                    if (checkNickname) {
                        AlertDialog.Builder(this@SignupActivity).setMessage("사용가능 합니다").create().show()
                        binding.inputLayoutNickname.editText?.clearFocus()
                        checkNickname = true
                    }
                    else AlertDialog.Builder(this@SignupActivity).setMessage("이미 사용중입니다").create().show()

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@SignupActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })

        }

    }

    private fun clickCheckEmail(){
        var email = binding.inputLayoutEmail.editText!!.text.toString()

        if (email.contains(" ")) {
            AlertDialog.Builder(this).setMessage("띄어쓰기는 사용할 수 없습니다").create().show()

        }else if (!email.contains("@")){
            AlertDialog.Builder(this).setMessage("@넣어 입력해주세요").create().show()

        }else {
            val retrofit = RetrofitHelper.getRetrofitInstance()
            val retrofitService = retrofit.create(RetrofitService::class.java)
            retrofitService.userCheckEmail(email).enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    var s= response.body().toString()
                    checkEmail= s.trim().toBoolean()
                    if (checkEmail) {
                        AlertDialog.Builder(this@SignupActivity).setMessage("사용가능 합니다").create().show()
                        binding.inputLayoutEmail.editText?.clearFocus()
                        checkEmail = true
                    }
                    else AlertDialog.Builder(this@SignupActivity).setMessage("이미 사용중입니다").create().show()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@SignupActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }


}