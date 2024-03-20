package com.hsr2024.tpwalkthehood.tab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.databinding.ActivityEditBinding


class EditActivity : AppCompatActivity() {
    val binding by lazy { ActivityEditBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //구현목록
        //myeditTitle,myedittext의 내용과 이미지 서버에 저장

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.myeditSave.setOnClickListener {  } // 서버에 저장
        binding.tvMyeditIvselect.setOnClickListener {  } // 이미지 선택창

    }
}