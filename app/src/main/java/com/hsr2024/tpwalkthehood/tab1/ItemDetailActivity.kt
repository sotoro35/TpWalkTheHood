package com.hsr2024.tpwalkthehood.tab1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.google.gson.Gson
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.Place
import com.hsr2024.tpwalkthehood.databinding.ActivityItemDetailBinding

//아이템 클릭시 웹뷰로 보여주기.. 서버작업... 뒤로가기버튼.. 찜버튼
class ItemDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityItemDetailBinding.inflate(layoutInflater) }
    private lateinit var place:Place
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val s:String? = intent.getStringExtra("place")
        s?.also {//also는 전체를 리턴

            place = Gson().fromJson(it,Place::class.java) // json --> 객체로

            // 웹뷰 필수 3가지 설정
            binding.webView.webViewClient = WebViewClient() // 내가 만든 뷰 안에 들어가도록 설정
            binding.webView.webChromeClient = WebChromeClient() // 웹 문서안에 다이얼로그 같은 기능이 뜰 수 있도록 설정
            binding.webView.settings.javaScriptEnabled = true // 자바스크립트 허용

            binding.webView.loadUrl(place.place_url)

        }

    }


    // 뒤로가기 버튼 눌렀을때 1칸만 뒤로 가도록...

    override fun onBackPressed(){
        if (binding.webView.canGoBack()) binding.webView.goBack() // 뒤로가는 페이지가 있다면 그 페이지로 가
        else super.onBackPressed() // 걍 꺼버려
    }
}