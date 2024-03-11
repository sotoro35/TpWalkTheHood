package com.hsr2024.tpwalkthehood.tab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hsr2024.tpwalkthehood.R

// 3번째이동.. 내 게시물보기모드.. Feed의 내용물과 같게 보일거지만 수정삭제버튼이 있음..
// 수정누르면 수정화면으로 이동, 삭제누르면 서버에서 삭제
class MyFeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_feed)
    }
}