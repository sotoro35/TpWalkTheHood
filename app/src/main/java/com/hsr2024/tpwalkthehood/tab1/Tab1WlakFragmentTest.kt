package com.hsr2024.tpwalkthehood.tab1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.adapter.PlaceItemAdapter
import com.hsr2024.tpwalkthehood.adapter.subCategoryTestAdapter
import com.hsr2024.tpwalkthehood.data.CategoryItem
import com.hsr2024.tpwalkthehood.databinding.FragmentTab1testWlakBinding
import okhttp3.internal.notify

class Tab1WlakFragmentTest : Fragment(){

    private lateinit var binding:FragmentTab1testWlakBinding
    lateinit var main:MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTab1testWlakBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //대분류 클릭시 버튼에 클릭리스너
        setCategoryListener()

        main = activity as MainActivity

        //main.placeResponse?: return
        if (main.placeResponse !== null){
            val placeAdapter = PlaceItemAdapter(requireContext(),main.placeResponse!!.documents)
            binding.recyclerSubItem.adapter = placeAdapter
            placeAdapter.notifyDataSetChanged()
        }

        first(binding.categoryBtns.category01)


    }//onViewCreated......

    private fun first(view:View){
        val clickedCategoryId = view.id
        val subcategoryData = setupSubcategoryData(clickedCategoryId)
        setupSearchKeywords(clickedCategoryId)
        subsubcategoryAdapter = subCategoryTestAdapter(requireContext(), subcategoryData) { clickedItem ->
        }

        // 리사이클러뷰에 어댑터 설정
        binding.reyclerViewSubCategory.adapter = subsubcategoryAdapter
        subsubcategoryAdapter.notifyDataSetChanged()

    }


    //멤버변수(property)
    var categoryId= R.id.category_01
    var searchCategory = ""
    var searchKeyword = ""

    lateinit var subsubcategoryAdapter:subCategoryTestAdapter


    private fun setCategoryListener(){

        binding.categoryBtns.category01.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category02.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category03.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category04.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category05.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category06.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category07.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category08.setOnClickListener { clickCategory(it) }



    } // listner......



    var SelectedCategory: Int = -1

    private fun clickCategory(view:View){

        if (SelectedCategory != -1) {
            val prevView = binding.root.findViewById<TextView>(SelectedCategory)
            prevView?.setTextColor(Color.parseColor("#757474"))
        }
        // 현재 클릭한 텍스트뷰의 색상을 변경합니다.
        (view as TextView).setTextColor(Color.parseColor("#FF009688"))

        // 현재 클릭한 텍스트뷰의 id를 저장하여 다음에 선택 색상을 변경할 때 사용합니다.
        SelectedCategory = view.id


        ///// 클릭 했을때 색상 변경 /////////////


        var subsearchKeyword = ""

        // 클릭한 카테고리에 따라 서브 카테고리 데이터와 검색 키워드를 설정합니다.
        val clickedCategoryId = view.id
        val subcategoryData = setupSubcategoryData(clickedCategoryId)
        setupSearchKeywords(clickedCategoryId)

        main.searchPlaces(searchCategory,searchKeyword)

        // 서브 카테고리 어댑터를 업데이트합니다.
        subsubcategoryAdapter = subCategoryTestAdapter(requireContext(), subcategoryData) { clickedItem ->
            // 클릭한 서브 카테고리에 대한 동작 처리
            if (!clickedItem.category.equals("전체보기")) {
                subsearchKeyword = clickedItem.category
            } else {
                subsearchKeyword = searchKeyword
            }
            // 메인 액티비티의 searchPlaces 메소드 호출
            main.searchPlaces(searchCategory, subsearchKeyword)
        }

        // 리사이클러뷰에 어댑터 설정
        binding.reyclerViewSubCategory.adapter = subsubcategoryAdapter
        subsubcategoryAdapter.notifyDataSetChanged()
    }

        private fun setupSubcategoryData(clickedCategoryId: Int): List<CategoryItem> {
            return when(clickedCategoryId){
                R.id.category_01 -> listOf(
                    CategoryItem("전체보기",R.drawable.icon_all),
                    CategoryItem("한식",R.drawable.icon_rice),
                    CategoryItem("중식",R.drawable.icon_noodle),
                    CategoryItem("일식",R.drawable.icon_sushi),
                    CategoryItem("피자",R.drawable.icon_pizza),
                    CategoryItem("치킨",R.drawable.icon_chicke),
                    CategoryItem("분식",R.drawable.icon_ricecake),
                    CategoryItem("애견동반",R.drawable.icon_pet)
                )
                R.id.category_02 -> listOf(
                    CategoryItem("전체보기",R.drawable.icon_all),
                    CategoryItem("애견동반",R.drawable.icon_pet)
                )
                R.id.category_03 -> listOf(
                    CategoryItem("전체보기",R.drawable.icon_all),
                    CategoryItem("어린이",R.drawable.icon_childre)
                )
                R.id.category_04 -> listOf(CategoryItem("전체보기",R.drawable.icon_all))
                R.id.category_05 -> listOf(CategoryItem("전체보기",R.drawable.icon_all))
                R.id.category_06 -> listOf(CategoryItem("전체보기",R.drawable.icon_all))
                R.id.category_07 -> listOf(
                    CategoryItem("전체보기",R.drawable.icon_all),
                    CategoryItem("내과",R.drawable.icon_stethoscope),
                    CategoryItem("이비인후과",R.drawable.icon_otorhinolaryngology),
                    CategoryItem("정형외과",R.drawable.icon_bone),
                    CategoryItem("소아청소년과",R.drawable.icon_pediatric),
                    CategoryItem("산부인과",R.drawable.icon_pregnant),
                    CategoryItem("동물",R.drawable.icon_pet)
                )
                R.id.category_08 -> listOf(
                    CategoryItem("전체보기",R.drawable.icon_all),
                    CategoryItem("동물",R.drawable.icon_pet)
                )
                else -> emptyList()
            }
        }

        private fun setupSearchKeywords(categoryId: Int) {
            when(categoryId) {
                R.id.category_01 -> {
                    searchCategory = "FD6"
                    searchKeyword = "음식점"
                }
                R.id.category_02 -> {
                    searchCategory = "CE7"
                    searchKeyword = "카페"
                }
                R.id.category_03 -> {
                    searchCategory = "CT1"
                    searchKeyword = "문화시설"
                }
                R.id.category_04 -> {
                    searchCategory = "CS2"
                    searchKeyword = "편의점"
                }
                R.id.category_05 -> {
                    searchCategory = "MT1"
                    searchKeyword = "마트"
                }
                R.id.category_06 -> {
                    searchCategory = "BK9"
                    searchKeyword = "은행"
                }
                R.id.category_07 -> {
                    searchCategory = "HP8"
                    searchKeyword = "병원"
                }
                R.id.category_08 -> {
                    searchCategory = "PM9"
                    searchKeyword = "약국"
                }
            }
        }

} // fragment..



