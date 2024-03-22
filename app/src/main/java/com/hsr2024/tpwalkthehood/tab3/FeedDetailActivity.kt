package com.hsr2024.tpwalkthehood.tab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hsr2024.tpwalkthehood.R

// Feed에서 아이템 클릭시 보여주는 상세내역
// 서버에서 내용 가져와야함
// 좋아요 / 댓글 / 친구신청 클릭시 서버로 정보 전달

class FeedDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_detail)
    }
}