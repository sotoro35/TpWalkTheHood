package com.hsr2024.tpwalkthehood.tab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hsr2024.tpwalkthehood.databinding.FragmentTab1WlakBinding

// 서버작업.. 원하는 동네를 검색할수있게끔..AutoCompleteTextView 자동완성기능..
// 기본은 내 위치기반으로 설정된 동네,전체보기모드,카테고리별로 소분류 선택기능
// 지도 플러팅버튼 누르면 탭2로 이동되면서 선택한 분류값도 가져감
// 날씨 api 받아와서 현재 날씨 상단에 아이콘 표시
class Tab1WlakFragment : Fragment() {

    private lateinit var binding:FragmentTab1WlakBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTab1WlakBinding.inflate(layoutInflater)
        return binding.root

    }
}