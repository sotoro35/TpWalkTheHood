package com.hsr2024.tpwalkthehood.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.UserAccount

// 인트로화면... 1.5초 뒤 메인으로 이동
class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        Glide.with(this).load(R.drawable.logo).into(findViewById(R.id.logo))

        val preferences: SharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)

        //var s = preferences.getString("email","")?: ""

        // 자료형별로 데이터 가져오기
        G.userAccount = UserAccount("","","","")
        G.userAccount?.nickname= preferences.getString("nickname","")?: ""
        G.userAccount?.email= preferences.getString("email","")?: ""
        G.userAccount?.password= preferences.getString("password","")?: ""
        G.userAccount?.imgfile = preferences.getString("imgfile","")?: ""


        //AlertDialog.Builder(this).setMessage(s).create().show()
       // AlertDialog.Builder(this).setMessage("${G.userAccount?.email}").create().show()

//        findViewById<ImageView>(R.id.logo).setOnClickListener {
//            startActivity(Intent(this,MainActivity::class.java))
//        }


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish() },1500)
    }


}