package com.hsr2024.tpwalkthehood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// 탭4,탭5 게스트 화면
// 계정이 없다면 이 화면을 띄우게 함

class GuestFragment : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_guest)
    }
}