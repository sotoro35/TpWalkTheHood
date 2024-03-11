package com.hsr2024.tpwalkthehood.tab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hsr2024.tpwalkthehood.R


// 4번째이동, 핸드폰 설정을 하는곳.. 현재는 알림설정만 넣을예정
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}