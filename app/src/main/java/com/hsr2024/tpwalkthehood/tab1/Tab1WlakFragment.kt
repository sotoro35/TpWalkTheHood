//package com.hsr2024.tpwalkthehood.tab1
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.hsr2024.tpwalkthehood.MainActivity
//import com.hsr2024.tpwalkthehood.R
//import com.hsr2024.tpwalkthehood.adapter.MainCategoryAdapter
//import com.hsr2024.tpwalkthehood.adapter.PlaceTab1RecyclerAdapter
//import com.hsr2024.tpwalkthehood.adapter.SubCategoryAdapter
//import com.hsr2024.tpwalkthehood.databinding.FragmentTab1WlakBinding
//
//// 서버작업.. 원하는 동네를 검색할수있게끔..AutoCompleteTextView 자동완성기능..
//// 기본은 내 위치기반으로 설정된 동네,전체보기모드,카테고리별로 소분류 선택기능
//// 지도 플러팅버튼 누르면 탭2로 이동되면서 선택한 분류값도 가져감
//// 날씨 api 받아와서 현재 날씨 상단에 아이콘 표시
//class Tab1WlakFragment : Fragment() {
//
//    private lateinit var binding:FragmentTab1WlakBinding
//    private lateinit var mainActivity: MainActivity
//
//    lateinit var tab1RecyclerAdapter : PlaceTab1RecyclerAdapter
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentTab1WlakBinding.inflate(layoutInflater)
//        return binding.root
//
//    } //onCreateView...
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        mainActivity = requireActivity() as MainActivity
//
//
//
//        // 대분류 어댑터 설정
//        val mainCategories = resources.getStringArray(R.array.main_categories)
//        val mainCategoryAdapter = MainCategoryAdapter(requireContext(), mainCategories) { category ->
//            mainActivity.onCategorySelected(category,"전체보기")
//
//            showSubCategories(category)
//        }
//
//        binding.reyclerViewMainCategory.adapter = mainCategoryAdapter
//
//        // "음식점" 대분류를 초기 선택 상태로 설정
//        mainCategoryAdapter.selectItem(0)
//
//        // "음식점"에 해당하는 소분류 목록을 표시
//        showSubCategories(mainCategories[0])
//
//
//
//
//
//    }//onViewCreated...
//
//    override fun onResume() {
//        super.onResume()
//
//        mainActivity.placeResponse?: return
//        tab1RecyclerAdapter = PlaceTab1RecyclerAdapter(requireContext(),mainActivity.placeResponse!!.documents)
//        binding.recyclerTab1.adapter = tab1RecyclerAdapter
//
//
//    }
//
//    private fun showSubCategories(category: String){
//        //선택된 분류에 따라 소분류를 설정
//
//        val subCategoryArray = when(category){
//            "음식점" -> resources.getStringArray(R.array.food_categories)
//            "카페" -> resources.getStringArray(R.array.cafe_categories)
//            "문화시설" -> resources.getStringArray(R.array.cultural_facility_categories)
//            "편의점" -> resources.getStringArray(R.array.convenience_store_categories)
//            "마트" -> resources.getStringArray(R.array.mart_categories)
//            "은행" -> resources.getStringArray(R.array.bank_categories)
//            "병원" -> resources.getStringArray(R.array.hospital_categories)
//            "약국" -> resources.getStringArray(R.array.pharmacy_categories)
//
//            else -> emptyArray()
//        }
//
//        // 각 소분류에 대한 이미지 배열
//        val subCategoryImages = when (category) {
//            "음식점" -> arrayOf(R.drawable.icon_all, R.drawable.icon_rice, R.drawable.icon_noodle,
//                R.drawable.icon_sushi,R.drawable.icon_pizza,R.drawable.icon_chicke,R.drawable.icon_ricecake,R.drawable.icon_pet)
//            "카페" -> arrayOf(R.drawable.icon_all, R.drawable.icon_pet)
//            "문화시설" -> arrayOf(R.drawable.icon_all, R.drawable.icon_childre)
//            "편의점" -> arrayOf(R.drawable.icon_all)
//            "마트" -> arrayOf(R.drawable.icon_all)
//            "은행" -> arrayOf(R.drawable.icon_all)
//            "병원" -> arrayOf(R.drawable.icon_all,R.drawable.icon_stethoscope,R.drawable.icon_otorhinolaryngology,R.drawable.icon_bone,
//                R.drawable.icon_pediatric,R.drawable.icon_pregnant, R.drawable.companion_pet1)
//            "약국" -> arrayOf(R.drawable.icon_all, R.drawable.companion_pet1)
//            else -> emptyArray()
//        }
//
//        val subCategoryAdapter = SubCategoryAdapter(requireContext(),subCategoryArray, subCategoryImages
//        ){ selectedSubCategory ->
//
//            // 소분류 클릭시 메인메소드 실행...
//
//            if (mainActivity.onCategorySelected(category, selectedSubCategory)){
//                tab1RecyclerAdapter.notifyDataSetChanged()
//            }
//
//        }
//
//        binding.reyclerViewSubCategory.adapter = subCategoryAdapter
//        subCategoryAdapter.selectFirstItem()
//
//
//    }
//
//
//}//....