package com.hsr2024.tpwalkthehood.tab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hsr2024.tpwalkthehood.R

// 1번째이동.. 내 프로필 수정버전
// 불러오기는 비밀번호를 뺀 나머지를 다 불러옴. 연결된 계정을 보여줌. 이메일주소 or 카카오 or 구글 or 네이버
// 서버에서 내 프로필 내용 가져오기, 저장을 누르면 수정된 내역 서버로 전송
// 프로필사진,닉네임,비밀번호,동네 수정가능.
class ChangeProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)
    }
}