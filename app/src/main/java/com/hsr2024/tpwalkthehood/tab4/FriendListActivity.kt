package com.hsr2024.tpwalkthehood.tab4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hsr2024.tpwalkthehood.R


//친구 목록을 보여줌, 게시물 이동 클릭시 친구의 게시물만 보여주는 페이지로 이동
// 친구삭제를 누르면 목록에서 삭제됨, 채팅방도 함께 삭제
class FriendListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)
    }
}