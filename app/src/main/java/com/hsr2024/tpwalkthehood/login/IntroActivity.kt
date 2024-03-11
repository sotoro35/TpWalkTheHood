package com.hsr2024.tpwalkthehood.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        Glide.with(this).load(R.drawable.logo).into(findViewById(R.id.logo))


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish() },2000)
    }


}