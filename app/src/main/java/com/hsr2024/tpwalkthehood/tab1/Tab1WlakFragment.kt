package com.hsr2024.tpwalkthehood.tab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.adapter.MainCategoryAdapter
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

    } //onCreateView...

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reyclerViewMainCategory.adapter = MainCategoryAdapter(requireContext(),
            resources.getStringArray(R.array.food_categories)){

        }


    }//onViewCreated...

//    private fun showSubCategories(category: String) {
//        // 선택된 대분류에 따라 소분류를 설정합니다.
//        val subCategoriesArray = when (category) {
//            "음식점" -> resources.getStringArray(R.array.food_categories)
//            "카페" -> resources.getStringArray(R.array.cafe_categories)
//            "문화시설" -> resources.getStringArray(R.array.cultural_facility_categories)
//            else -> emptyArray() // 다른 대분류에 대한 처리 추가
//        }
//
//        // 소분류를 보여줄 리사이클러뷰에 대한 설정
//        val subCategoriesRecyclerView: RecyclerView = view.findViewById(R.id.subCategoriesRecyclerView)
//        subCategoriesAdapter = SubCategoryAdapter(requireContext(), subCategoriesArray) { subCategory ->
//            // 클릭한 소분류에 대한 처리
//            // 이 부분에서는 API로 가져온 내용을 보여주는 리사이클러뷰를 초기화하고 연결합니다.
//        }
//        subCategoriesRecyclerView.adapter = subCategoriesAdapter
//    }



}//....