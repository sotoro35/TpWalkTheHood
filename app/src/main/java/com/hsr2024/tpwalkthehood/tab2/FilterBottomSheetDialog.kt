package com.hsr2024.tpwalkthehood.tab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hsr2024.tpwalkthehood.R

// 카테고리별로 또다시 선택하는 창을 만들기에는..... 이건 보류...
// 내 위치기준으로 모든 정보가 다 나옴
// 탭1에서 왔다면 탭1의 정보를 토대로 정보가 나옴
// 핀 클릭시 하단에 아이템 바텀시트가 올라오며, 클릭시 탭1의 DetailActivity로 이동

class FilterBottomSheetDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_bottom_sheet_dialog)
    }
}