package com.hsr2024.tpwalkthehood.tab1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.databinding.FragmentTab1WlakBinding
import com.hsr2024.tpwalkthehood.databinding.FragmentTab1testWlakBinding

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

        main = activity as MainActivity


        Log.d("오류", "여기까지 실행")

        //대분류 클릭시 버튼에 클릭리스너
        setCategoryListener()


    }

    //멤버변수(property)
    var categoryId= R.id.category_01
    var searchCategory = ""
    var searchKeyword = ""


    private fun setCategoryListener(){

        binding.categoryBtns.category01.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category02.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category03.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category04.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category05.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category06.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category07.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category08.setOnClickListener { clickCategory(it) }

    } // listner

    private fun clickCategory(view:View){


        // 클릭한 뷰를 파란색으로 변경
        val clickedTextView = view.findViewById<TextView>(view.id)
        clickedTextView?.setTextColor(Color.BLUE)

        // 나머지 뷰들을 검은색으로 변경
        val categoryIds = arrayOf(
            R.id.category_01, R.id.category_02, R.id.category_03, R.id.category_04,
            R.id.category_05, R.id.category_06, R.id.category_07, R.id.category_08
        )
        for (categoryId in categoryIds) {
            if (categoryId != view.id) {
                val textView = view.findViewById<TextView>(categoryId)
                textView?.setTextColor(Color.BLACK)
            }
        }

        //view.findViewById<TextView>(categoryId).setTextColor(Color.BLACK)
        //if (view is TextView) view.setTextColor(Color.BLUE)


        //클릭한 뷰의 id를 저장
        categoryId= view.id
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
        } // when...




           main.searchPlaces(searchCategory,searchKeyword)

        }


    } // fragment..



