package com.hsr2024.tpwalkthehood.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hsr2024.tpwalkthehood.R


// 닉네임,이메일 중복확인. 모든 값은 필수. 비밀번호 눈 클릭시 보이게(Ex25)
class SignupActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }
}