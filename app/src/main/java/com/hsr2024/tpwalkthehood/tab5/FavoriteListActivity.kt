package com.hsr2024.tpwalkthehood.tab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.databinding.ActivityFavoriteListBinding

// 찜목록... 아이템상세에서 찜한것들을 보여주는곳. 찜은 계정저장(닷홈.. 파이어베이스의 계정과 비교해서..), 폰에저장
// 삭제기능도 있음
class FavoriteListActivity : AppCompatActivity() {

    val binding by lazy { ActivityFavoriteListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}