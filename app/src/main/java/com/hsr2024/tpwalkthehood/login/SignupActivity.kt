package com.hsr2024.tpwalkthehood.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.databinding.ActivitySignupBinding


// 닉네임,이메일 중복확인. 모든 값은 필수. 저장정보 서버로 전송
// 차후에는.. 동네를 틀리면 안되니까.. 동네를 선택할수있게끔 바꾸기
class SignupActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }

    lateinit var nickname:String
    lateinit var email:String

    var hoodList = arrayListOf<String>(
        "전체보기","강남구","강동구","강북구","강서구","관악구","광진구","구로구",
        "금천구","노원구","도봉구","동대문구","동작구","마포구","서대문구","서초구",
        "성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구",
        "중구","중랑구")

    lateinit var hoodselect:AutoCompleteTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnSignup.setOnClickListener { clickSingup() }
        binding.btnNicknameCheck.setOnClickListener { clickCheckName() }
        binding.btnEmailCheck.setOnClickListener { clickCheckEmail() }

    }

    private fun clickSingup(){
        nickname = binding.inputLayoutNickname.editText!!.text.toString()
        email = binding.inputLayoutEmail.editText!!.text.toString()
        var password = binding.inputLayoutPassword.editText!!.toString()
        var passwordConfirm = binding.inputLayoutPasswordConfirm.editText!!.toString()

        hoodselect= binding.inputLayoutHood.editText!! as AutoCompleteTextView


    }

    private fun clickCheckName(){


    }

    private fun clickCheckEmail(){

    }

    private fun loadData(nickname:String,email:String) : Boolean{

        return true
    }


}