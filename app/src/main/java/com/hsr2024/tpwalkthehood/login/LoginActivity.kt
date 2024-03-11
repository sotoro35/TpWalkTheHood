package com.hsr2024.tpwalkthehood.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener { startActivity(Intent(this,SignupActivity::class.java)) }
        binding.btnLogin.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

        binding.btnBack.setOnClickListener { finish() }


    }
}